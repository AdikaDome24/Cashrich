package com.cashrich.controller;
import com.cashrich.model.*;
import com.cashrich.service.AccountUserService;
import com.cashrich.service.ThirdPartyAPIService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Log4j2
public class AccountUserController {

    @Autowired
    private final AccountUserService accountUserService;

    @Autowired
    private final ThirdPartyAPIService thirdPartyAPIService;

    public AccountUserController(AccountUserService accountUserService, ThirdPartyAPIService thirdPartyAPIService) {
        this.accountUserService = accountUserService;
        this.thirdPartyAPIService = thirdPartyAPIService;
    }


    @GetMapping(value = "/login/{userId}",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountUserDto> loginUser(@PathVariable Long userId,
                                                    @RequestHeader("X-CMC_PRO_API_KEY") String apiKey) {
        log.info("[AccountUserController][loginUser],Received login request for userId: {}", userId);
        AccountUserDto user = accountUserService.findByUsername(userId);
        if (user != null) {
            log.info("[AccountUserController][loginUser],User found: {}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.info("[AccountUserController][loginUser],User not found for userId: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/sign-up",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountUserResponseDto> createProfile(@RequestBody AccountUserProfileDto accountUserProfileDto,
                                                                @RequestHeader ("X-CMC_PRO_API_KEY") String apiKey) {
        log.info("[AccountUserController][createProfile],Received login request for userId: {}", accountUserProfileDto);
        try {
            AccountUserResponseDto response = accountUserService.createUser(accountUserProfileDto);
            log.info("[AccountUserController][createProfile] User created successfully with response: {}", response);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("[AccountUserController][createProfile] Error during user creation: {}", e.getMessage(), e);
            return new ResponseEntity<>(new AccountUserResponseDto("Failed to sign-up = " + e.getMessage(),null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update-users/{userId}",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountUserResponseDto> updateUserDetails(@PathVariable Long userId, @RequestBody UpdateAccountUserDetailDto updateAccountUserDetailDto) {
        log.info("[AccountUserController][updateUserDetails] Received update request for userId: {} with details: {}", userId, updateAccountUserDetailDto);
        AccountUserResponseDto response = accountUserService.updateUserDetails(userId, updateAccountUserDetailDto);
        if (response.getMessage().equals("User updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.info("[AccountUserController][updateUserDetails] Failed to update user details for userId: {}. Reason: {}", userId, response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "users/update-password",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountUserResponseDto> updatePassword(@RequestBody UpdateAccountUserProfilePasswordDto updateAccountUserProfilePasswordDto) {
        log.info("[AccountUserController][updatePassword] Received update request for user: {} with details: {}", updateAccountUserProfilePasswordDto);
        AccountUserResponseDto response = accountUserService.updateUserPassword(updateAccountUserProfilePasswordDto);
        if (response.getMessage().equals("User updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.info("[AccountUserController][updatePassword] Failed to update user password: {}. Reason: {}",  response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/coin",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCryptoData(@RequestParam String userId) {
        log.info("[CryptoController][getCryptoData] Received request to fetch crypto data for userId: {}", userId);
        try {
            String response = thirdPartyAPIService.callCoinApi(userId);
            log.info("[CryptoController][getCryptoData] Successfully fetched crypto data for userId: {}", userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("[CryptoController][getCryptoData] Successfully fetched crypto data for userId: {}", userId);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
