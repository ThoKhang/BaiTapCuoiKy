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
    
    // Lấy ôn luyện
    @Query(value = "SELECT COUNT(*) FROM HoatDongHocTap WHERE TieuDe LIKE N'Ôn cơ bản%'", nativeQuery = true)
    int tongSoDeCoBan();
    @Query(value = "SELECT COUNT(*) FROM HoatDongHocTap WHERE TieuDe LIKE N'Ôn TB%'", nativeQuery = true)
    int tongSoDeTrungBinh();
    @Query(value = "SELECT COUNT(*) FROM HoatDongHocTap WHERE TieuDe LIKE N'Ôn NC%'", nativeQuery = true)
    int tongSoDeNangCao();
    //End Lấy ôn luyện
    
    // start lấy tiêu đề toán tiếng việt giải trí
    @Query(value = """
            select distinct h.tieude,mh.tenmonhoc,l.tenloai
            from hoatdonghoctap h
            join monhoc mh on mh.MaMonHoc = h.MaMonHoc
            join loaihoatdong l on l.maloai = h.MaLoai
        """,nativeQuery = true)
    List<Object[]> toanTVGTri();
    // end lấy tiêu đề toán tiếng việt giải trí
    
}