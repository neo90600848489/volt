package com.example.assignment.Service;

import com.example.assignment.CommonUtils.RequestUtils;
import com.example.assignment.Enums.BookingStatus;
import com.example.assignment.Enums.ExceptionList;
import com.example.assignment.Enums.Slots;
import com.example.assignment.Exception.BusinessException;
import com.example.assignment.Model.AppointmentRequest;
import com.example.assignment.Model.Appointment;
import com.example.assignment.Model.AppointmentAudit;
import com.example.assignment.Model.OpenSlotResponse.OpenSlotResponseUnit;
import com.example.assignment.Repository.AppointmentAuditRepository;
import com.example.assignment.Repository.AppointmentRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentAuditRepository appointmentAuditRepository;

    @Autowired
    RequestUtils requestUtils;

    public void saveAppointment(AppointmentRequest appointMentRequest) throws BusinessException {
        try {
            // try to create new Appointment with status as (confirmed-1)
            requestUtils.validateAppointmentRequest(appointMentRequest);
            processBookingWithStatus(appointMentRequest,BookingStatus.CONFIRMED);
        }  catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Transactional
    public boolean processBookingWithStatus(AppointmentRequest appointMentRequest, BookingStatus bookingStatus) throws BusinessException {
        Appointment appointment = null;
        boolean isAppointmentCreated = false;
        try {
            // check for any service operator
            if (appointMentRequest.isMultiOperator()) {
                /* count is hard code due to service operator is fixed up to 3
                check and try for every operator at given time and day
                 */
                int count = 1;
                do {
                    appointMentRequest.setOperatorId(count);
                    appointment = createAppointment(appointMentRequest, bookingStatus);
                    if(ObjectUtils.isNotEmpty(appointment)) {
                        isAppointmentCreated = true;
                    }
                    count++;
                }

                while (count < 4 && isAppointmentCreated == false);
            } else {
                /*
                for specific operator flow comes here
                */
                appointment = createAppointment(appointMentRequest, bookingStatus);
                isAppointmentCreated=true;
            }
            if(bookingStatus.getId()==BookingStatus.CONFIRMED.getId()) {
                // if user request for new booking
                Appointment savedAppointment = appointmentRepository.save(appointment);
                AppointmentAudit appointmentAudit = createAppointmentAudit(savedAppointment.getAppointmentId(), appointment.getBookingStatus());
                appointmentAuditRepository.save(appointmentAudit);
            }
            return isAppointmentCreated;
        }
        catch (BusinessException e) {
                throw new BusinessException(e.getId(), e.getMessage());
        }
        catch(Exception e) {
                throw new RuntimeException(e.getMessage());
        }
    }
    private AppointmentAudit createAppointmentAudit(Long appointmentId,int status) {
        AppointmentAudit appointmentAudit = new AppointmentAudit();
        appointmentAudit.setAppointMentStatus(BookingStatus.getNameById(status));
        appointmentAudit.setCreatedTime(new Date());
        appointmentAudit.setAppointmentId(appointmentId);
        return  appointmentAudit;
    }
    private Appointment createAppointment(AppointmentRequest appointMentRequest, BookingStatus status) throws BusinessException {
        Appointment appointment =new Appointment();
        try {
            appointment.setBookingStatus(status.getId());
            appointment.setUserId(1);
            appointment.setDay(appointMentRequest.getDay());
            appointment.setOperatorId(appointMentRequest.getOperatorId());
            boolean isBookingTimeAdded=setBookingTime(appointMentRequest.getStart_time(),appointMentRequest.getOperatorId(),appointMentRequest.getDay(),appointment,appointMentRequest.isMultiOperator());
            if(isBookingTimeAdded) {
                return appointment;
            }
            return  null;
        }
        catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private boolean setBookingTime(int startTime,int operatorId,int day,Appointment appointment,boolean isMultiOperator) throws BusinessException {
        try {
            List<Integer> allstartTimeForDate = appointmentRepository.findAllStartTimeForDate(operatorId, 1, day);
            boolean preScheduled = allstartTimeForDate.stream().anyMatch(data->data==startTime);
            if(!preScheduled)  {
                appointment.setStartTime(startTime);
                return true;
            }
            else {
                if(!isMultiOperator) {
                    throw new BusinessException(ExceptionList.PREVIOUSLY_BOOKED.getId(), ExceptionList.PREVIOUSLY_BOOKED.getText());
                }
            }
            return false;
        }
        catch (BusinessException e)  {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public  List<OpenSlotResponseUnit>  getOpenSlot(int operator) throws BusinessException {
        List<OpenSlotResponseUnit> slotResponse = null;
        try {
            requestUtils.validateOPerator(operator);
            List<Appointment> appointments = appointmentRepository.showOpenSLot(operator, 1);
            Map<Integer, List<Appointment>> operatorAppointments = appointments.stream().collect(Collectors.groupingBy(Appointment::getDay));
            slotResponse = createSlotResponse(operatorAppointments);
        }
        catch (BusinessException e)  {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return  slotResponse;
    }
    private List<OpenSlotResponseUnit>  createSlotResponse(Map<Integer, List<Appointment>> appointmentMap) {
        List<OpenSlotResponseUnit> openSlotResponseUnits = new ArrayList<>();
        Map<Integer, List<Appointment>> sortedMap = appointmentMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        Map::putAll
                );
        IntStream.rangeClosed(1, 7)
                .forEach(day->{
                    OpenSlotResponseUnit openSlotResponseUnit = new OpenSlotResponseUnit();
                    openSlotResponseUnit.setDay("Day  "+ String.valueOf(day));
                    // algorithm to calculate time-Range
                    if(appointmentMap.containsKey(day)) {
                        List<Integer> startTime=appointmentMap.get(day).stream().map(Appointment::getStartTime).collect(Collectors.toList());
                        openSlotResponseUnit.setSlots(calcauteTimeRange(startTime));
                    }
                    else {
                        List<Integer> emptyList= new ArrayList<>();
                        openSlotResponseUnit.setSlots(calcauteTimeRange(emptyList));
                    }
                    openSlotResponseUnits.add(openSlotResponseUnit);
                }
                );

        return  openSlotResponseUnits;
    }

    private Map<String,String> calcauteTimeRange(List<Integer> startTimes) {
        boolean[] data = new boolean[25];
        Arrays.fill(data,false);
        for(int num : startTimes) {
            data[num] = true;
        }
        data[0]=true;
        data[24]=true;
        int curr_index=-1;
        int slotCounter=1;
        Map<String,String> map = new LinkedHashMap<>();
        for(int i=1;i<data.length;i++) {
            if(data[i-1]==true & data[i]==false) {
                curr_index=i;
            }
            else if(data[i-1]==false && data[i]==true) {
                String slotTime=String.valueOf(curr_index)+"  "+String.valueOf(i);
                map.put(Slots.getNameById(slotCounter++),slotTime);
            }
        }
        return  map;
    }
    public   List<OpenSlotResponseUnit>  getBookedSLot(int operator) throws BusinessException {
        try {
            requestUtils.validateOPerator(operator);
            List<Appointment> appointments = appointmentRepository.bookedSlot(operator,1);
            Map<Integer, List<Appointment>> operatorAppointments = appointments.stream().collect(Collectors.groupingBy(Appointment::getDay));
            return createBookedSlotResponse(operatorAppointments);
        }
        catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    private List<OpenSlotResponseUnit> createBookedSlotResponse(Map<Integer, List<Appointment>> operatorAppointments) {
        List<OpenSlotResponseUnit> openSlotResponseUnits = new ArrayList<>();
        IntStream.rangeClosed(1,7).forEach(day->{
            OpenSlotResponseUnit openSlotResponseUnit = new OpenSlotResponseUnit();
            openSlotResponseUnit.setDay("Day  "+ String.valueOf(day));
            // algorithm to calculate time-Range
            if(operatorAppointments.containsKey(day)) {
                List<Integer> startTime=operatorAppointments.get(day).stream().map(Appointment::getStartTime).collect(Collectors.toList());
                openSlotResponseUnit.setSlots(createBookedSlot(startTime));
            }
            else {
                List<Integer> emptyList= new ArrayList<>();
                openSlotResponseUnit.setSlots(createBookedSlot(emptyList));
            }
            openSlotResponseUnits.add(openSlotResponseUnit);
        });
        return  openSlotResponseUnits;
    }

    private Map<String,String> createBookedSlot(List<Integer> startTimes) {
        Map<String,String> map = new LinkedHashMap<>();
        AtomicInteger slot= new AtomicInteger(1);
        startTimes.forEach(startTime->{
            StringBuilder rangeBuilder = new StringBuilder();
            rangeBuilder.append(String.valueOf(startTime)).append(" - ").append(String.valueOf(startTime+1));
            map.put(Slots.getNameById(slot.getAndIncrement()),rangeBuilder.toString());
        });
        return  map;
    }
    // two database update so create transactional
    @Transactional
    public void rescheduleandProcessBookinf(Long appointmentId, AppointmentRequest appointMentRequest) throws BusinessException {
        try {
            boolean isSucessfull = processBookingWithStatus(appointMentRequest, BookingStatus.RESCHEDULED);

            if(isSucessfull) {
                // create appointment_audit

               appointmentRepository.updateBookingStatus(appointmentId, 1, appointMentRequest.getDay(), appointMentRequest.getOperatorId(),appointMentRequest.getStart_time());
                AppointmentAudit appointmentAudit = new AppointmentAudit();
                appointmentAudit.setAppointMentStatus(BookingStatus.RESCHEDULED.getText());
                appointmentAudit.setCreatedTime(new Date());
                appointmentAudit.setAppointmentId(appointmentId);
                appointmentAuditRepository.save(appointmentAudit);
            }
        } catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
    @Transactional
    public void rescheduleAppointment(AppointmentRequest appointMentRequest, Long appointmentId) throws BusinessException {
        try {
           if(appointmentRepository.isAppointmentExist(appointmentId) ) {
               rescheduleandProcessBookinf(appointmentId,appointMentRequest);
           }
           else{
               throw new BusinessException(ExceptionList.INVALID_APPOINTMENT.getId(),ExceptionList.INVALID_APPOINTMENT.getText());
           }
        }
        catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    @Transactional
    public void cancelAppointment(Long appointmentId) throws BusinessException {
        try {
            if (appointmentRepository.isAppointmentExist(appointmentId)) {
                appointmentRepository.updateBookingStatus(appointmentId, 3);
            } else {
                throw new BusinessException(ExceptionList.INVALID_APPOINTMENT.getId(), ExceptionList.INVALID_APPOINTMENT.getText());
            }
            AppointmentAudit appointmentAudit = new AppointmentAudit();
            appointmentAudit.setAppointMentStatus(BookingStatus.CANCELLED.getText());
            appointmentAudit.setCreatedTime(new Date());
            appointmentAudit.setAppointmentId(appointmentId);
            appointmentAuditRepository.save(appointmentAudit);
        }
        catch (BusinessException e) {
            throw new BusinessException(e.getId(),e.getMessage());
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<AppointmentAudit> getAppointAudit(Long appointmentId) {
        List<AppointmentAudit> byOperator = appointmentAuditRepository.findByOperator(appointmentId);
        return  byOperator;
    }
}
