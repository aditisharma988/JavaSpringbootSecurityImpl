package com.security.securityImpl.security.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberRequest {

    @NotNull(message = "Phone Number cannot be blank")
    @Column(name = "phone_number",nullable = false, unique = true)
    private String phoneNumber;

}