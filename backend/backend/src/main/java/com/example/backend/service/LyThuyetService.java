package com.example.backend.service;

import com.example.backend.dto.response.LyThuyetDaLamResponse;
import com.example.backend.dto.response.LyThuyetMonHocResponse;
import com.example.backend.dto.response.TienDoLyThuyetResponse;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LyThuyetService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TienTrinhHocTapRepository tienTrinhRepo;

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private HoatDongHocTapRepository hoatDongRepo;

    // ======================= GET TIẾN ĐỘ =======================
    public List<TienDoLyThuyetResponse> getTienDo(String maNguoiDung) {
        String tongQuery = """
                SELECT MH.MaMonHoc, MH.TenMonHoc, COUNT(*) AS TongSoBai
                FROM HoatDongHocTap HD
                JOIN MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
                WHERE HD.MaLoai = 'LHD01'
                GROUP BY MH.MaMonHoc, MH.TenMonHoc
                """;
        String daHocQuery = """
                SELECT MH.MaMonHoc, COUNT(*) AS SoDaHoc
                FROM TienTrinhHocTap TT
                JOIN HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
                JOIN MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
                WHERE TT.MaNguoiDung = :ma AND HD.MaLoai = 'LHD01' AND TT.DaHoanThanh = 1
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

        List<TienDoLyThuyetResponse> result = new ArrayList<>();
        for (Object[] row : tong) {
            String maMon = (String) row[0];
            String tenMon = (String) row[1];
            int tongBai = ((Number) row[2]).intValue();
            int da = mapDaHoc.getOrDefault(maMon, 0);
            result.add(new TienDoLyThuyetResponse(maMon, tenMon, da, tongBai));
        }
        return result;
    }

    // ======================= GET LÝ THUYẾT THEO MÔN =======================
    public List<LyThuyetMonHocResponse> getLyThuyetByMonHoc(String maMonHoc) {
        String sql = """
                SELECT MaHoatDong, TieuDe, MoTa, TongDiemToiDa
                FROM HoatDongHocTap
                WHERE MaMonHoc = :maMon AND MaLoai = 'LHD01'
                ORDER BY MaHoatDong
                """;
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maMon", maMonHoc)
                .getResultList();

        List<LyThuyetMonHocResponse> result = new ArrayList<>();
        for (Object[] r : rows) {
            String maHD = (String) r[0];
            String tieuDe = (String) r[1];
            String moTa = (String) r[2];
            int diem = ((Number) r[3]).intValue();
            result.add(new LyThuyetMonHocResponse(maHD, tieuDe, moTa, diem));
        }
        return result;
    }

    // ======================= GET LÝ THUYẾT ĐÃ LÀM (CLEAN) =======================
    public List<LyThuyetDaLamResponse> getLyThuyetDaLam(String maMonHoc, String maNguoiDung) {

        String sql = """
                SELECT HD.MaHoatDong, HD.TieuDe, HD.MoTa, HD.TongDiemToiDa,
                       ISNULL(TT.DaHoanThanh, 0) AS DaHoanThanh
                FROM HoatDongHocTap HD
                LEFT JOIN TienTrinhHocTap TT 
                       ON HD.MaHoatDong = TT.MaHoatDong 
                      AND TT.MaNguoiDung = :maNguoiDung
                WHERE HD.MaMonHoc = :maMonHoc 
                  AND HD.MaLoai = 'LHD01'
                ORDER BY ISNULL(TT.DaHoanThanh, 0) ASC, HD.MaHoatDong ASC
                """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maMonHoc", maMonHoc)
                .setParameter("maNguoiDung", maNguoiDung)
                .getResultList();

        List<LyThuyetDaLamResponse> result = new ArrayList<>();
        for (Object[] r : rows) {

            String maHoatDong = (String) r[0];
            String tieuDe = (String) r[1];
            String moTa = (String) r[2];
            int tongDiemToiDa = ((Number) r[3]).intValue();

            boolean daHoanThanh;
            if (r[4] instanceof Boolean) {
                daHoanThanh = (Boolean) r[4];
            } else {
                daHoanThanh = ((Number) r[4]).intValue() == 1;
            }

            result.add(new LyThuyetDaLamResponse(
                    maHoatDong, tieuDe, moTa, tongDiemToiDa, daHoanThanh
            ));
        }
        return result;
    }

    // ======================= HOÀN THÀNH HOẠT ĐỘNG =======================
    public boolean hoanThanhHoatDong(String maNguoiDung, String maHoatDong, int diem) {

        TienTrinhHocTap tt = tienTrinhRepo
                .findByNguoiDung_MaNguoiDungAndHoatDong_MaHoatDong(maNguoiDung, maHoatDong)
                .orElse(null);

        if (tt == null) {
            tt = new TienTrinhHocTap();
            long count = tienTrinhRepo.count() + 1;
            String id = "TT" + String.format("%03d", count);

            tt.setMaTienTrinh(id);
            tt.setNguoiDung(nguoiDungRepo.findById(maNguoiDung).orElseThrow());
            tt.setHoatDong(hoatDongRepo.findById(maHoatDong).orElseThrow());
            tt.setNgayBatDau(LocalDateTime.now());
            tt.setSoCauDung(0);
            tt.setSoCauDaLam(0);
        }

        tt.setDiemDatDuoc(diem);
        tt.setDaHoanThanh(true);
        tt.setNgayHoanThanh(LocalDateTime.now());

        tienTrinhRepo.save(tt);
        return true;
    }
}
