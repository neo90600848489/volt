package com.example.assignment.Repository;

import com.example.assignment.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // You can define additional methods here if needed


    @Query(value = "SELECT start_time FROM appointments WHERE operator_id = :operatorId and booking_status = :bookingStatus and day = :day", nativeQuery = true)
    List<Integer> findAllStartTimeForDate(int operatorId, int bookingStatus, int day);


//    @Query(value = "SELECT * from  appointments WHERE operator_id = :operator and booking_status = :booking_status", nativeQuery = true)
//    List<Appointment> showOpenSLot(int operator,int booking_status);

    @Query(value = "SELECT * FROM appointments WHERE operator_id = :operatorId and booking_status = :booking_status", nativeQuery = true)
    List<Appointment> bookedSlot(int operatorId,int booking_status);

    @Modifying
    @Query(value = "UPDATE appointments SET booking_status = :status WHERE appointment_id = :id", nativeQuery = true)
    public void updateBookingStatus(@Param("id") Long appointmentId, @Param("status") int status);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM appointments WHERE appointment_id = :appointmentId) THEN TRUE ELSE FALSE END", nativeQuery = true)
    boolean isAppointmentExist(Long appointmentId);

    @Modifying
    @Query(value = "UPDATE appointments SET booking_status = :status, day = :day, operator_id = :operatorId,start_time = :startTime WHERE appointment_id = :appointmentId", nativeQuery = true)
    int  updateBookingStatus(@Param("appointmentId") Long appointmentId, @Param("status") int status, @Param("day") int day, @Param("operatorId") int operatorId,@Param("startTime") int startTime);



}
