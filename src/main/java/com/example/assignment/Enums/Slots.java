package com.example.assignment.Enums;

    public enum Slots {
        FIRST_SLOT(1, "First Slot"),
        SECOND_SLOT(2, "Second Slot"),
        THIRD_SLOT(3, "Third Slot"),
        FOURTH_SLOT(4, "Fourth Slot"),
        FIFTH_SLOT(5, "Fifth Slot"),
        SIXTH_SLOT(6, "Sixth Slot"),
        SEVENTH_SLOT(7, "Seventh Slot"),
        EIGHTH_SLOT(8, "Eighth Slot"),
        NINTH_SLOT(9, "Ninth Slot"),
        TENTH_SLOT(10, "Tenth Slot"),
        ELEVENTH_SLOT(11, "Eleventh Slot"),
        TWELFTH_SLOT(12, "Twelfth Slot"),
        THIRTEENTH_SLOT(13, "Thirteenth Slot"),
        FOURTEENTH_SLOT(14, "Fourteenth Slot"),
        FIFTEENTH_SLOT(15, "Fifteenth Slot"),
        SIXTEENTH_SLOT(16, "Sixteenth Slot"),
        SEVENTEENTH_SLOT(17, "Seventeenth Slot"),
        EIGHTEENTH_SLOT(18, "Eighteenth Slot"),
        NINETEENTH_SLOT(19, "Nineteenth Slot"),
        TWENTIETH_SLOT(20, "Twentieth Slot"),
        TWENTY_FIRST_SLOT(21, "Twenty-First Slot"),
        TWENTY_SECOND_SLOT(22, "Twenty-Second Slot"),
        TWENTY_THIRD_SLOT(23, "Twenty-Third Slot"),
        TWENTY_FOURTH_SLOT(24, "Twenty-Fourth Slot");

        private final int id;
        private final String name;

        Slots(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static String getNameById(int id) {
            for (Slots slot : Slots.values()) {
                if (slot.id == id) {
                    return slot.name;
                }
            }
            return null; // or throw exception
        }
    }
