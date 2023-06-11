package com.example.deliveryapp.email;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailSenderServiceImpl implements EmailSenderService{

    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("burgeshop1@gmail.com");
        simpleMailMessage.setTo(to); //catre cine trimitem
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        this.mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendEmailWithAttach(String to, String subject, String message, String file) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("burgeshop1@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);

        FileSystemResource fileSystemResource = new FileSystemResource(new File(file));

        mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

        mailSender.send(mimeMessage);
    }
}
