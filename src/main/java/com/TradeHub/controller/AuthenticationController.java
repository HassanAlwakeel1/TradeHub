package com.TradeHub.controller;

import com.TradeHub.model.dto.*;
import com.TradeHub.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());


    @PreAuthorize("permitAll()")
    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest signUpRequest){
        logger.info("Signup attempt for email: " + signUpRequest.getEmail()); // Before calling service

        JwtAuthenticationResponse response;
        try {
            response = authenticationService.signup(signUpRequest);
            logger.info("Signup successful for email: " + signUpRequest.getEmail());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Signup failed for email: " + signUpRequest.getEmail(), e);
            throw e;
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = {"/signin","/login"})
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest){
        logger.info("Login attempt for email: " + signInRequest.getEmail());

        JwtAuthenticationResponse response;
        try {
            response = authenticationService.signin(signInRequest);
            logger.info("Login successful for email: " + signInRequest.getEmail());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Login failed for email: " + signInRequest.getEmail(), e);
            throw e;
        }

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        String token = refreshTokenRequest.getToken();
        logger.fine("Token refresh requested for token: " + token);

        JwtAuthenticationResponse response = authenticationService.refreshToken(refreshTokenRequest);

        logger.fine("Token refresh successful for token: " + token);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        logger.info("Password reset requested for email: " + email);

        authenticationService.initiatePasswordReset(email);

        logger.info("Password reset link sent for email: " + email);
        return ResponseEntity.ok("Password reset link has been sent to your email");
    }
    @PreAuthorize("permitAll()")
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        logger.info("Password reset attempt for token: " + token);

        authenticationService.resetPassword(token, request.getNewPassword());

        logger.info("Password reset successful for token: " + token);
        return ResponseEntity.ok("Password has been reset successfully");
    }
}
