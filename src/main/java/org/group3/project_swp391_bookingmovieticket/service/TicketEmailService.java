package org.group3.project_swp391_bookingmovieticket.service;

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
        message.setSubject("Email Verification Code ");
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
                                 String orderCode,
                                 byte[] qrBytes) throws MessagingException {
        System.out.println(listPopcornAndDrinkName + "send email");

        String htmlContent = """
                <h2>🎬 Cảm ơn, %s, đã sử dụng dịch vụ của chúng tôi!</h2>
                <p>Dưới đây là thông tin chi tiết:</p>
                <table style="border-collapse: collapse;">
                    <tr><td><strong>🎥 Phim:</strong></td><td>%s</td></tr>
                    <tr><td><strong>📅 Ngày:</strong></td><td>%s</td></tr>
                    <tr><td><strong>⏰ Thời gian:</strong></td><td>%s</td></tr>
                    <tr><td><strong>🏢 Rạp:</strong></td><td>%s</td></tr>
                    <tr><td><strong>💺 Ghế:</strong></td><td>%s</td></tr>
                    <tr><td><strong>🎞️ Định dạng:</strong></td><td>%s</td></tr>
                    <tr><td><strong> Mã đặt vé:</strong></td><td>%s</td></tr>
                     <tr><td><strong>🍿🍹 Bỏng và nước:</strong></td><td>%s</td></tr>
                </table>
                <p style="margin-top: 15px;">🎟️ Vé của bạn mã QR được đính kèm. Vui lòng xuất trình nó tại lối vào rạp chiếu phim.</p>
                <p style="color: gray;">Chúc bạn có trải nghiệm xem phim thú vị!</p>
                """.formatted(customerName, movieName, showDate, showTime, branchName, seatName, movieFormat,orderCode, listPopcornAndDrinkName);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart=true

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // set HTML body
        helper.addAttachment("ticket_qr.png", new ByteArrayResource(qrBytes));

        mailSender.send(message);
    }
}
