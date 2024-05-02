package com.example.assignment.Model.OpenSlotResponse;

import java.util.List;
import java.util.Map;

public class OpenSlotResponseUnit {
    String  day;
    Map<String,String> slots;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Map<String, String> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, String> slots) {
        this.slots = slots;
    }
}
