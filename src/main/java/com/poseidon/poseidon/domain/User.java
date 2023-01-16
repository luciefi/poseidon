package com.poseidon.poseidon.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Pattern(regexp = "^$|^.*(?=.{8,125})(?=.*[A-Z])(?=.*\\d)(?=.*[!&$%&? \"]).*$", message = "Password must be between 8 and 125 characters, and " +
            "contain at least one capital letter, one number and one special character.")
    private String password;
    @NotBlank(message = "FullName is mandatory")
    private String fullname;
    @NotNull(message = "Role is mandatory")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String principal;

    private String registrationId;
}
