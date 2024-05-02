package com.example.assignment.Exception;

public class BusinessException extends Exception {
    private int id;

    public BusinessException(int id, String message) {
        super(message);
        this.id = id;
    }
    public BusinessException(String message) {
        super(message);
    }


    public int getId() {
        return id;
    }
}
