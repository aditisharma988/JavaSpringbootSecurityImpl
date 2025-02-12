package com.security.securityImpl.security.controller;

import com.security.securityImpl.security.dto.AuthRequest;
import com.security.securityImpl.security.dto.AuthResponse;
import com.security.securityImpl.security.request.PhoneNumberRequest;
import com.security.securityImpl.security.request.RegisterUserRequest;
import com.security.securityImpl.security.service.AuthenticationService;
import com.security.securityImpl.security.service.JWTService;
import com.security.securityImpl.security.service.UserService;
import io.jsonwebtoken.io.Decoders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin
@Tag(name = "User Controller", description = "API's for user security")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

//    @Autowired
//    private AuthenticationController authenticationController;


    @Operation(
            summary = "API to register user USING EMAIL.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")},
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error",
                            content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")},
                            responseCode = "400"
                    )
            }
    )
    @PostMapping(value = "/register", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthResponse> registerUser(
            @Parameter(description = "Save user request", schema = @Schema(implementation = RegisterUserRequest.class)) final @Valid @RequestBody RegisterUserRequest registerUserRequest
    ) {
        {
            AuthResponse response = userService.registerWithEmail(registerUserRequest);
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping(value = "/registerWithPhoneNumber", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthResponse> registerWithPhoneNumber(
            @Parameter(description = "Phone number Request", schema = @Schema(implementation = PhoneNumberRequest.class))
            @Valid @RequestBody final PhoneNumberRequest phoneNumberRequest) {
        AuthResponse response = userService.registerWithPhoneNumber(phoneNumberRequest);

        if (response.equals(HttpStatus.OK.value())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }


    @PostMapping(value = "/login", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthResponse> loginUsingEmail(
            @Parameter(description = "Username and Password", schema = @Schema(implementation = AuthRequest.class)) final @Valid @RequestBody AuthRequest authRequest
    ) {
        AuthResponse authenticationResponse = AuthResponse.builder().build();
        try {
            byte[] keyBytes = Decoders.BASE64.decode(authRequest.getPassword());

            authRequest.setPassword(new String(keyBytes, StandardCharsets.UTF_8));

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            authenticationResponse = authenticationService.generateAuthenticationResponse(authRequest.getEmail(), authenticationResponse);
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

        } catch (Exception e) {
            authenticationResponse.setMessage(e.getLocalizedMessage());
            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/loginWithPhoneNumber", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthResponse> loginUsingPhoneNumber(
            @Parameter(description = "PhoneNumber and Password", schema = @Schema(implementation = PhoneNumberRequest.class)) final @Valid @RequestBody PhoneNumberRequest phoneNumberRequest
    ) {
        AuthResponse authenticationResponse = AuthResponse.builder().build();
        try {

            authenticationResponse = authenticationService.generateAuthenticationResponseForPhoneNumber(phoneNumberRequest.getPhoneNumber(), authenticationResponse);

            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

        } catch (Exception e) {
            authenticationResponse.setMessage(e.getLocalizedMessage());
            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestHeader String newPassword, @RequestHeader String oldPassword) {
        return new ResponseEntity<>(userService.resetPassword(email, oldPassword, newPassword), HttpStatus.OK);
    }
}
