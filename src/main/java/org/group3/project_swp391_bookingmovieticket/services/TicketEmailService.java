package org.group3.project_swp391_bookingmovieticket.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TicketEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public String generateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }

    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Email Verification Code Register");
        message.setText("Your verification code is: " + code + "The verification code only exists for 5 minutes.");

        mailSender.send(message);
    }


    public void sendTicketWithQr(String toEmail,
                                 String subject,
                                 String customerName,
                                 String movieName,
                                 String showDate,
                                 String showTime,
                                 String branchName,
                                 String seatName,
                                 String movieFormat,
                                 String listPopcornAndDrinkName,
                                 byte[] qrBytes) throws MessagingException {
        System.out.println(listPopcornAndDrinkName + "send email");

        String htmlContent = """
                <h2>🎬 Thank you, %s, for booking your ticket with us!</h2>
                <p>Here are your ticket details:</p>
                <table style="border-collapse: collapse;">
                    <tr><td><strong>🎥 Movie:</strong></td><td>%s</td></tr>
                    <tr><td><strong>📅 Date:</strong></td><td>%s</td></tr>
                    <tr><td><strong>⏰ Time:</strong></td><td>%s</td></tr>
                    <tr><td><strong>🏢 Cinema:</strong></td><td>%s</td></tr>
                    <tr><td><strong>💺 Seat:</strong></td><td>%s</td></tr>
                    <tr><td><strong>🎞️ Format:</strong></td><td>%s</td></tr>
                     <tr><td><strong>🍿🍹 Popcorn & Drinks:</strong></td><td>%s</td></tr>
                </table>
                <p style="margin-top: 15px;">🎟️ Your QR code ticket is attached. Please present it at the cinema entrance.</p>
                <p style="color: gray;">Enjoy your movie experience!</p>
                """.formatted(customerName, movieName, showDate, showTime, branchName, seatName, movieFormat, listPopcornAndDrinkName);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart=true

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // set HTML body
        helper.addAttachment("ticket_qr.png", new ByteArrayResource(qrBytes));

        mailSender.send(message);
    }
}
