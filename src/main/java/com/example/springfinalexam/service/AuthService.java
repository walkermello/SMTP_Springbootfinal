package com.example.springfinalexam.service;

import com.example.springfinalexam.dto.validasi.ValUserDTO;
import com.example.springfinalexam.model.User;
import com.example.springfinalexam.repo.UserRepo;
import com.example.springfinalexam.security.Crypto;
import com.example.springfinalexam.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class AuthService implements UserDetailsService {

    private final SendEmailService sendEmailService;
    private final UserRepo userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    public AuthService(SendEmailService sendEmailService, UserRepo userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.sendEmailService = sendEmailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method untuk registrasi user dan mengirim OTP ke email
    public void register(ValUserDTO valUserDTO) {
        System.out.println("User berhasil didaftarkan: " + valUserDTO.getUsername());

        // Generate OTP
        String otp = generateOTP();
        // Set OTP expiration time (valid for 10 minutes)
        LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(10);

        try {
            String subject = "OTP untuk Registrasi";
            String message = "Selamat! Anda telah berhasil melakukan registrasi. Berikut adalah OTP untuk verifikasi: " + otp;
            sendEmailService.sendEmail(valUserDTO.getEmail(), subject, message);

            // Simpan user ke database
            User user = new User();
            user.setNamaLengkap(valUserDTO.getNamaLengkap());
            user.setAlamat(valUserDTO.getAlamat());
            user.setNoTelp(valUserDTO.getNoTelp());
            user.setTanggalLahir(valUserDTO.getTanggalLahir());
            user.setEmail(valUserDTO.getEmail());
            user.setUsername(valUserDTO.getUsername());
            user.setPassword(passwordEncoder.encode(valUserDTO.getPassword())); // Encrypt password
            user.setOtp(otp);
            user.setOtpExpirationTime(otpExpirationTime);
            user.setOtpVerified(false);

            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengirim OTP atau menyimpan user: " + e.getMessage());
        }
    }

    // Login API
    public ResponseEntity<Object> login(User userInput, HttpServletRequest request) {
        // Validasi jika username null, kosong atau spasi
        if (userInput.getUsername() == null || userInput.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "username tidak boleh null atau blank"));
        }

        // Validasi jika password null, kosong atau spasi
        if (userInput.getPassword() == null || userInput.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "password tidak boleh null atau blank"));
        }

        // Validasi username, apakah ada di database
        Optional<User> optionalUser = userRepository.findByUsername(userInput.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Username atau password salah"));
        }

        User user = optionalUser.get();

        // Validasi jika OTP sudah diverifikasi
        if (!user.isOtpVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Username atau password salah"));
        }

        // Validasi password
        if (!passwordEncoder.matches(userInput.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Username atau password salah"));
        }

        // Generate JWT token
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("nl", user.getNamaLengkap());
        claims.put("al", user.getAlamat());
        claims.put("nt", user.getNoTelp());
        claims.put("tl", user.getTanggalLahir().format(DateTimeFormatter.ISO_DATE));
        claims.put("ml", user.getEmail());
        claims.put("un", user.getUsername());

        String token = jwtUtility.generateToken(userDetails, claims);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("token", Crypto.performEncrypt(token));

        // Return response dengan status 201
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Method untuk menghasilkan OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
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
            throw new IllegalArgumentException("OTP yang anda input salah, silahkan dicoba kembali!!");
        }

        // Mark OTP as verified
        user.setOtpVerified(true);
        userRepository.save(user);  // Update status OTP di database

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public User convertToEntity(ValUserDTO valUserDTO){
        return modelMapper.map(valUserDTO, User.class);
    }
}
