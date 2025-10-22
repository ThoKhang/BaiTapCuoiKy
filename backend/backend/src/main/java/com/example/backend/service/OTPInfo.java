/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class OTPInfo {
    String otp;
    LocalDateTime timestamp;
    OTPInfo(String otp, LocalDateTime timestamp) {
        this.otp = otp;
        this.timestamp = timestamp;
    }
}