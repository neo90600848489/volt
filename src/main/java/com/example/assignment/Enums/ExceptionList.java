package com.example.assignment.Enums;

public enum ExceptionList {
    PREVIOUSLY_BOOKED(1, "Booking is Already happened at this time"),
    INVALID_REQUEST(2,"please pass right paramater as per constraints"),
    INVALID_APPOINTMENT(3,"Invalid Appointment"),
    OPERATOR_ENGAGED(4,"Operator Engaged");
    private final int id;
    private final String text;
    ExceptionList(int id, String text) {
        this.id = id;
        this.text = text;
    }
    public int getId() {
        return id;
    }
    public String getText() {
        return text;
    }
}
