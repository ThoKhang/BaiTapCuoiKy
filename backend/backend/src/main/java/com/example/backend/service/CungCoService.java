package com.example.backend.service;

import com.example.backend.dto.response.CungCoMonHocResponse;
import com.example.backend.dto.response.TienDoResponse;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CungCoService {

    @Autowired
    private EntityManager em;

    // ======================================================
    // 1) HÀM CŨ: LẤY TIẾN ĐỘ
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
    // 2) HÀM MỚI: LẤY DANH SÁCH BÀI CỦNG CỐ CỦA MỘT MÔN
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
}
