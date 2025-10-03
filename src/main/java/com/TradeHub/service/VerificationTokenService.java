package com.TradeHub.service;

import com.TradeHub.model.entity.User;
import com.TradeHub.model.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createToken(User user);
    VerificationToken getToken(String token);
    void deleteToken(VerificationToken token);
}
