package com.cashrich.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountUserProfilePasswordDto {

    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @NonNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")
    private String password;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10}$")
    private String mobile;

}
