package com.cashrich.service;

import com.cashrich.entity.AccountUserEntity;
import com.cashrich.entity.AccountUserProfileEntity;
import com.cashrich.model.*;
import com.cashrich.repository.AccountUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Log4j2
public class AccountUserService {

    @Autowired
    private final AccountUserRepository accountUserRepository;

    public AccountUserService(AccountUserRepository accountUserRepository) {
        this.accountUserRepository = accountUserRepository;
    }


    public AccountUserDto findByUsername(Long userId) {
        log.info("[AccountUserService][findByUsername],Searching for user with ID: {}", userId);

        Optional<AccountUserEntity> accountUserEntity = accountUserRepository.findById(userId);
        if (((Optional<?>) accountUserEntity).isEmpty()) {
            log.debug("User with ID: {} not found", userId);
            return null;
        }
        AccountUserDto accountUser = new AccountUserDto();
        accountUser.setFirstName(accountUserEntity.get().getFirstName());
        accountUser.setLastName(accountUserEntity.get().getLastName());
        accountUser.setMobile(accountUserEntity.get().getMobile());
        accountUser.setEmail(accountUserEntity.get().getEmail());

        AccountUserProfileDto accountUserProfileDto = new AccountUserProfileDto();
        if (accountUserEntity.get().getAccountUserProfile() != null) {
            accountUserProfileDto.setUsername(accountUserEntity.get().getAccountUserProfile().getUsername());
            accountUserProfileDto.setPassword(accountUserEntity.get().getAccountUserProfile().getPassword());
        }
        accountUser.setAccountUserProfileDto(accountUserProfileDto);

        log.info("Returning user details for ID: {}", userId);
        return accountUser;
    }

    public AccountUserResponseDto createUser(AccountUserProfileDto accountUserProfileDto) {
        log.info("[AccountUserService][createUser] create user with profile data: {}", accountUserProfileDto);
        if (accountUserProfileDto == null ||
                accountUserProfileDto.getUsername() == null ||
                accountUserProfileDto.getUsername().trim().isEmpty() ||
                accountUserProfileDto.getPassword() == null ||
                accountUserProfileDto.getPassword().trim().isEmpty()) {
            return new AccountUserResponseDto("Username and password must not be blank", null);
        }
        if (accountUserRepository.existsByAccountUserProfile_Username(accountUserProfileDto.getUsername())) {
            return new AccountUserResponseDto("Username already in use", null);
        }
        try {
            AccountUserProfileEntity profile = new AccountUserProfileEntity();
            profile.setUsername(accountUserProfileDto.getUsername());
            profile.setPassword(accountUserProfileDto.getPassword());

            AccountUserEntity user = new AccountUserEntity();
            user.setEmail("example@example.com");
            user.setMobile("0000000000");
            user.setAccountUserProfile(profile);

            accountUserRepository.save(user);

            log.info("[AccountUserService][createUser] User profile successfully created with ID: {}", user.getId());
            return new AccountUserResponseDto("User Profile successfully created", user.getId());
        } catch (Exception e) {
            log.error("[AccountUserService][createUser] Error occurred while creating user: {}", e.getMessage(), e);
            return new AccountUserResponseDto("Failed to create user profile: " + e.getMessage(), null);
        }
    }

    public AccountUserResponseDto updateUserDetails(Long userId, UpdateAccountUserDetailDto updateAccountUserDetailDto) {
        log.info("[AccountUserService][updateUserDetails] Attempting to update user details for userId: {} with data: {}", userId, updateAccountUserDetailDto);
        Optional<AccountUserEntity> existingUser = Optional.ofNullable(accountUserRepository.findById(userId).orElse(null));

        if (existingUser.isEmpty()) {
            return new AccountUserResponseDto("User not found", null);
        }
        if (accountUserRepository.existsByEmail(updateAccountUserDetailDto.getEmail()) && !existingUser.get().getEmail().equals(updateAccountUserDetailDto.getEmail())) {
            return new AccountUserResponseDto("Email already in use", null);
        }
        if (accountUserRepository.existsByMobile(updateAccountUserDetailDto.getMobile()) && !existingUser.get().getMobile().equals(updateAccountUserDetailDto.getMobile())) {
            return new AccountUserResponseDto("Mobile number already in use", null);
        }
        existingUser.get().setFirstName(updateAccountUserDetailDto.getFirstName());
        existingUser.get().setLastName(updateAccountUserDetailDto.getLastName());
        existingUser.get().setEmail(updateAccountUserDetailDto.getEmail());
        existingUser.get().setMobile(updateAccountUserDetailDto.getMobile());

        accountUserRepository.save(existingUser.get());

        return new AccountUserResponseDto("Profile details updated successfully. Your profile is now complete.", existingUser.get().getId());
    }

    public AccountUserResponseDto updateUserPassword(UpdateAccountUserProfilePasswordDto updateAccountUserProfilePasswordDto) {
    log.info("[AccountUserService][updateUserPassword] Attempting to update user password: {} with data: {}", updateAccountUserProfilePasswordDto);

        Optional<AccountUserEntity> optionalUser = accountUserRepository.findByEmailOrMobileAndAccountUserProfile_Username(
                updateAccountUserProfilePasswordDto.getEmail(),
                updateAccountUserProfilePasswordDto.getMobile(),
                updateAccountUserProfilePasswordDto.getUsername()
        );
        if (!optionalUser.isPresent()) {
            return new AccountUserResponseDto("User not found. Please check the email or phone number.", null);
        }
        AccountUserEntity existingUser = optionalUser.get();
        if (existingUser.getAccountUserProfile() != null &&
                existingUser.getAccountUserProfile().getUsername().equals(updateAccountUserProfilePasswordDto.getUsername())) {
            existingUser.getAccountUserProfile().setPassword(updateAccountUserProfilePasswordDto.getPassword());
        } else {
            return new AccountUserResponseDto("Username does not match the provided details.", null);
        }

        accountUserRepository.save(existingUser);

        return new AccountUserResponseDto("Password updated successfully", existingUser.getId());
    }

}
