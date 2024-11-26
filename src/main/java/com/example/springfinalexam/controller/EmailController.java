package com.example.springfinalexam.controller;

import com.example.springfinalexam.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final SendEmailService sendEmailService;

    public EmailController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {
        try {
            sendEmailService.sendEmail("danieladitya38@gmail.com", "Hi DADDY", "Pijat ++");
            return "Email berhasil dikirim!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Gagal mengirim email: " + e.getMessage();
        }
    }
}
