package com.example.deliveryapp.constants;

public enum FoodType {

    BURGER("burger"),
    FRIES("fires"),
    DRINK("drink"),
    DESERT("desert"),
    EXTRAS("extras");

    private final String type;

    public String getType() {
        return type;
    }

    FoodType(String type) {
        this.type = type;
    }
}
