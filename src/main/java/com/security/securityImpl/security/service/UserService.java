package com.security.securityImpl.security.service;

import com.security.securityImpl.security.dto.AuthResponse;
import com.security.securityImpl.security.request.PhoneNumberRequest;
import com.security.securityImpl.security.request.RegisterUserRequest;


public interface UserService {

    AuthResponse registerWithEmail(final RegisterUserRequest registerUserRequest);

    AuthResponse registerWithPhoneNumber(final PhoneNumberRequest phoneNumberRequest);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgotPassword(String email);

    String resetPassword(String email, String oldPassword, String newPassword);

//    AuthResponse registerWithEmail(final RegisterUserRequest registerUserRequest);

//    AuthResponse registerWithPhoneNumber(final PhoneNumberRequest phoneNumberRequest);
//
//    SendEmailResponse sendingResetPasswordRequestToGivenEmailAddress(final EmailVerificationRequest emailVerificationRequest, final HttpServletRequest request);
//
//    BasicRestResponse resetPasswordAfterVerification(final ResetPasswordRequest resetPasswordRequest, final String resetPasswordToken);
//
//    ResponseEntity<BasicRestResponse> updateUserProfile(final UserRequestDto userRequestDto, final Integer id);
//
//    ResponseEntity<BasicRestResponse> fetchUserProfileById(final Integer id);

}

