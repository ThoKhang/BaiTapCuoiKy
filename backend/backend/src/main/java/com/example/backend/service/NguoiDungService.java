package com.example.backend.service;

import com.example.backend.converter.NguoiDungConverter;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.dto.response.NguoiDungXepHangResponse;
import com.example.backend.dto.response.XepHangResponse;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.IService.INguoiDungService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    @Override
    public XepHangResponse layXepHang(String email, int gioiHan) {
        if (gioiHan <= 0) gioiHan = 20;

        XepHangResponse res = new XepHangResponse();

        // Tổng số người chơi
        long tongNguoiChoi = nguoiDungRepository.count();
        res.setTongSoNguoiChoi(tongNguoiChoi);

        // Lấy danh sách người dùng sắp xếp theo TongDiem giảm dần
        List<NguoiDung> dsNguoiDung = nguoiDungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "tongDiem")
        );

        // Top N người chơi
        List<NguoiDungXepHangResponse> dsTop = new ArrayList<>();
        int hang = 1;
        for (NguoiDung nd : dsNguoiDung) {
            if (hang > gioiHan) break;
            int diem = nd.getTongDiem() != null ? nd.getTongDiem() : 0;
            NguoiDungXepHangResponse item = new NguoiDungXepHangResponse(
                    nd.getTenDangNhap(),
                    diem,
                    hang
            );
            dsTop.add(item);
            hang++;
        }
        res.setTopNguoiDung(dsTop);

        // Người dùng hiện tại (theo email)
        NguoiDung ndHienTai = nguoiDungRepository.findByEmail(email);
        if (ndHienTai != null) {
            int diemNguoiDung = ndHienTai.getTongDiem() != null ? ndHienTai.getTongDiem() : 0;
            int hangNguoiDung = nguoiDungRepository.layHangNguoiDungTheoDiem(diemNguoiDung);

            NguoiDungXepHangResponse nguoiDungHienTai = new NguoiDungXepHangResponse(
                    ndHienTai.getTenDangNhap(),
                    diemNguoiDung,
                    hangNguoiDung
            );
            res.setNguoiDungHienTai(nguoiDungHienTai);
        } else {
            res.setNguoiDungHienTai(null);
        }

        return res;
    }

}
