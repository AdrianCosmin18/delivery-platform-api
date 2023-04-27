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
    PLACE_ORDER("order:add");

    private String permission;
    public String getPermission(){ return permission; }
}
