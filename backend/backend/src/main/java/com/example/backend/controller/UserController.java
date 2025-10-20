package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.EmailService;  // Import EmailService (nếu chưa có)
import com.example.backend.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép Android gọi API
public class UserController {
    
    @Autowired
    private UserRepository repo;
    
    @Autowired
    private EmailService emailService;  // Sửa tên biến: lowercase, chuẩn Java
    
    @Autowired
    private OTPService otpService;
    
    // Lấy tất cả users (giữ nguyên)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // Đăng ký: Gửi email xác nhận sau khi save
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody User user)
    {
        if(repo.findByEmail(user.getEmail())!=null)
        {
            return ResponseEntity.ok("User đã tồn tại, vui lòng nhập user mới !");
        }
        repo.save(user);
        emailService.sendOTP(user.getEmail(), user.getUsername());
        return ResponseEntity.ok("OTP đã gửi về email của bạn ! vui lòng kiểm tra !");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody User user)
    {
        User existing = repo.findByEmail(user.getEmail());
        if(existing!= null && existing.getPassword().equals(user.getPassword()))
        {
            emailService.sendOTP(existing.getEmail(), existing.getUsername());
            return ResponseEntity.ok("Đăng nhập thành công! vui lòng kiểm tra Email để nhận OTP!");
        }
        return ResponseEntity.ok("Tài khoản hoặc mật khẩu không đúng !");
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verityOTP (@RequestBody Map<String,String> request)
    {
        String email = request.get ("email");
        String otp = request.get("otp");
        if(otpService.validateOTP(email, otp))
        {
            return ResponseEntity.ok("Xác thực OTP thành công! chào mừng đến với ứng dụng");
        }
        return ResponseEntity.ok("Xác thực không thành công, OTP không đúng hoặc hết hạn !");
    }
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = repo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại!");
        }
        emailService.sendOTP(email, user.getUsername());
        return ResponseEntity.ok("OTP đã gửi đến email của bạn!");
    }
}