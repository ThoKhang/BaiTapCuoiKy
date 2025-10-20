/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;



@Service
public class OTPService {
    private Map<String, OTPInfo> otpMap = new HashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;
    public String generateOTP(String email)
    {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i= 0; i <OTP_LENGTH ; i++)
        {
            otp.append(random.nextInt(10));
        }
        String otpStr = otp.toString();
        otpMap.put(email, new OTPInfo(otpStr,LocalDateTime.now()));
        return otpStr;
    }
    public boolean validateOTP(String email, String otp)
    {
        OTPInfo info = otpMap.get(email);
        if (info == null)
            return false;
        if (LocalDateTime.now().isAfter(info.timestamp.plusMinutes(EXPIRE_MINUTES)))
        {
            otpMap.remove(email);
            return false;
        }
        if(info.otp.equals(otp))
        {
            otpMap.remove(email);
            return true;
        }
        return false;
    }

}
