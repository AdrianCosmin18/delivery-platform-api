package com.example.deliveryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsertPictureException extends IOException {

    public InsertPictureException(String msg){
        super(msg);
    }
}
