package com.cashrich.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="account_user")
public class AccountUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_user_seq")
    @SequenceGenerator(name = "account_user_seq", sequenceName = "account_user_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String mobile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_user_profile_id")
    private AccountUserProfileEntity accountUserProfile;

}
