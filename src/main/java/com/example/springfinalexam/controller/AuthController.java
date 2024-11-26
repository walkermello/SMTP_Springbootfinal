package com.example.springfinalexam.controller;

import com.example.springfinalexam.dto.validasi.ValUserDTO;
import com.example.springfinalexam.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid ValUserDTO valUserDTO) {
        try {
            authService.register(valUserDTO);
            return "Registrasi berhasil, OTP telah dikirim ke email Anda.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Terjadi kesalahan saat registrasi: " + e.getMessage();
        }
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        try {
            boolean isVerified = authService.verifyOtp(email, otp);
            if (isVerified) {
                return "OTP berhasil diverifikasi! User aktif.";
            } else {
                return "OTP verification failed.";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
