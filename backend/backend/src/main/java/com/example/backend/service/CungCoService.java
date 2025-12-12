package com.example.backend.service;

import com.example.backend.dto.response.CungCoDaLamResponse;
import com.example.backend.dto.response.CungCoMonHocResponse;
import com.example.backend.dto.response.TienDoResponse;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CungCoService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TienTrinhHocTapRepository tienTrinhRepo;

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private HoatDongHocTapRepository hoatDongRepo;
    // ======================================================
    // 1) TIẾN ĐỘ THEO MÔN
    // ======================================================
    public List<TienDoResponse> getTienDo(String maNguoiDung) {

        String tongQuery = """
            SELECT MH.MaMonHoc, MH.TenMonHoc, COUNT(*) AS TongSoBai
            FROM HoatDongHocTap HD
            JOIN MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
            WHERE HD.MaLoai = 'LHD02'
            GROUP BY MH.MaMonHoc, MH.TenMonHoc
        """;

        String daHocQuery = """
            SELECT MH.MaMonHoc, COUNT(*) AS SoDaHoc
            FROM TienTrinhHocTap TT
            JOIN HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
            JOIN MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
            WHERE TT.MaNguoiDung = :ma AND HD.MaLoai = 'LHD02' AND TT.DaHoanThanh = 1
            GROUP BY MH.MaMonHoc
        """;

        List<Object[]> tong = em.createNativeQuery(tongQuery).getResultList();

        List<Object[]> daHoc = em.createNativeQuery(daHocQuery)
                .setParameter("ma", maNguoiDung)
                .getResultList();

        Map<String, Integer> mapDaHoc = new HashMap<>();
        for (Object[] row : daHoc) {
            mapDaHoc.put((String) row[0], ((Number) row[1]).intValue());
        }

        List<TienDoResponse> result = new ArrayList<>();

        for (Object[] row : tong) {
            String maMon = (String) row[0];
            String tenMon = (String) row[1];
            int tongBai = ((Number) row[2]).intValue();
            int da = mapDaHoc.getOrDefault(maMon, 0);

            result.add(new TienDoResponse(maMon, tenMon, da, tongBai));
        }

        return result;
    }

    // ======================================================
    // 2) DANH SÁCH CỦNG CỐ THEO MÔN
    // ======================================================
    public List<CungCoMonHocResponse> getCungCoByMonHoc(String maMonHoc) {

        String sql = """
            SELECT MaHoatDong, TieuDe, MoTa, TongDiemToiDa
            FROM HoatDongHocTap
            WHERE MaMonHoc = :maMon AND MaLoai = 'LHD02'
            ORDER BY MaHoatDong
        """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maMon", maMonHoc)
                .getResultList();

        List<CungCoMonHocResponse> result = new ArrayList<>();

        for (Object[] r : rows) {
            String maHD = (String) r[0];
            String tieuDe = (String) r[1];
            String moTa = (String) r[2];
            int diem = ((Number) r[3]).intValue();

            result.add(new CungCoMonHocResponse(maHD, tieuDe, moTa, diem));
        }

        return result;
    }

    // ======================================================
    // 3) DANH SÁCH CỦNG CỐ (ĐÃ LÀM + CHƯA LÀM)
    // ======================================================
    public List<CungCoDaLamResponse> getCungCoDaLam(String maMonHoc, String maNguoiDung) {

        String sql = """
            SELECT
                HD.MaHoatDong,
                HD.TieuDe,
                HD.MoTa,
                HD.TongDiemToiDa,
                ISNULL(TT.SoCauDung, 0) AS SoCauDung,
                ISNULL(TT.SoCauDaLam, 0) AS SoCauDaLam,
                ISNULL(TT.DiemDatDuoc, 0) AS DiemDatDuoc,
                ISNULL(TT.DaHoanThanh, 0) AS DaHoanThanh,
                TT.NgayBatDau,
                TT.NgayHoanThanh
            FROM
                HoatDongHocTap HD
            LEFT JOIN
                TienTrinhHocTap TT ON HD.MaHoatDong = TT.MaHoatDong 
                AND TT.MaNguoiDung = :maNguoiDung
            WHERE
                HD.MaMonHoc = :maMonHoc
                AND HD.MaLoai = 'LHD02'
            ORDER BY
                ISNULL(TT.DaHoanThanh, 0) ASC,
                HD.MaHoatDong ASC
        """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maMonHoc", maMonHoc)
                .setParameter("maNguoiDung", maNguoiDung)
                .getResultList();

        List<CungCoDaLamResponse> result = new ArrayList<>();

        for (Object[] r : rows) {
            String maHoatDong = (String) r[0];
            String tieuDe = (String) r[1];
            String moTa = (String) r[2];
            int tongDiemToiDa = ((Number) r[3]).intValue();
            int soCauDung = ((Number) r[4]).intValue();
            int soCauDaLam = ((Number) r[5]).intValue();
            int diemDatDuoc = ((Number) r[6]).intValue();

            boolean daHoanThanh = false;
            if (r[7] instanceof Boolean) {
                daHoanThanh = (Boolean) r[7];
            } else if (r[7] instanceof Number) {
                daHoanThanh = ((Number) r[7]).intValue() == 1;
            }

            LocalDateTime ngayBatDau = r[8] != null ? ((java.sql.Timestamp) r[8]).toLocalDateTime() : null;
            LocalDateTime ngayHoanThanh = r[9] != null ? ((java.sql.Timestamp) r[9]).toLocalDateTime() : null;

            result.add(new CungCoDaLamResponse(
                    maHoatDong,
                    tieuDe,
                    moTa,
                    tongDiemToiDa,
                    soCauDung,
                    soCauDaLam,
                    diemDatDuoc,
                    daHoanThanh,
                    ngayBatDau,
                    ngayHoanThanh
            ));
        }

        return result;
    }

    // ===============================
    // 4) HOÀN THÀNH HOẠT ĐỘNG (CỦNG CỐ / THỬ THÁCH)
    // ===============================
    public boolean hoanThanhHoatDong(
            String maNguoiDung,
            String maHoatDong,
            int soCauDung,
            int tongCauHoi,
            int diem) {

        // Tìm bản ghi tiến trình nếu có
        TienTrinhHocTap tt = tienTrinhRepo
                .findByNguoiDung_MaNguoiDungAndHoatDong_MaHoatDong(maNguoiDung, maHoatDong)
                .orElse(null);

        // Nếu chưa có → tạo mới
        if (tt == null) {

            tt = new TienTrinhHocTap();

            // Tạo ID mới
            long count = tienTrinhRepo.count() + 1;
            String id = "TT" + String.format("%03d", count);
            tt.setMaTienTrinh(id);

            tt.setNguoiDung(nguoiDungRepo.findById(maNguoiDung).orElseThrow());
            tt.setHoatDong(hoatDongRepo.findById(maHoatDong).orElseThrow());
            tt.setNgayBatDau(LocalDateTime.now());

            // Lần đầu làm: set luôn
            tt.setSoCauDung(soCauDung);
            tt.setSoCauDaLam(tongCauHoi);
            tt.setDiemDatDuoc(diem);

        } else {
            // Cập nhật số câu cho LẦN CHƠI GẦN NHẤT
            tt.setSoCauDung(soCauDung);
            tt.setSoCauDaLam(tongCauHoi);

            // Giữ ĐIỂM CAO NHẤT
            int diemCu = tt.getDiemDatDuoc();   // kiểu int → không null

            if (diem > diemCu) {
                tt.setDiemDatDuoc(diem);        // chỉ update khi điểm mới cao hơn
            }
            // nếu diem <= diemCu thì giữ nguyên điểm cũ
        }

        tt.setDaHoanThanh(true);
        tt.setNgayHoanThanh(LocalDateTime.now());

        tienTrinhRepo.save(tt);
        return true;
    }

}
