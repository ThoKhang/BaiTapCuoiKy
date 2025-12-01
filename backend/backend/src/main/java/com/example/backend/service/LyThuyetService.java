// 3. Cập nhật LyThuyetService.java (chỉ thêm 1 method mới)
package com.example.backend.service;

import com.example.backend.dto.request.HoanThanhLyThuyetRequest;
import com.example.backend.dto.response.LyThuyetDetailResponse;
import com.example.backend.dto.response.LyThuyetItemResponse;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.IService.ILyThuyetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LyThuyetService implements ILyThuyetService {

    private final HoatDongHocTapRepository hoatDongRepo;
    private final TienTrinhHocTapRepository tienTrinhRepo;
    private final NguoiDungRepository nguoiDungRepo;

    public LyThuyetService(HoatDongHocTapRepository hoatDongRepo,
                           TienTrinhHocTapRepository tienTrinhRepo,
                           NguoiDungRepository nguoiDungRepo) {
        this.hoatDongRepo = hoatDongRepo;
        this.tienTrinhRepo = tienTrinhRepo;
        this.nguoiDungRepo = nguoiDungRepo;
    }

    @Override
    public List<LyThuyetItemResponse> layDanhSachTheoMon(String email, String maMonHoc) {
        return hoatDongRepo.findByMaMonHocAndMaLoai(maMonHoc, "LHD01")
                .stream()
                .map(hd -> new LyThuyetItemResponse(
                        hd.getMaHoatDong(),
                        hd.getTieuDe(),
                        hd.getMoTa(),
                        hd.getTongDiemToiDa() != null ? hd.getTongDiemToiDa() : 50,
                        tienTrinhRepo.existsByNguoiDung_EmailAndHoatDong_MaHoatDongAndDaHoanThanhTrue(email, hd.getMaHoatDong())
                ))
                .collect(Collectors.toList());
    }

    // ← THÊM MỚI: Chi tiết bài học
    @Override
    public LyThuyetDetailResponse layChiTiet(String email, String maHoatDong) {
        HoatDongHocTap hd = hoatDongRepo.findById(maHoatDong)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học"));

        boolean daHoanThanh = tienTrinhRepo
                .existsByNguoiDung_EmailAndHoatDong_MaHoatDongAndDaHoanThanhTrue(email, maHoatDong);

        String noiDung = hd.getMoTa() != null && !hd.getMoTa().trim().isEmpty()
                ? hd.getMoTa()
                : "Chưa có nội dung bài học.";

        return new LyThuyetDetailResponse(hd.getTieuDe(), noiDung, daHoanThanh);
    }

    @Override
    public String hoanThanh(HoanThanhLyThuyetRequest req) {
        boolean daHoanThanh = tienTrinhRepo
                .existsByNguoiDung_EmailAndHoatDong_MaHoatDongAndDaHoanThanhTrue(req.getEmail(), req.getMaHoatDong());

        if (daHoanThanh) {
            throw new RuntimeException("Đã hoàn thành rồi!");
        }

        NguoiDung user = nguoiDungRepo.findByEmail(req.getEmail());
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        HoatDongHocTap hd = hoatDongRepo.findById(req.getMaHoatDong())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học"));

        TienTrinhHocTap tt = new TienTrinhHocTap();
        tt.setMaTienTrinh("TT" + System.currentTimeMillis());
        tt.setNguoiDung(user);
        tt.setHoatDong(hd);
        tt.setDaHoanThanh(true);
        tt.setDiemDatDuoc(10);
        tt.setNgayBatDau(LocalDateTime.now());
        tt.setNgayHoanThanh(LocalDateTime.now());
        tienTrinhRepo.save(tt);

        user.setTongDiem(user.getTongDiem() + 10);
        nguoiDungRepo.save(user);

        return "Hoàn thành! +10 điểm";
    }
}