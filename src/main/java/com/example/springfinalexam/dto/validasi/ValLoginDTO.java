package com.example.springfinalexam.dto.validasi;

import jakarta.validation.constraints.*;

public class ValLoginDTO {
    @NotNull(message = "Username tidak boleh null")
    @NotEmpty(message = "Username tidak boleh null")
    @NotBlank(message = "Username tidak boleh blank")
    private String username;

    @NotNull(message = "Password tidak boleh null")
    @NotEmpty(message = "Password tidak boleh null")
    @NotBlank(message = "Password tidak boleh blank")
    private String password;

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
