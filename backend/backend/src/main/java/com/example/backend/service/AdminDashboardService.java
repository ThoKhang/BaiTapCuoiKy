package com.example.backend.service;

import com.example.backend.dto.admin.DashboardResponse;
import com.example.backend.entity.MonHoc;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardService {

    private final NguoiDungRepository nguoiDungRepo;
    private final MonHocRepository monHocRepo;
    private final HoatDongHocTapRepository hoatDongRepo;
    private final CauHoiRepository cauHoiRepo;
    private final TienTrinhHocTapRepository tienTrinhRepo;

    public AdminDashboardService(NguoiDungRepository nguoiDungRepo,
                                 MonHocRepository monHocRepo,
                                 HoatDongHocTapRepository hoatDongRepo,
                                 CauHoiRepository cauHoiRepo,
                                 TienTrinhHocTapRepository tienTrinhRepo) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.monHocRepo = monHocRepo;
        this.hoatDongRepo = hoatDongRepo;
        this.cauHoiRepo = cauHoiRepo;
        this.tienTrinhRepo = tienTrinhRepo;
    }

    public DashboardResponse getDashboard() {

        DashboardResponse res = new DashboardResponse();

        // ===== Tổng =====
        res.setTongHocSinh(nguoiDungRepo.count());
        res.setTongMonHoc(monHocRepo.count());
        res.setTongHoatDong(hoatDongRepo.count());
        res.setTongCauHoi(cauHoiRepo.count());
        res.setTongLuotLamBai(tienTrinhRepo.count());

        // ===== Top học sinh theo điểm =====
        List<NguoiDung> top = nguoiDungRepo.topNguoiDungTheoDiem(PageRequest.of(0, 10));
        List<DashboardResponse.TopHocSinh> topDiem = new ArrayList<>();

        for (NguoiDung n : top) {
            DashboardResponse.TopHocSinh item = new DashboardResponse.TopHocSinh();
            item.setMaNguoiDung(n.getMaNguoiDung());
            item.setTenDangNhap(n.getTenDangNhap());
            item.setTongDiem(n.getTongDiem());
            topDiem.add(item);
        }
        res.setTopHocSinhTheoDiem(topDiem);

        // ===== Top học sinh theo số hoạt động đã làm =====
        // Query trả: Object[] { String maNguoiDung, Long count }
        List<Object[]> topHoatDongRaw = tienTrinhRepo.topNguoiDungTheoSoHoatDong();
        List<DashboardResponse.TopHocSinhTheoHoatDong> topHD = new ArrayList<>();

        int limit = Math.min(10, topHoatDongRaw.size());
        for (int i = 0; i < limit; i++) {
            Object[] row = topHoatDongRaw.get(i);

            String maNguoiDung = (String) row[0];
            Long count = (Long) row[1];

            NguoiDung n = nguoiDungRepo.findById(maNguoiDung).orElse(null);
            if (n == null) continue;

            DashboardResponse.TopHocSinhTheoHoatDong item =
                    new DashboardResponse.TopHocSinhTheoHoatDong();

            item.setMaNguoiDung(maNguoiDung);
            item.setTenDangNhap(n.getTenDangNhap());
            item.setSoHoatDongDaLam(count);

            topHD.add(item);
        }
        res.setTopHocSinhTheoSoHoatDong(topHD);

        // ===== Thống kê số hoạt động theo môn =====
        Map<String, String> mapTenMon = new HashMap<>();
        for (MonHoc m : monHocRepo.findAll()) {
            mapTenMon.put(m.getMaMonHoc(), m.getTenMonHoc());
        }

        List<Object[]> thongKeRaw = hoatDongRepo.thongKeSoHoatDongTheoMon();
        List<DashboardResponse.ThongKeMonHoc> tkList = new ArrayList<>();

        for (Object[] row : thongKeRaw) {
            String maMon = (String) row[0];
            Long soHoatDong = (Long) row[1];

            DashboardResponse.ThongKeMonHoc item =
                    new DashboardResponse.ThongKeMonHoc();

            item.setMaMonHoc(maMon);
            item.setTenMonHoc(mapTenMon.getOrDefault(maMon, maMon));
            item.setSoHoatDong(soHoatDong);

            tkList.add(item);
        }

        res.setThongKeTheoMonHoc(tkList);

        return res;
    }
}
