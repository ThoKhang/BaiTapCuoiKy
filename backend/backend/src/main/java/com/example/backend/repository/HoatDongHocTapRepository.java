package com.example.backend.repository;

import com.example.backend.entity.HoatDongHocTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface HoatDongHocTapRepository extends JpaRepository<HoatDongHocTap, String> {

    @Query("SELECT h FROM HoatDongHocTap h WHERE h.maMonHoc = ?1 AND h.maLoai = 'LHD02'")
    List<HoatDongHocTap> getCungCoByMonHoc(String maMonHoc);

    // Lấy tất cả bài Củng Cố mà người dùng ĐÃ LÀM
    @Query(value = """
        SELECT
            HD.MaHoatDong,
            HD.TieuDe,
            HD.MoTa,
            HD.TongDiemToiDa,
            TT.SoCauDung,
            TT.SoCauDaLam,
            TT.DiemDatDuoc,
            TT.DaHoanThanh,
            TT.NgayBatDau,
            TT.NgayHoanThanh
        FROM
            HoatDongHocTap HD
        INNER JOIN
            TienTrinhHocTap TT ON HD.MaHoatDong = TT.MaHoatDong
        WHERE
            HD.MaMonHoc = :maMonHoc
            AND HD.MaLoai = 'LHD02'
            AND TT.MaNguoiDung = :maNguoiDung
        ORDER BY
            TT.NgayBatDau DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getCungCoDaLamByMaNguoiDung(
            @Param("maMonHoc") String maMonHoc,
            @Param("maNguoiDung") String maNguoiDung
    );
}