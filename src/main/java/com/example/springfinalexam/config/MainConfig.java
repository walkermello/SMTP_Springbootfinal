package com.example.springfinalexam.config;

import com.example.springfinalexam.security.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class MainConfig {

    @Autowired
    private Environment environment;

    @Primary
    @Bean
    public DataSource getDataSource() {

        String encryptedPassword = environment.getProperty("spring.datasource.password");
        String decryptedPassword = Crypto.performDecrypt(encryptedPassword);

        // Membangun DataSource dengan semua properti (Tidak bisa hanya satu properti saja)
        return DataSourceBuilder.create()
                .driverClassName(environment.getProperty("spring.datasource.driverClassName"))  // Driver class
                .url(environment.getProperty("spring.datasource.url"))                         // JDBC URL
                .username(environment.getProperty("spring.datasource.username"))              // Username
                .password(decryptedPassword)                                                  // Password (didekripsi)
                .build();
    }
}
