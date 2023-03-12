package com.example.deliveryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeliveryCustomException extends RuntimeException{
    public DeliveryCustomException(String msg){
        super(msg);
    }
}
