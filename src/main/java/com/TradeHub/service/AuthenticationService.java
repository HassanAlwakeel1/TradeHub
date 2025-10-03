package com.TradeHub.service;


import com.TradeHub.model.dto.JwtAuthenticationResponse;
import com.TradeHub.model.dto.RefreshTokenRequest;
import com.TradeHub.model.dto.SignInRequest;
import com.TradeHub.model.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
