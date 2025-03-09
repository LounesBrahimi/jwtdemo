package com.jwtdemo.controller;

import org.springframework.web.bind.annotation.*;

import com.jwtdemo.service.EncryptionService;
import com.jwtdemo.service.JwtService;

@RestController
@RequestMapping("/jwt")
public class JwtController {
    private final JwtService jwtService;
    private final EncryptionService encryptionService;

    public JwtController(JwtService jwtService, EncryptionService encryptionService) {
        this.jwtService = jwtService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/sign")
    public String signJwt(@RequestParam String user) throws Exception {
        return jwtService.generateSignedJwt(user);
    }

    @PostMapping("/verify")
    public boolean verifyJwt(@RequestParam String token) {
        return jwtService.verifySignedJwt(token);
    }

    @PostMapping("/encrypt")
    public String encryptJwt(@RequestParam String data) throws Exception {
        return encryptionService.encryptJwt(data);
    }

    @PostMapping("/decrypt")
    public String decryptJwt(@RequestParam String token) throws Exception {
        return encryptionService.decryptJwt(token);
    }
}