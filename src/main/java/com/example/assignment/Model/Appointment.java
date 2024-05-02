package com.example.assignment.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  appointmentId;

    @Column(name = "user_id")
    private int  userId;

    @Column(name = "operator_id")
    private int  operatorId;

    @Column(name="day")
    private int day;

    @Column(name = "start_time")
    private int  startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "booking_status")
    private int bookingStatus;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int  getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int  getOperatorId() {
        return operatorId;
    }

    public void  setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int  getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int  getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int  bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}

