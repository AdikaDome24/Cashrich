package com.cashrich.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="account_user_profile")
public class AccountUserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_user_profile_seq")
    @SequenceGenerator(name = "account_user_profile_seq", sequenceName = "account_user_profile_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 15)
    private String username;

    @Column(nullable = false)
    @Size(min = 8, max = 15)
    private String password;

    @OneToOne(mappedBy = "accountUserProfile")
    private AccountUserEntity accountUser;
}
