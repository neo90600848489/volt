package com.example.assignment.Repository;

import com.example.assignment.Model.Appointment;
import com.example.assignment.Model.AppointmentAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Repository
public interface AppointmentAuditRepository extends JpaRepository<AppointmentAudit, Long> {
    @Query(value = "SELECT * FROM appointments_audit WHERE appointment_id =:appointmentId order by created_time", nativeQuery = true)
    List<AppointmentAudit> findByOperator(Long appointmentId);
}
