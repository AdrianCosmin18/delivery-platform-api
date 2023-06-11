package com.example.deliveryapp.email;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

public interface EmailSenderService {

    void sendEmail(String to, String subject, String message);

    void sendEmailWithAttach(String to, String subject, String message, String file) throws MessagingException;
}
