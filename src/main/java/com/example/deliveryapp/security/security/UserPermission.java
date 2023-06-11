package com.example.deliveryapp.security.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum UserPermission {

    ADD_PRODUCT_TO_WISHLIST("product:add to wishlist"),
    DELETE_FROM_WISHLIST("product:delete from wishlist"),
    ADD_ADDRESS("address:add"),
    DELETE_ADDRESS("address:delete"),
    ADD_CARD("card:add"),
    REMOVE_CARD("card:remove"),
    PLACE_ORDER("order:add"),
    CONFIRM_PAYMENT("order:confirm payment"),
    PREPARE_ORDER("order:order in preparation"),
    DELIVER_ORDER("order:order in delivery"),
    CANCEL_ORDER("order:cancel the order"),
    DELIVERED_TIME("order:delivered");

    private String permission;
    public String getPermission(){ return permission; }
}
