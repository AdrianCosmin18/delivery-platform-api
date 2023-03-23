package com.example.deliveryapp.constants;

public enum StatusAddress {

    ADD("Added"),
    ALREADY_EXISTS("Already exists in db"),
    ERROR("Something went wrong");


    StatusAddress(String mess) {}

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
