package com.example.springfinalexam.service;

import com.example.springfinalexam.dto.validasi.ValUserDTO;
import com.example.springfinalexam.model.User;
import com.example.springfinalexam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    private final SendEmailService sendEmailService;
    private final UserRepo userRepository;

    @Autowired
    public AuthService(SendEmailService sendEmailService, UserRepo userRepository) {
        this.sendEmailService = sendEmailService;
        this.userRepository = userRepository;
    }

    // Method untuk registrasi user dan mengirim OTP ke email
    public void register(ValUserDTO valUserDTO) {
        System.out.println("User berhasil didaftarkan: " + valUserDTO.getUsername());

        // Generate OTP
        String otp = generateOTP();
        // Set OTP expiration time (valid for 10 minutes)
        LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(10);

        // Kirim OTP ke email yang diregistrasikan
        try {
            String subject = "OTP untuk Registrasi";
            String message = "Selamat! Anda telah berhasil melakukan registrasi. Berikut adalah OTP untuk verifikasi: " + otp;
            sendEmailService.sendEmail(valUserDTO.getEmail(), subject, message);
            System.out.println("OTP telah dikirim ke email: " + valUserDTO.getEmail());

            // Simpan informasi user tanpa status OTP verified
            User user = new User();
            user.setNamaLengkap(valUserDTO.getNamaLengkap());
            user.setAlamat(valUserDTO.getAlamat());
            user.setNoTelp(valUserDTO.getNoTelp());
            user.setTanggalLahir(valUserDTO.getTanggalLahir());
            user.setEmail(valUserDTO.getEmail());
            user.setUsername(valUserDTO.getUsername());
            user.setPassword(valUserDTO.getPassword());
            user.setOtp(otp);  // Set OTP yang dihasilkan
            user.setOtpExpirationTime(otpExpirationTime);  // Set waktu kedaluwarsa OTP
            user.setOtpVerified(false);  // Status OTP belum diverifikasi

            userRepository.save(user);  // Simpan ke database
            System.out.println("User berhasil disimpan sementara.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal mengirim OTP ke email: " + e.getMessage());
        }
    }

    // Method untuk menghasilkan OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate OTP 6 digit
        return String.valueOf(otp);
    }

    // Verifikasi OTP dan update status OTP user
    public boolean verifyOtp(String email, String otpInput) {
        // Validasi apakah email terdaftar
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email tidak terdaftar!");
        }

        // Verifikasi apakah OTP telah kedaluwarsa
        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP sudah kedaluwarsa!");
        }

        // Verifikasi OTP
        if (!user.getOtp().equals(otpInput)) {
            throw new IllegalArgumentException("OTP yang dimasukkan salah!");
        }

        // Mark OTP as verified
        user.setOtpVerified(true);
        userRepository.save(user);  // Update status OTP di database

        return true;
    }
}
