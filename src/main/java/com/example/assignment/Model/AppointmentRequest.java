package com.example.assignment.Model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AppointmentRequest {
    @JsonProperty("operatorId")
    int operatorId;

    @JsonProperty("day")
    int day;

    @JsonProperty("start_time")
    int start_time;

    @JsonProperty("multiOperator")
    boolean multiOperator;


    public int getOperatorId() {
        return operatorId;
    }

    public int getDay() {
        return day;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public boolean isMultiOperator() {
        return multiOperator;
    }
}


