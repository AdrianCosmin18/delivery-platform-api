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
            "/delivery-app/user/login",
            "/delivery-app/user/register",
            "/delivery-app/restaurant/get-restaurant-products/*",
            "/delivery-app/restaurant/get-image-product/*",
            "/delivery-app/restaurant/get-products-by-ingredients",
            "/delivery-app/user/get-user/*",
            "/delivery-app/restaurant/get-product-by-restaurant-and-product-Name/*",
            "/delivery-app/restaurant/add-product",
            "/delivery-app/city",
            "/delivery-app/restaurant/get-product-photo",
            "/delivery-app/user/make-user-as-admin/*",
            "/delivery-app/courier",
            "/delivery-app/send-email",
            "/delivery-app/send-email-attach",
            "/api/v1/students/login",
            "/api/v1/students/register",
            "/swagger-ui.html",
            "/swagger-ui/**","/v3/**",
            "/**"
    };
//    public static final String[] PUBLIC_URLS = { "/api/v1/students/login", "/api/v1/students/register","/swagger-ui.html","/swagger-ui/**","/v3/**",
//    ;

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
}
