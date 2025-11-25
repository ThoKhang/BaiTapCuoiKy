package com.example.backend.controller;

import com.example.backend.model.NguoiDung;
import com.example.backend.service.EmailService;
import com.example.backend.service.IService.INguoiDungService;
import com.example.backend.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Cho phép Android gọi API
public class NguoiDungController {

    @Autowired
    private INguoiDungService nguoiDungService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private DataSource dataSource;

    // 🔵 TEST KẾT NỐI DATABASE
    @GetMapping("/db-test")
    public String checkDatabase() {
        try (var conn = dataSource.getConnection()) {
            return "Kết nối thành công với DB: " + conn.getCatalog();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi kết nối DB: " + e.getMessage();
        }
    }

    // 🔵 LẤY TẤT CẢ NGƯỜI DÙNG
    @GetMapping("/nguoi-dung")
    public ResponseEntity<?> getAllNguoiDung() {
        try {
            return ResponseEntity.ok(nguoiDungService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi lấy dữ liệu: " + e.getMessage());
        }
    }

    // 🟢 ĐĂNG KÝ NGƯỜI DÙNG + GỬI OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NguoiDung nguoiDung) {
        try {
            // Email tồn tại?
            if (nguoiDungService.findByEmail(nguoiDung.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email đã tồn tại, vui lòng dùng email khác!");
            }

            // Tạo mã NDxxx
            String newId = nguoiDungService.generateNewId();
            nguoiDung.setMaNguoiDung(newId);

            nguoiDungService.createUser(nguoiDung);

            // Gửi OTP
            emailService.sendOTP(nguoiDung.getEmail(), nguoiDung.getTenDangNhap());

            return ResponseEntity.ok("Đăng ký thành công! OTP đã gửi về email.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi đăng ký: " + e.getMessage());
        }
    }

    // 🟠 ĐĂNG NHẬP + GỬI OTP
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody NguoiDung request) {
        try {
            NguoiDung user = nguoiDungService.findByEmail(request.getEmail());

            if (user == null || !user.getMatKhauMaHoa().equals(request.getMatKhauMaHoa())) {
                return ResponseEntity.badRequest().body("Sai email hoặc mật khẩu!");
            }

            // Gửi OTP để xác thực
            emailService.sendOTP(user.getEmail(), user.getTenDangNhap());

            return ResponseEntity.ok("Đăng nhập thành công! Vui lòng kiểm tra email để nhận OTP.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi đăng nhập: " + e.getMessage());
        }
    }

    // 🟡 XÁC THỰC OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String otp = request.get("otp");

            if (otpService.validateOTP(email, otp)) {
                return ResponseEntity.ok("Xác thực OTP thành công!");
            } else {
                return ResponseEntity.badRequest().body("OTP không đúng hoặc đã hết hạn!");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi xác thực OTP: " + e.getMessage());
        }
    }

    // 🔵 GỬI OTP THỦ CÔNG
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            NguoiDung user = nguoiDungService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.badRequest().body("Email không tồn tại!");
            }

            emailService.sendOTP(email, user.getTenDangNhap());
            return ResponseEntity.ok("OTP đã gửi!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi gửi OTP: " + e.getMessage());
        }
    }

    // 🟣 QUÊN MẬT KHẨU – GỬI OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            NguoiDung user = nguoiDungService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.badRequest().body("Email không tồn tại!");
            }

            emailService.sendOTP(email, user.getTenDangNhap());

            return ResponseEntity.ok("OTP đã được gửi.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi gửi OTP: " + e.getMessage());
        }
    }

    // 🟢 ĐỔI MẬT KHẨU
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("newPassword");

            if (email == null || newPassword == null) {
                return ResponseEntity.badRequest().body("Thiếu dữ liệu!");
            }

            NguoiDung user = nguoiDungService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy tài khoản!");
            }

            user.setMatKhauMaHoa(newPassword);
            nguoiDungService.createUser(user);

            return ResponseEntity.ok("Đổi mật khẩu thành công!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi reset mật khẩu: " + e.getMessage());
        }
    }

}
