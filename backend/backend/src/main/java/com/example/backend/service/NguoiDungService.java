package com.example.backend.service;

import com.example.backend.converter.NguoiDungConverter;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.LichSuDiemItem;
import com.example.backend.dto.response.LichSuDiemResponse;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.dto.response.NguoiDungXepHangResponse;
import com.example.backend.dto.response.XepHangResponse;
import com.example.backend.entity.LichSuHoatDong;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.LichSuHoatDongRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.IService.INguoiDungService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    
    @Autowired
    private LichSuHoatDongRepository lichSuHoatDongRepository;
    
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
    private int tinhTongDiemNguoiDung(NguoiDung nguoiDung) {
        if (nguoiDung == null) return 0;

        int soLanTrucTuyen = (nguoiDung.getSoLanTrucTuyen() == null)
                ? 0
                : nguoiDung.getSoLanTrucTuyen();

        int tongDiemLichSu = lichSuHoatDongRepository
                .tongDiemTatCa(nguoiDung.getMaNguoiDung());

        return soLanTrucTuyen + tongDiemLichSu;
    }
    private void capNhatDangNhapHangNgay(String email) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) {
            throw new RuntimeException("Không tìm thấy người dùng khi cập nhật đăng nhập hằng ngày!");
        }

        // DB dùng SYSUTCDATETIME => tính ngày theo UTC cho khớp
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate today = nowUtc.toLocalDate();

        LocalDate lastLoginDate = null;
        if (nd.getLanDangNhapCuoi() != null) {
            lastLoginDate = nd.getLanDangNhapCuoi().toLocalDate();
        }

        // 👉 Nếu CHƯA từng login, hoặc lần cuối trước hôm nay => +1 lần trực tuyến
        if (lastLoginDate == null || lastLoginDate.isBefore(today)) {
            Integer soLan = nd.getSoLanTrucTuyen();
            if (soLan == null) soLan = 0;
            nd.setSoLanTrucTuyen(soLan + 1);
        }

        // Luôn cập nhật thời gian đăng nhập cuối
        nd.setLanDangNhapCuoi(nowUtc);

        // 👉 Cập nhật lại tổng điểm theo công thức: SoLanTrucTuyen + tổng điểm history
        int tongDiemMoi = tinhTongDiemNguoiDung(nd);
        nd.setTongDiem(tongDiemMoi);

        nguoiDungRepository.save(nd);
    }


    @Override
    public boolean verifyOtp(String email, String otp) {
        boolean hopLe = otpService.validateOTP(email, otp);

        if (!hopLe) {
            return false;
        }

        //  OTP đúng → coi như đăng nhập thành công → cập nhật lần đăng nhập + số lần trực tuyến
        capNhatDangNhapHangNgay(email);

        return true;
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
    
    @Override
    public LichSuDiemResponse layThongKeDiemVaLichSu(String email) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email);
        if (nguoiDung == null) {
            throw new RuntimeException("Không tìm thấy người dùng!");
        }

        int tongDiem = nguoiDung.getTongDiem();

        // Tổng điểm loại "Kiểm tra"
        int diemKiemTra = lichSuHoatDongRepository
                .tongDiemTheoLoai(nguoiDung.getMaNguoiDung(), "Kiểm tra");

        if (diemKiemTra < 0) diemKiemTra = 0;

        // Điểm hoạt động = Tổng - điểm kiểm tra (nếu âm thì đưa về 0 cho an toàn)
        int diemHoatDong = tongDiem - diemKiemTra;
        if (diemHoatDong < 0) diemHoatDong = 0;

        // Danh sách chi tiết lịch sử
        List<LichSuHoatDong> lichSu = lichSuHoatDongRepository
                .findByNguoiDungOrderByThoiGianDesc(nguoiDung);

        List<LichSuDiemItem> dsChiTiet = lichSu.stream().map(ls -> {
            LichSuDiemItem item = new LichSuDiemItem();
            item.setSoDiem(ls.getSoDiem());
            item.setThongTin(ls.getChiTiet());
            item.setThoiGian(ls.getThoiGian() != null
                    ? ls.getThoiGian().toString()   // ví dụ: "2025-11-17T15:30:00"
                    : "");
            return item;
        }).toList();

        LichSuDiemResponse res = new LichSuDiemResponse();
        res.setTongDiem(tongDiem);
        res.setDiemKiemTra(diemKiemTra);
        res.setDiemHoatDong(diemHoatDong);
        res.setDanhSachChiTiet(dsChiTiet);
        return res;
    }

}
