package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.service.IService.INguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nguoidung")
@CrossOrigin("*")
public class NguoiDungController {

    @Autowired
    private INguoiDungService service;

    @PostMapping("/register")
    public ResponseEntity<NguoiDungResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> body) {
        boolean ok = service.verifyOtp(body.get("email"), body.get("otp"));
        if (ok) return ResponseEntity.ok("Xác thực OTP thành công!");
        return ResponseEntity.badRequest().body("Mã OTP sai hoặc hết hạn!");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> body) {
        service.sendOtp(body.get("email"));
        return ResponseEntity.ok("Đã gửi OTP!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        service.resetPassword(body.get("email"), body.get("newPassword"));
        return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
    }
}
