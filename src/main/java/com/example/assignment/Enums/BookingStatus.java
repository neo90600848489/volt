package com.example.assignment.Enums;

public enum BookingStatus {
        CONFIRMED(1, "Confirmed"),
        RESCHEDULED(2, "Rescheduled"),
        CANCELLED(3, "Cancelled");
        private final int id;
        private final String text;
         BookingStatus(int id, String text) {
            this.id = id;
            this.text = text;
        }
        public int getId() {
            return id;
        }
        public String getText() {
            return text;
        }
    public static String getNameById(int id) {
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            if (bookingStatus.id == id) {
                return bookingStatus.text;
            }
        }
        return null; // or throw exception
    }
    }
