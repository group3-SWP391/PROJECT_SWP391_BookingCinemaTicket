package org.group3.project_swp391_bookingmovieticket.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   @Autowired
   private JavaMailSender mailSender;

   public void sendEmail(String to, String subject, String body) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);
      message.setFrom("sonledz22cm@gmail.com");
      mailSender.send(message);
   }
}


