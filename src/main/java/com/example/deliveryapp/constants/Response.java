package com.example.deliveryapp.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String message;
    private HttpStatus status;
}
