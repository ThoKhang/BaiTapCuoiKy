package com.example.backend.service.IService;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.LichSuDiemResponse;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.dto.response.XepHangResponse;

public interface INguoiDungService {

    NguoiDungResponse register(RegisterRequest request);

    String login(LoginRequest request);

    boolean verifyOtp(String email, String otp);

    void sendOtp(String email);

    void resetPassword(String email, String newPassword);

    NguoiDungResponse getByEmail(String email);
    
    // 👉 Hàm mới: lấy thông tin xếp hạng    
    XepHangResponse layXepHang(String email, int gioiHan);
    LichSuDiemResponse layThongKeDiemVaLichSu(String email);
    NguoiDungResponse loginWithGoogle(String email);
    
    void updateThongTinNguoiDung(String tenDangNhap,String email);
}

