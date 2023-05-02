package com.example.deliveryapp.security.utils;

public class Util {

    public static final String MY_CODE = "Cosmin";
    public static final String  ADMINISTRATION = "Spring Security App";
    public static final String AUTHORITIES = "authorities";
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String[] PUBLIC_URLS = {
            "/user/login",
            "/user/register",
            "/delivery-app/restaurant/get-restaurant-products/*",
            "/delivery-app/user/get-user/*",
            "/delivery-app/restaurant/get-product-by-restaurant-and-product-Name/*",
            "/delivery-app/restaurant/add-product"
    }; //trebuie modificat aici cum va fi in aplicatia mea
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
}
