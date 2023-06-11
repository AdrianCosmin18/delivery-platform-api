package com.example.deliveryapp.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

@RestController
@RequestMapping("delivery-app")
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;
    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

//    @PostMapping("/send-email-attach")
//    public ResponseEntity sendEmailAttach(@RequestBody EmailMessage emailMessage, @RequestParam(value = "file") String file) {
//        try{
//            this.emailSenderService.sendEmailWithAttach(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage(), file);
//            return ResponseEntity.ok("Success");
//        }catch (MessagingException e){
//            return ResponseEntity.ok("False");
//        }
//
//    }
}
