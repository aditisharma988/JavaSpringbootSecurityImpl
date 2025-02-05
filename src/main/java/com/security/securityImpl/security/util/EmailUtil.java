package com.security.securityImpl.security.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {

//        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
//        simpleMailMessage.setTo(email);
//        simpleMailMessage.setSubject("Verify Otp");
//        simpleMailMessage.setText("Hello, your OTP is " + otp);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Otp");
        mimeMessageHelper.setText("""
                <div>
                <a href="http://localhost:8089/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
                </div>
                """.formatted(email, otp), true);

        javaMailSender.send(mimeMessage);

    }


    public void sendResetPasswordEmail(String email) throws MessagingException {

//        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
//        simpleMailMessage.setTo(email);
//        simpleMailMessage.setSubject("Verify Otp");
//        simpleMailMessage.setText("Hello, your OTP is " + otp);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset password");
        mimeMessageHelper.setText("""
                <div>
                <a href="http://localhost:8089/reset-password?email=%s" target="_blank">click link to reset</a>
                </div>
                """.formatted(email), true);

        javaMailSender.send(mimeMessage);
    }

}