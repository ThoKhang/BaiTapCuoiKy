package com.example.backend.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LyThuyetNoiDungService {

    @Autowired
    private EntityManager em;

    public Map<String, Object> getNoiDungByHoatDong(String maHoatDong) {
        String sql = """
            SELECT HD.TieuDe, HD.MoTa, HD.TongDiemToiDa
            FROM HoatDongHocTap HD
            WHERE HD.MaHoatDong = :maHoatDong AND HD.MaLoai = 'LHD01'
            """;

        List<Object[]> result = em.createNativeQuery(sql)
                .setParameter("maHoatDong", maHoatDong)
                .getResultList();

        if (result.isEmpty()) {
            return null;
        }

        Object[] row = result.get(0);
        Map<String, Object> data = new HashMap<>();
        data.put("tieuDe", row[0]);
        data.put("moTa", row[1] != null ? row[1] : "Nội dung đang được cập nhật...");
        data.put("tongDiemToiDa", ((Number) row[2]).intValue());

        return data;
    }
}