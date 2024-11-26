package com.example.springfinalexam.dto.validasi;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ValUserDTO {

    @NotNull(message = "Nama Lengkap tidak boleh null")
    @NotEmpty(message = "Nama Lengkap tidak boleh kosong")
    @NotBlank(message = "Nama Lengkap tidak boleh kosong atau hanya spasi")
    @Size(min = 6, max = 15, message = "Nama Lengkap harus memiliki panjang antara 6 hingga 15 karakter")
    @Pattern(regexp = "^[a-z ]+$", message = "Nama Lengkap hanya boleh mengandung huruf kecil dan spasi")
    private String namaLengkap;

    @NotNull(message = "Alamat tidak boleh null")
    @NotEmpty(message = "Alamat tidak boleh kosong")
    @NotBlank(message = "Alamat tidak boleh kosong atau hanya spasi")
    @Size(min = 30, max = 255, message = "Alamat harus memiliki panjang antara 30 hingga 255 karakter")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Alamat hanya boleh mengandung karakter alfanumerik dan spasi")
    private String alamat;

    @NotNull(message = "No Telp tidak boleh null")
    @NotEmpty(message = "No Telp tidak boleh kosong")
    @NotBlank(message = "No Telp tidak boleh kosong atau hanya spasi")
    @Size(min = 12, max = 18, message = "No Telp harus memiliki panjang antara 12 hingga 18 karakter")
    @Pattern(regexp = "^(0|62|\\+62)\\d+$", message = "No Telp harus diawali dengan 0, 62, atau +62 dan hanya boleh mengandung angka")
    private String noTelp;

    @NotNull(message = "Tanggal Lahir tidak boleh null")
    private LocalDate tanggalLahir;

    @NotNull(message = "Email tidak boleh null")
    @NotEmpty(message = "Email tidak boleh kosong")
    @NotBlank(message = "Email tidak boleh kosong atau hanya spasi")
    @Email(message = "Format email tidak valid, pastikan menggunakan format seperti example@domain.com")
    private String email;

    @NotNull(message = "Username tidak boleh null")
    @NotEmpty(message = "Username tidak boleh kosong")
    @NotBlank(message = "Username tidak boleh kosong atau hanya spasi")
    @Size(min = 7, max = 15, message = "Username harus memiliki panjang antara 7 hingga 15 karakter")
    @Pattern(regexp = "^[a-z]+$", message = "Username hanya boleh mengandung huruf kecil tanpa spasi")
    private String username;

    @NotNull(message = "Password tidak boleh null")
    @NotEmpty(message = "Password tidak boleh kosong")
    @NotBlank(message = "Password tidak boleh kosong atau hanya spasi")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_\\-#$]).{6,}$",
            message = "Password harus mengandung minimal 1 huruf besar, 1 huruf kecil, 1 angka, dan 1 karakter spesial (_,-,#,$) serta minimal 6 karakter")
    private String password;



    // Getters and Setters
    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

