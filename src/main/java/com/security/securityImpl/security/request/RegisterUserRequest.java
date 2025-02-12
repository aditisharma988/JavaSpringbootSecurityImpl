package com.security.securityImpl.security.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotNull(message = "Name cannot be blank")
    private String username;

    @NotNull(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotNull(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Confirm Password cannot be blank")
    private String confirmPassword;


}
