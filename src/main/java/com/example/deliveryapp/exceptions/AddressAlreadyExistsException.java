package com.example.deliveryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressAlreadyExistsException extends Exception{

    public AddressAlreadyExistsException(){
        super("Already exists this address in the db");
    }
}
