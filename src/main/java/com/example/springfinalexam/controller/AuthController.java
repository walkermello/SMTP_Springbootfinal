package com.example.springfinalexam.controller;

import com.example.springfinalexam.dto.validasi.ValLoginDTO;
import com.example.springfinalexam.dto.validasi.ValUserDTO;
import com.example.springfinalexam.model.User;
import com.example.springfinalexam.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody ValLoginDTO valLoginDTO, HttpServletRequest request) {
        User user = new User();
        user.setUsername(valLoginDTO.getUsername());
        user.setPassword(valLoginDTO.getPassword());
        return authService.login(user, request);
    }

}
