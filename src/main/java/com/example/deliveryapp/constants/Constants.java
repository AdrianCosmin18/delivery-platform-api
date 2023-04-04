package com.example.deliveryapp.constants;

import lombok.Data;

public enum Constants {

    ADDRESS_NOT_FOUND_EXCEPTION("There is no address like this in db"),
    CITY_NOT_FOUND_EXCEPTION("There is no city with this name in db"),
    CITY_ALREADY_EXITS_EXCEPTION("Already exists this city in db"),
    PHONE_COURIER_ALREADY_EXISTS_EXCEPTION("Already exists a courier with this phone number"),
    RESTAURANT_NAME_ALREADY_EXISTS_EXCEPTION("Already exists this restaurant in the db"),
    RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION("There is no restaurant in the db with this name"),
    RESTAURANT_ALREADY_EXISTS_IN_CITY_EXCEPTION("Already exists this restaurant in this city"),
    RESTAURANT_NOT_PRESENT_IN_CITY_EXCEPTION("This restaurant isn't present in this city"),
    PRODUCT_ALREADY_EXISTS_IN_RESTAURANT_EXCEPTION("Already exists this product in this restaurant"),
    PRODUCT_NOT_FOUND_BY_RESTAURANT_AND_NAME("There is no product from this restaurant with this name"),
    USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION("Already exists a user with this email"),
    USER_ALREADY_EXISTS_PHONE_EXCEPTION("This phone number belongs to someone else"),
    WISHLIST_EXIST_EXCEPTION("User already has this restaurant in his wishlist"),
    WISHLIST_NOT_EXIST_EXCEPTION("User doesn't have this restaurant in his wishlist"),
    USER_ALREADY_OWN_ADDRESS_EXCEPTION("User already has this address"),
    USER_NOT_OWN_ADDRESS_EXCEPTION("User does not have this address"),
    USER_CARD_ALREADY_EXISTS_EXCEPTION("Already added this card"),
    USER_CARD_NOT_OWN_EXCEPTION("User does not own this card"),
    CARD_NOT_FOUND_BY_NUMBER_EXCEPTION("No card in db with this number");


    private final String message;

    public String getMessage() {
        return message;
    }
    Constants(String message) {
        this.message = message;
    }
}
