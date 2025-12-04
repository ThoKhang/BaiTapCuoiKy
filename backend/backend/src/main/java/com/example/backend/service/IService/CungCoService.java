package com.example.backend.service.IService;

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
}
