package com.example.backend.controller;

import com.example.backend.model.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
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
public class NguoiDungController {
    
    @Autowired
    private NguoiDungRepository repo;
    
    @Autowired
    private EmailService emailService;  // Sửa tên biến: lowercase, chuẩn Java
    
    @Autowired
    private OTPService otpService;
    
    @Autowired
private javax.sql.DataSource dataSource;

@GetMapping("/db-test")
public String checkDatabase() {
    try (var conn = dataSource.getConnection()) {
        return "Đang kết nối tới database: " + conn.getCatalog();
    } catch (Exception e) {
        e.printStackTrace();
        return "Lỗi khi kiểm tra DB: " + e.getMessage();
    }
}

    // Lấy tất cả người dùng (thêm try-catch để tránh JSON error)
    @GetMapping("/nguoi-dung")
    public ResponseEntity<?> getAllNguoiDung() {
        try {
            return ResponseEntity.ok(repo.findAll());
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi
            return ResponseEntity.status(500).body("Lỗi khi lấy dữ liệu: " + e.getMessage());
        }
    }

    // Đăng ký: Gửi email xác nhận sau khi save
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NguoiDung nguoiDung) {
        try {
            if (repo.findByEmail(nguoiDung.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Người dùng đã tồn tại, vui lòng nhập người dùng mới!");
            }
            nguoiDung.setMaVaiTro((byte) 2);  // Default NguoiDung (MaVaiTro = 2)
            repo.save(nguoiDung);
            emailService.sendOTP(nguoiDung.getEmail(), nguoiDung.getTenDangNhap());
            return ResponseEntity.ok("OTP đã gửi về email của bạn! Vui lòng kiểm tra!");
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            return ResponseEntity.status(500).body("Lỗi đăng ký: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody NguoiDung nguoiDung) {
        try {
            NguoiDung existing = repo.findByEmail(nguoiDung.getEmail());
            if (existing != null && existing.getMatKhauMaHoa().equals(nguoiDung.getMatKhauMaHoa())) {
                emailService.sendOTP(existing.getEmail(), existing.getTenDangNhap());
                return ResponseEntity.ok("Đăng nhập thành công! Vui lòng kiểm tra Email để nhận OTP!");
            }
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu không đúng!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi đăng nhập: " + e.getMessage());
        }
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String otp = request.get("otp");
            if (otpService.validateOTP(email, otp)) {
                return ResponseEntity.ok("Xác thực OTP thành công! Chào mừng đến với ứng dụng");
            }
            return ResponseEntity.badRequest().body("Xác thực không thành công, OTP không đúng hoặc hết hạn!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi xác thực OTP: " + e.getMessage());
        }
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            NguoiDung nguoiDung = repo.findByEmail(email);
            if (nguoiDung == null) {
                return ResponseEntity.badRequest().body("Email không tồn tại!");
            }
            emailService.sendOTP(email, nguoiDung.getTenDangNhap());
            return ResponseEntity.ok("OTP đã gửi đến email của bạn!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi gửi OTP: " + e.getMessage());
        }
    }
    // 🟢 Gửi OTP khi quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            NguoiDung nguoiDung = repo.findByEmail(email);

            if (nguoiDung == null) {
                return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống!");
            }

            // Gửi OTP qua email
            emailService.sendOTP(email, nguoiDung.getTenDangNhap());
            return ResponseEntity.ok("Đã gửi OTP đến email của bạn!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi gửi OTP: " + e.getMessage());
        }
    }

    // 🟡 Xác nhận OTP và đổi mật khẩu mới
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("newPassword");

            if (email == null || newPassword == null) {
                return ResponseEntity.badRequest().body("Thiếu thông tin cần thiết!");
            }


            NguoiDung nguoiDung = repo.findByEmail(email);
            if (nguoiDung == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy người dùng!");
            }

            // ✅ Cập nhật mật khẩu mới
            nguoiDung.setMatKhauMaHoa(newPassword); // sau này có thể mã hóa bằng BCrypt
            repo.save(nguoiDung);

            return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi reset mật khẩu: " + e.getMessage());
        }
    }

}