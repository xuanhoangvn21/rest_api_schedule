package com.timetable.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.timetable.service.SendEmailService;

import java.io.IOException;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendEmail(String email, String subject, String body) throws  MessagingException {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
           helper.setFrom("Hoangxuanit");
           helper.setTo(email);
           helper.setSubject(subject);
           helper.setText(body,true);
           mailSender.send(message);
           System.out.println("Success");
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
}
