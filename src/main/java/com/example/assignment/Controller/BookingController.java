package com.example.assignment.Controller;

import com.example.assignment.Exception.BusinessException;
import com.example.assignment.Model.AppointmentRequest;
import com.example.assignment.Model.AppointmentAudit;
import com.example.assignment.Model.OpenSlotResponse.OpenSlotResponseUnit;
import com.example.assignment.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volt")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello, world!";
    }

    @RequestMapping(value = "/book" , method=RequestMethod.POST)
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequest request) {

        try {
            bookingService.saveAppointment(request);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body(" Error :: "+ e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error :: server error");
        }
        return  ResponseEntity.ok("Booked");
    }
    @RequestMapping(value = "/openslot/{operatorId}" , method=RequestMethod.GET)
    public ResponseEntity<Object> openSlot(@PathVariable int operatorId) {
        List<OpenSlotResponseUnit> openSlot=null;
        try {
            openSlot = bookingService.getOpenSlot(operatorId);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body(" Error :: "+ e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error :: server error");
        }
        return ResponseEntity.ok(openSlot);
    }
    @RequestMapping(value = "/bookedlot/{operatorId}" , method=RequestMethod.GET)
    public ResponseEntity<Object> bookedSLot(@PathVariable int operatorId) {
        List<OpenSlotResponseUnit> bookedSLot=null;
        try {
            bookedSLot = bookingService.getBookedSLot(operatorId);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body(" Error :: "+ e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error :: server error");
        }
        return ResponseEntity.ok(bookedSLot);
    }
    @RequestMapping(value = "/reschedule/{appointmentId}" , method=RequestMethod.GET)
    public ResponseEntity<String> rescheduleAppointment(@RequestBody AppointmentRequest request, @PathVariable Long appointmentId) throws BusinessException {
        try {
            bookingService.rescheduleAppointment(request, appointmentId);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body(" Error :: "+ e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error :: server error");
        }
         return ResponseEntity.ok("Rescheduked");
    }
    @RequestMapping(value = "/cancel/{appointmentId}" , method=RequestMethod.GET)
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) throws BusinessException {
        try {
            bookingService.cancelAppointment(appointmentId);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body(" Error :: "+ e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error :: server error");
        }
        return ResponseEntity.ok("Cancelled");
    }
    @RequestMapping(value = "/audit/{appointmentId}" , method=RequestMethod.GET)
    public ResponseEntity< List<AppointmentAudit>> getAppointmentAudit(@PathVariable Long appointmentId) {
        List<AppointmentAudit> appointAudit = bookingService.getAppointAudit(appointmentId);
        return ResponseEntity.ok(appointAudit);
    }


}

