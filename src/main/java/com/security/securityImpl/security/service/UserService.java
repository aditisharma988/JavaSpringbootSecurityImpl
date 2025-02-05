package com.security.securityImpl.security.service;

import com.security.securityImpl.security.dto.LoginDto;
import com.security.securityImpl.security.dto.RegisterDto;
import com.security.securityImpl.security.model.Users;
import com.security.securityImpl.security.repository.UserRepository;
import com.security.securityImpl.security.util.EmailUtil;
import com.security.securityImpl.security.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;
    //
    @Autowired
    AuthenticationManager authenticationManager;

//    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    //    @Transactional
    public String register(RegisterDto registerDto) {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }

        Users users = new Users();
        users.setUsername(registerDto.getUsername());
        users.setEmail(registerDto.getEmail());
        users.setOtp(otp);
        users.setOtpGeneratedTime(LocalDateTime.now());
        users.setPassword(encodedPassword);
//        users.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        System.out.println("Saving user: " + users);

        Users savedUser = userRepository.save(users);
        System.out.println("User saved successfully: " + savedUser);

        return "User registration successful";

//        userRepository.save(users);
//        return "User registration successfull";

//            return null;
    }


//
//    public Users register(Users users){
//
//        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
//         return userRepository.save(users);
//
//    }


//    public String login(LoginDto loginDto) {
//        Users users = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found with this email address." + loginDto.getEmail()));
//        if (!loginDto.getPassword().equals(users.getPassword())) {
//            return "Password is not correct";
//        } else if (!users.isActive()) {
//            return "Your account is not verified";
//        }
//        return "Login successfull";
//    }

    public String login(LoginDto loginDto) {

        Users users = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found with this email address: " + loginDto.getEmail()));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginDto.getPassword(), users.getPassword())) {
            return "Password is not correct";
        }

        if (!users.isActive()) {
            return "Your account is not verified";
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(), loginDto.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getEmail());
        }

        return "Authentication failed";
    }

//    public String login(LoginDto loginDto) {
//        // Fetch user details from DB
//        Users users = userRepository.findByEmail(loginDto.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found with this email address: " + loginDto.getEmail()));
//
//        // Check password
//        if (!loginDto.getPassword().equals(users.getPassword())) {
//            return "Password is not correct";
//        }
//
//        // Check if the account is verified
//        if (!users.isActive()) {
//            return "Your account is not verified";
//        }
//
//        // Authenticate user using Spring Security
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(users.getEmail(), loginDto.getPassword()));
//
//        // Generate JWT token
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(users.getEmail());
//        }
//
//        return "Authentication failed";
//    }


//
//    public String login(Users users) {
//        Authentication authentication=
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
//       if( authentication.isAuthenticated())
//        return jwtService.generateToken(users.getUsername());
//
//       return "fail";
//    }

    public String verifyAccount(String email, String otp) {

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email address." + email));

        if (users.getOtp().equals(otp) && Duration.between(users.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {

            users.setActive(true);
            userRepository.save(users);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again. ";
    }

    public String regenerateOtp(String email) {

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email address." + email));
        String otp = otpUtil.generateOtp();


        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        users.setOtp(otp);
        users.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(users);
        return "Email sent ... please verify account  within 1 minute";

    }

    public String forgotPassword(String email) {

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email address: " + email));

        try {
            emailUtil.sendResetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to set reset password mail please try again");
        }

        return "Please check email to reset new password";

    }

    public String resetPassword(String email, String newPassword) {

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email address: " + email));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);


        users.setPassword(encodedPassword);
        userRepository.save(users);
        return "New password set successfully login with new password.";

    }
}
