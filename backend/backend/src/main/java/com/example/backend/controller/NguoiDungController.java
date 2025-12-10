package com.example.backend.controller;

import com.example.backend.dto.request.GoogleLoginRequest;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.dto.response.XepHangResponse;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            NguoiDungResponse res = service.register(request);
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            // Ví dụ: "Email đã tồn tại!", "Tên đăng nhập đã tồn tại!"
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            // Nếu đúng email + mật khẩu: service.login sẽ gửi OTP và trả về message
            String result = service.login(request); // "OTP đã gửi, vui lòng kiểm tra email."
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            // Ví dụ: "Sai email hoặc mật khẩu!"
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> body) {
        boolean ok = service.verifyOtp(body.get("email"), body.get("otp"));
        if (ok) {
            return ResponseEntity.ok("Xác thực OTP thành công!");
        }
        return ResponseEntity.badRequest().body("Mã OTP sai hoặc hết hạn!");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> body) {
        try {
            service.sendOtp(body.get("email"));
            return ResponseEntity.ok("Đã gửi OTP!");
        } catch (RuntimeException ex) {
            // Ví dụ: "Email không tồn tại!"
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        try {
            service.resetPassword(body.get("email"), body.get("newPassword"));
            return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
        } catch (RuntimeException ex) {
            // Ví dụ: "Không tìm thấy người dùng."
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }
    // API lấy xếp hạng
    @GetMapping("/xep-hang")
    public ResponseEntity<XepHangResponse> layXepHang(
            @RequestParam String email,
            @RequestParam(name = "gioiHan", defaultValue = "20") int gioiHan
    ) {
        XepHangResponse res = service.layXepHang(email, gioiHan);
        return ResponseEntity.ok(res);
    }
  
    @PostMapping("/lich-su-diem")
    public ResponseEntity<?> lichSuDiem(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email không được để trống!");
        }
        try {
            return ResponseEntity.ok(service.layThongKeDiemVaLichSu(email));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PostMapping("/get-by-email")
    public ResponseEntity<?> getByEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email không được để trống!");
        }

        NguoiDungResponse nd = service.getByEmail(email);
        if (nd == null) {
            return ResponseEntity.status(404)
                    .body("Không tìm thấy người dùng với email: " + email);
        }

        return ResponseEntity.ok(nd);
    }
    @PostMapping("/login-google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            NguoiDungResponse res = service.loginWithGoogle(request.getIdToken());
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
