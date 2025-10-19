package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;  // ← Thêm import này cho List

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép Android gọi API
public class UserController {

    @Autowired
    private UserRepository repo;

    // Thêm mới: Lấy tất cả users (cho test API GET)
    @GetMapping("/users")  // ← Path này + @RequestMapping("/api") → /api/users
    public List<User> getAllUsers() {
        return repo.findAll();  // Trả list từ DB (nếu rỗng → [])
    }

    // Đăng ký (giữ nguyên)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (repo.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        repo.save(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    // Đăng nhập (giữ nguyên)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existing = repo.findByEmail(user.getEmail());
        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok("Đăng nhập thành công!");
        }
        return ResponseEntity.status(401).body("Sai email hoặc mật khẩu!");
    }
}