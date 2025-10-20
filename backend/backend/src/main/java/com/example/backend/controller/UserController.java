package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.EmailService;  // Import EmailService (nếu chưa có)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép Android gọi API
public class UserController {
    
    @Autowired
    private UserRepository repo;
    
    @Autowired
    private EmailService emailService;  // Sửa tên biến: lowercase, chuẩn Java
    
    // Lấy tất cả users (giữ nguyên)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // Đăng ký: Gửi email xác nhận sau khi save
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (repo.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        repo.save(user);
        
        // Gửi email xác nhận
        try {
            emailService.sendEmail(
                user.getEmail(),  // Người nhận
                "Xác nhận đăng ký tài khoản thành công!",  // Tiêu đề
                "Chào " + (user.getUsername() != null ? user.getUsername() : "người dùng") + "!\n\n" +
                "Bạn đã đăng ký thành công với email: " + user.getEmail() + ".\n" +
                "Cảm ơn bạn đã sử dụng App Hoc Táp Cho Trẻ!\n\n" +
                "Nếu cần hỗ trợ, liên hệ: support@apphoctapchotre.com\n\n" +
                "Trân trọng,\nTeam App Hoc Táp Cho Trẻ"  // Nội dung tùy chỉnh
            );
        } catch (Exception e) {
            // Log lỗi, nhưng không fail API
            System.err.println("Lỗi gửi email đăng ký: " + e.getMessage());
        }
        
        return ResponseEntity.ok("Đăng ký thành công! Email xác nhận đã được gửi.");
    }

    // Đăng nhập: Gửi email chào mừng nếu thành công
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existing = repo.findByEmail(user.getEmail());
        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            // Gửi email chào mừng đăng nhập
            try {
                emailService.sendEmail(
                    existing.getEmail(),  // Người nhận
                    "Chào mừng bạn đăng nhập thành công!",  // Tiêu đề
                    "Chào " + (existing.getUsername() != null ? existing.getUsername() : "người dùng") + "!\n\n" +
                    "Bạn đã đăng nhập thành công vào App Hoc Tập Cho Trẻ lúc " + java.time.LocalDateTime.now() + ".\n" +
                    "Chúc bạn có buổi học vui vẻ!\n\n" +
                    "Trân trọng,\nTeam App Hoc Tập Cho Trẻ, Nhóm 5ae"  // Nội dung với thời gian hiện tại
                );
            } catch (Exception e) {
                // Log lỗi, nhưng không fail login
                System.err.println("Lỗi gửi email đăng nhập: " + e.getMessage());
            }
            
            return ResponseEntity.ok("Đăng nhập thành công! Email chào mừng đã được gửi.");
        }
        return ResponseEntity.status(401).body("Sai email hoặc mật khẩu!");
    }
}