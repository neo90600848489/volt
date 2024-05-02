package com.example.assignment.Enums;

public enum ServiceOperators {
    OPERATOR_ONE(1, "Operator One"),
    OPERATOR_TWO(2, "Operator Two"),
    OPERATOR_THREE(3, "Operator Three");

    private final int id;
    private final String text;

    ServiceOperators(int id, String text) {
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
