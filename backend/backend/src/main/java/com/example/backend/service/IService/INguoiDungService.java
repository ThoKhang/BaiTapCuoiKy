package com.example.backend.service.IService;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.NguoiDungResponse;

public interface INguoiDungService {

    NguoiDungResponse register(RegisterRequest request);

    String login(LoginRequest request);

    boolean verifyOtp(String email, String otp);

    void sendOtp(String email);

    void resetPassword(String email, String newPassword);

    NguoiDungResponse getByEmail(String email);
}
