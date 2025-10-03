package com.TradeHub.controller;

import com.TradeHub.model.entity.User;
import com.TradeHub.model.entity.VerificationToken;
import com.TradeHub.repository.UserRepository;
import com.TradeHub.service.VerificationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Verification")
@CrossOrigin
public class VerificationController {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());



    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        logger.info("Attempting to verify account with token: " + token);

        VerificationToken verificationToken = tokenService.getToken(token);
        if (verificationToken == null) {
            logger.warning("Invalid verification token: " + token);
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("User " + user.getId() + " verified successfully");

        tokenService.deleteToken(verificationToken);
        logger.info("Verification token " + token + " deleted after successful verification");

        return ResponseEntity.ok("Account verified successfully");
    }
}
