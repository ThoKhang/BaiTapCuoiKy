package com.example.backend.service;

import com.example.backend.converter.NguoiDungConverter;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.IService.INguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungService implements INguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @Override
    public NguoiDungResponse register(RegisterRequest request) {

        if (nguoiDungRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        if (nguoiDungRepository.findByTenDangNhap(request.getTenDangNhap()) != null) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Tạo mã NDxxx tự động
        String newId = "ND" + String.format("%03d", nguoiDungRepository.count() + 1);

        NguoiDung nd = new NguoiDung();
        nd.setMaNguoiDung(newId);
        nd.setTenDangNhap(request.getTenDangNhap());
        nd.setEmail(request.getEmail());
        nd.setMatKhauMaHoa(request.getMatKhau());

        nguoiDungRepository.save(nd);

        emailService.sendOTP(nd.getEmail(), nd.getTenDangNhap());

        return NguoiDungConverter.toResponse(nd);
    }

    @Override
    public String login(LoginRequest request) {
        NguoiDung nd = nguoiDungRepository.findByEmail(request.getEmail());

        if (nd == null || !nd.getMatKhauMaHoa().equals(request.getMatKhau())) {
            throw new RuntimeException("Sai email hoặc mật khẩu!");
        }

        emailService.sendOTP(nd.getEmail(), nd.getTenDangNhap());

        return "OTP đã gửi, vui lòng kiểm tra email.";
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        return otpService.validateOTP(email, otp);
    }

    @Override
    public void sendOtp(String email) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);

        if (nd == null) {
            throw new RuntimeException("Email không tồn tại!");
        }

        emailService.sendOTP(email, nd.getTenDangNhap());
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);

        if (nd == null) {
            throw new RuntimeException("Không tìm thấy người dùng.");
        }

        nd.setMatKhauMaHoa(newPassword);
        nguoiDungRepository.save(nd);
    }

    @Override
    public NguoiDungResponse getByEmail(String email) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);

        if (nd == null) return null;

        return NguoiDungConverter.toResponse(nd);
    }
}
