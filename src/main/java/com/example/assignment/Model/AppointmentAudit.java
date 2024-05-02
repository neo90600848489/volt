package com.example.assignment.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointments_audit")
public class AppointmentAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(name="appointment_id")
    private Long  appointmentId;

    @Column(name = "appointment_status")
    private String  appointMentStatus;

    @Column(name = "created_time")
    private Date createdTime;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointMentStatus() {
        return appointMentStatus;
    }

    public void setAppointMentStatus(String appointMentStatus) {
        this.appointMentStatus = appointMentStatus;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
