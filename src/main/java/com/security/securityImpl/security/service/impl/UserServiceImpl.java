package com.security.securityImpl.security.service.impl;

import com.security.securityImpl.security.dto.AuthResponse;
import com.security.securityImpl.security.entity.Users;
import com.security.securityImpl.security.mapper.UsersMapper;
import com.security.securityImpl.security.repository.UserRepository;
import com.security.securityImpl.security.request.PhoneNumberRequest;
import com.security.securityImpl.security.request.RegisterUserRequest;
import com.security.securityImpl.security.service.AuthenticationService;
import com.security.securityImpl.security.service.JWTService;
import com.security.securityImpl.security.service.UserService;
import com.security.securityImpl.security.util.EmailUtil;
import com.security.securityImpl.security.util.OtpUtil;
import com.security.securityImpl.security.util.PasswordUtil;
import io.jsonwebtoken.io.Decoders;
import jakarta.mail.MessagingException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;
    //
    @Autowired
    AuthenticationManager authenticationManager;

    public AuthResponse registerWithEmail(RegisterUserRequest registerUserRequest) {

        AuthResponse authenticationResponse = AuthResponse.builder().build();
        List<String> errorList = new ArrayList<>();

        try {

            Optional<Users> optionalUser = userRepository.findByEmail(registerUserRequest.getEmail());
            if (optionalUser.isPresent()) {
                throw new RuntimeException("USER_PRESENT_EMAIL_ADDRESS_ERROR");
            }

            byte[] keyBytesForPassword = Decoders.BASE64.decode(registerUserRequest.getPassword());
            String password = new String(keyBytesForPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(password)) errorList.add("FIELD_FORMAT_ERROR");

            byte[] keyBytesForConfirmPassword = Decoders.BASE64.decode(registerUserRequest.getConfirmPassword());
            String confirmPassword = new String(keyBytesForConfirmPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(confirmPassword)) errorList.add("FIELD_FORMAT_ERROR");

            if (password.isBlank() || confirmPassword.isBlank()) errorList.add("FIELD_BLANK");
            if (!password.equals(confirmPassword)) errorList.add("FIELDS_NOT_EQUAL_ERROR");

            if (!errorList.isEmpty()) throw new ValidationException("Error occurred");

            String otp = otpUtil.generateOtp();

            try {
                emailUtil.sendOtpEmail(registerUserRequest.getEmail(), otp);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send OTP, please try again.");
            }



            Users user = Users.builder().username(registerUserRequest.getUsername()).email(registerUserRequest.getEmail()).phoneNumber(registerUserRequest.getPhoneNumber()).password(passwordEncoder.encode(registerUserRequest.getPassword())).otp(otp).otpGeneratedTime(LocalDateTime.now()).build();

            Users savedUser = userRepository.save(user);


            authenticationResponse = authenticationService.generateAuthenticationResponse(savedUser.getEmail(), AuthResponse.builder().build());

            authenticationResponse.setStatus(HttpStatus.OK.value());
            authenticationResponse.setMessage("USER_SAVED_SUCCESSFULLY");

        } catch (Exception ex) {
            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            authenticationResponse.setMessage(ex.getLocalizedMessage());
        }

        return authenticationResponse;
    }


    @Override
    public AuthResponse registerWithPhoneNumber(PhoneNumberRequest phoneNumberRequest) {

        AuthResponse authResponse = AuthResponse.builder().build();
        try {

            Optional<Users> optionalUsers = userRepository.findByPhoneNumber(phoneNumberRequest.getPhoneNumber());
            if (optionalUsers.isPresent()) {
                throw new RuntimeException("User already present");
            }

            final String password = PasswordUtil.getPassword(10);
            log.info("Generated Password: {}", password);

            String otp = otpUtil.generateOtp();

            final Users users = Users.builder()

//                    .username(username)
                    .password(passwordEncoder.encode(password)).phoneNumber(phoneNumberRequest.getPhoneNumber()).otp(otp).otpGeneratedTime(LocalDateTime.now()).build();
            userRepository.save(users);


            authResponse = authenticationService.generateAuthenticationResponseForPhoneNumber(users.getPhoneNumber(), AuthResponse.builder().build());

            authResponse.setStatus(HttpStatus.OK.value());
            authResponse.setMessage("USER_SAVED_SUCCESSFULLY");
        } catch (Exception ex) {
            authResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            authResponse.setMessage(ex.getLocalizedMessage());
        }
        return authResponse;
    }


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

        //random temp pass
        String tempPassword = generateRandomPassword(10);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedTempPassword = passwordEncoder.encode(tempPassword);

        users.setPassword(encodedTempPassword);
        userRepository.save(users);


        try {
            emailUtil.sendResetPasswordEmail(email, tempPassword);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to set reset password mail please try again");
        }

        return " A temporary password has been sent on mail. Please use it to login or reset new password.";

    }

    private String generateRandomPassword(int length) {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyz0123456789@#$%!&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    public String resetPassword(String email, String oldPassword, String newPassword) {

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email address: " + email));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(oldPassword, users.getPassword())) {
            return "Temporary password is incorrect.";
        }

        String encodedPassword = passwordEncoder.encode(newPassword);


        users.setPassword(encodedPassword);
        userRepository.save(users);
        return "New password has been successfully updated.You can now login with new password.";

    }
}

//    public AuthResponse registerWithPhoneNumber(final PhoneNumberRequest phoneNumberRequest){
//
//    }


//    //    @Transactional
//    public AuthResponse registerWithEmail(RegisterUserRequest registerUserRequest) {
//
//        AuthResponse authenticationResponse = AuthResponse.builder().build();
//        List<String> errorList = new ArrayList<>();
//        try {
//
////            final Optional<User> optionalUser1 = userRepository.findByPhoneNumber(registerUserRequest.getPhoneNumber());
////            if (optionalUser1.isPresent())
////                throw new UserAlreadyPresentException(errorMessageSource.getMessage(ErrorConstants.USER_PRESENT_PHONE_NUMBER_ERROR, null, LocaleUtil.getLocale()));
//
//            final Optional<Users> optionalUser = userRepository.findByEmail(registerUserRequest.getEmail());
//            if (optionalUser.isPresent())
//                throw new RuntimeException("USER_PRESENT_EMAIL_ADDRESS_ERROR", null);
//
//
//            byte[] keyBytesForPassword = Decoders.BASE64.decode(registerUserRequest.getPassword());
//            String password = new String(keyBytesForPassword, StandardCharsets.UTF_8);
//            if (PasswordUtil.isNotInPasswordFormat(password))
//                errorList.add("FIELD_FORMAT_ERROR");
//
//            byte[] keyBytesForConfirmPassword = Decoders.BASE64.decode(registerUserRequest.getConfirmPassword());
//            String confirmPassword = new String(keyBytesForConfirmPassword, StandardCharsets.UTF_8);
//            if (PasswordUtil.isNotInPasswordFormat(confirmPassword))
//                errorList.add("FIELD_FORMAT_ERROR");
//
//            if (password.isBlank())
//                errorList.add("FIELD_BLANK");
//            if (confirmPassword.isBlank())
//                errorList.add("FIELD_BLANK");
//
//            if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword()))
//                errorList.add("FIELDS_NOT_EQUAL_ERROR");
//
//            if (!errorList.isEmpty()) throw new ValidationException("Error occurred");
//
//
//            String otp = otpUtil.generateOtp();
//        try {
//            emailUtil.sendOtpEmail(registerUserRequest.getEmail(), otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }
//
//            final Users user = Users
//                    .builder()
//                    .username(registerUserRequest.getEmail())
//                    .email(registerUserRequest.getEmail())
//                    .username(registerUserRequest.getUsername())
//                    .otp(registerUserRequest.getOtp())
////                    .role(Role.USER)
//                    .phoneNumber(registerUserRequest.getPhoneNumber())
//                    .password(passwordEncoder.encode(password))
//                    .build();
//
//
//            Users users = new Users();
//
//        users.setOtp(otp);
//        users.setOtpGeneratedTime(LocalDateTime.now());
//
//            final Users savedUser = userRepository.save(user);
//
//            authenticationResponse = authenticationService.generateAuthenticationResponse(savedUser.getUsername(), AuthResponse.builder().build());
//
//            authenticationResponse.setStatus(HttpStatus.OK.value());
//            authenticationResponse.setMessage("USER_SAVED_SUCCESSFULLY");
//        } catch (Exception ex) {
//            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            authenticationResponse.setMessage(ex.getLocalizedMessage());
////            authenticationResponse.setErrorList(errorList);
//        }
//        return authenticationResponse;
//
//
//    }


//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());

//        String otp = otpUtil.generateOtp();
//        try {
//            emailUtil.sendOtpEmail(registerUserRequest.getEmail(), otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }
//
//        Users users = new Users();
//        users.setUsername(registerUserRequest.getUsername());
//        users.setEmail(registerUserRequest.getEmail());
//        users.setOtp(otp);
//        users.setOtpGeneratedTime(LocalDateTime.now());
//        users.setPassword(encodedPassword);
////        users.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        System.out.println("Saving user: " + users);
//
//        Users savedUser = userRepository.save(users);
//        System.out.println("User saved successfully: " + savedUser);
////
////        return "User registration successful";
//        return ;
//
////        userRepository.save(users);
////        return "User registration successfull";
//
////            return null;
//    }

