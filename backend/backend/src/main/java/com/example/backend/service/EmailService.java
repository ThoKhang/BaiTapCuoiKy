package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service  // Đánh dấu là Spring bean
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;  // Inject từ Spring config
    @Autowired
    private OTPService otpService;
    public void sendOTP(String to, String username)
    {
        String otp = otpService.generateOTP(to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("khangheheqt1234@gmail.com");
        message.setSubject("Mã OTP xác thực của bạn");
        message.setText("Chào " + username + "!\n\n" +
                "Mã OTP của bạn là: " + otp + "\n" +
                "Mã này hết hạn sau 5 phút. Vui lòng không chia sẻ cho bất kỳ ai!\n\n" +
                "Trân trọng,\nTeam 5ae: App Hoc Tập Cho Trẻ");
        mailSender.send(message);
    }
    // Phương thức gửi email đơn giản (text)
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);  // Email người nhận (user.getEmail())
        message.setFrom("khangheheqt1234@gmail.com");  // Email gửi (từ config)
        message.setSubject(subject);  // Tiêu đề
        message.setText(body);  // Nội dung
        mailSender.send(message);
    }

    // Phương thức gửi HTML email (nâng cao, tùy chọn)
    // Nếu cần HTML, dùng MimeMessageHelper thay SimpleMailMessage
}
