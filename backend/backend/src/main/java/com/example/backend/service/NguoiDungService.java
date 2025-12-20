package com.example.backend.service;

import com.example.backend.converter.NguoiDungConverter;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.LichSuDiemItem;
import com.example.backend.dto.response.LichSuDiemResponse;
import com.example.backend.dto.response.NguoiDungResponse;
import com.example.backend.dto.response.NguoiDungXepHangResponse;
import com.example.backend.dto.response.XepHangResponse;
import com.example.backend.entity.NguoiDung;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.IService.INguoiDungService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

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

    private static final String GOOGLE_CLIENT_ID =
        "770393692760-to1bqmiu4gkrtcjgha0ok23ejej9cmjr.apps.googleusercontent.com";

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private TienTrinhHocTapRepository tienTrinhHocTapRepository;

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
        nguoiDungRepository.save(nd);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        boolean hopLe = otpService.validateOTP(email, otp);
        if (!hopLe) {
            return false;
        }
        // OTP đúng → coi như đăng nhập thành công → cập nhật lần đăng nhập + số lần trực tuyến
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
        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) {
            throw new RuntimeException("Không tìm thấy người dùng.");
        }

        // ===== Điểm hàng ngày =====
        Integer soLanTrucTuyen = nd.getSoLanTrucTuyen();
        if (soLanTrucTuyen == null) soLanTrucTuyen = 0;
        final int DIEM_MOI_LAN_DANG_NHAP = 10;
        int diemHoatDong = soLanTrucTuyen * DIEM_MOI_LAN_DANG_NHAP;

        // ===== Điểm kiểm tra + lịch sử bài làm =====
        List<TienTrinhHocTap> list =
                tienTrinhHocTapRepository.findByNguoiDung_MaNguoiDung(nd.getMaNguoiDung());

        System.out.println(">>> TienTrinh size = " + list.size());   // LOG

        int diemKiemTra = 0;
        List<LichSuDiemItem> chiTiet = new ArrayList<>();

        for (TienTrinhHocTap tt : list) {
            // CHỈ tính bài đã hoàn thành (nếu muốn tất cả thì bỏ if này)
            if (!tt.isDaHoanThanh()) {
                continue;
            }

            int diem = tt.getDiemDatDuoc();
            diemKiemTra += diem;

            LichSuDiemItem item = new LichSuDiemItem();
            item.setSoDiem(diem);
            item.setThongTin("Hoàn thành: " + tt.getHoatDong().getTieuDe());
            item.setThoiGian(
                    tt.getNgayHoanThanh() != null
                            ? tt.getNgayHoanThanh().toString()
                            : ""
            );
            chiTiet.add(item);

            System.out.println(">>> item: " + item.getThongTin()
                    + " | diem=" + item.getSoDiem()
                    + " | time=" + item.getThoiGian());             // LOG
        }

        System.out.println(">>> So item tra ve = " + chiTiet.size()); // LOG

        // ===== Tổng điểm + ghi lại vào NguoiDung =====
        int tongDiem = diemHoatDong + diemKiemTra;
        nd.setTongDiem(tongDiem);
        nguoiDungRepository.save(nd);

        // ===== Trả về =====
        LichSuDiemResponse res = new LichSuDiemResponse();
        res.setTongDiem(tongDiem);
        res.setDiemHoatDong(diemHoatDong);
        res.setDiemKiemTra(diemKiemTra);
        res.setDanhSachChiTiet(chiTiet);

        return res;
    }

    @Override
    public NguoiDungResponse loginWithGoogle(String idToken) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(GOOGLE_CLIENT_ID))
                .setIssuer("https://accounts.google.com")
                .build();

        GoogleIdToken token;
        try {
            token = verifier.verify(idToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token Google không hợp lệ!");
        }

        if (token == null) {
            throw new RuntimeException("Xác thực Google thất bại!");
        }

        GoogleIdToken.Payload payload = token.getPayload();

        System.out.println("===== GOOGLE ID TOKEN PAYLOAD =====");
        System.out.println("audience        : " + payload.getAudience());
        System.out.println("authorizedParty : " + payload.getAuthorizedParty());
        System.out.println("issuer          : " + payload.getIssuer());
        System.out.println("email           : " + payload.getEmail());
        System.out.println("===================================");

        String email = payload.getEmail();
        String name  = (String) payload.get("name");

        NguoiDung existing = nguoiDungRepository.findByEmail(email);

        if (existing == null) {
            String newId = "ND" + String.format("%03d", nguoiDungRepository.count() + 1);

            NguoiDung nd = new NguoiDung();
            nd.setMaNguoiDung(newId);
            nd.setTenDangNhap(name);
            nd.setEmail(email);
            nd.setMatKhauMaHoa("GOOGLE");

            nguoiDungRepository.save(nd);
            capNhatDangNhapHangNgay(email);
            return NguoiDungConverter.toResponse(nd);
        }

        capNhatDangNhapHangNgay(email);
        return NguoiDungConverter.toResponse(existing);
    }

    @Override
    public void updateThongTinNguoiDung(String tenDangNhap, String email) {
        int rows = nguoiDungRepository.updateTenDangNhap(email,tenDangNhap);
    }
    
}
