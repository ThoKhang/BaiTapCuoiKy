package com.example.backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenPassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("Admin@123"));
    }
}
