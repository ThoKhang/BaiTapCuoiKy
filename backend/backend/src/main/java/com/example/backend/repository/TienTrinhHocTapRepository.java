package com.example.backend.repository;

import com.example.backend.dto.admin.response;
import com.example.backend.entity.TienTrinhHocTap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface TienTrinhHocTapRepository extends JpaRepository<TienTrinhHocTap, String> {

    Optional<TienTrinhHocTap> findByNguoiDung_MaNguoiDungAndHoatDong_MaHoatDong(
            String maNguoiDung,
            String maHoatDong
    );

    // Lấy tất cả tiến trình của 1 người dùng
    List<TienTrinhHocTap> findByNguoiDung_MaNguoiDung(String maNguoiDung);

    //Start : lấy tiến trình cho ôn luyện
    @Query(value = "SELECT COUNT(DISTINCT h.mahoatdong) FROM TienTrinhHocTap t JOIN HoatDongHocTap h ON t.mahoatdong = h.mahoatdong WHERE t.manguoidung = :maNguoiDung AND h.TieuDe LIKE N'Ôn cơ bản%'",nativeQuery = true)
    int soDeCoBanDaLam(String maNguoiDung);
    @Query(value = "SELECT COUNT(DISTINCT h.mahoatdong) FROM TienTrinhHocTap t JOIN HoatDongHocTap h ON t.mahoatdong = h.mahoatdong WHERE t.manguoidung = :maNguoiDung AND h.TieuDe LIKE N'Ôn TB%'",nativeQuery = true)
    int soDeTrungBinhDaLam(String maNguoiDung);

    @Query(value = "SELECT COUNT(DISTINCT h.mahoatdong) FROM TienTrinhHocTap t JOIN HoatDongHocTap h ON t.mahoatdong = h.mahoatdong WHERE t.manguoidung = :maNguoiDung AND h.TieuDe LIKE N'Ôn NC%'",nativeQuery = true)
    int soDeNangCaoDaLam(String maNguoiDung);

    @Query("SELECT t FROM TienTrinhHocTap t WHERE t.nguoiDung.email = :email AND t.hoatDong.maHoatDong = :maHoatDong")
    TienTrinhHocTap findByEmailAndHoatDong(String email, String maHoatDong);

    @Query("SELECT MAX(t.maTienTrinh) FROM TienTrinhHocTap t")
    String findIdLonNhat();

    @Query("SELECT t FROM TienTrinhHocTap t WHERE t.nguoiDung.email = :email AND t.hoatDong.tieuDe LIKE CONCAT(:tieuDe, '%')")
    List<TienTrinhHocTap> findByEmailAndTieuDeHoatDong(String email, String tieuDe);


    //End : lấy tiến trình cho ôn luyện

    @Query("""
        SELECT t.nguoiDung.maNguoiDung, COUNT(t)
        FROM TienTrinhHocTap t
        GROUP BY t.nguoiDung.maNguoiDung
        ORDER BY COUNT(t) DESC
    """)
    List<Object[]> topNguoiDungTheoSoHoatDong();

    // 1️⃣ Hoạt động theo ngày (Line chart)
    @Query(value = """
        SELECT 
            CONVERT(date, NgayBatDau) AS ngay,
            COUNT(*) AS soLuot
        FROM TienTrinhHocTap
        WHERE NgayBatDau BETWEEN :fromDate AND :toDate
        GROUP BY CONVERT(date, NgayBatDau)
        ORDER BY ngay
    """, nativeQuery = true)
    List<response.HoatDongTheoNgayDTO> thongKeTheoNgay(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    // 2️⃣ Theo loại hoạt động (Pie chart)
    @Query(value = """
        SELECT 
            l.TenLoai AS tenLoai,
            COUNT(t.MaTienTrinh) AS soLuot
        FROM TienTrinhHocTap t
        JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
        JOIN LoaiHoatDong l ON h.MaLoai = l.MaLoai
        WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
        GROUP BY l.TenLoai
    """, nativeQuery = true)
    List<response.ThongKeTheoLoaiDTO> thongKeTheoLoai(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    // 3️⃣ Theo môn học (Bar chart)
    @Query(value = """
        SELECT 
            m.TenMonHoc AS tenMonHoc,
            COUNT(*) AS soLuot
        FROM TienTrinhHocTap t
        JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
        JOIN MonHoc m ON h.MaMonHoc = m.MaMonHoc
        WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
        GROUP BY m.TenMonHoc
    """, nativeQuery = true)
    List<response.ThongKeTheoMonDTO> thongKeTheoMon(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    @Query(value = """
    SELECT
        COUNT(*) AS tongLuotThamGia,
        COUNT(DISTINCT MaNguoiDung) AS soNguoiDungThamGia,
        SUM(CASE WHEN DaHoanThanh = 1 THEN 1 ELSE 0 END) AS soHoanThanh,
        CAST(
            SUM(CASE WHEN DaHoanThanh = 1 THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(*),0)
        AS DECIMAL(5,2)) AS tyLeHoanThanh,
        CAST(AVG(CASE WHEN NgayHoanThanh IS NOT NULL THEN DATEDIFF(MINUTE, NgayBatDau, NgayHoanThanh) END)
        AS DECIMAL(10,2)) AS thoiGianTBPhut,
        SUM(DiemDatDuoc) AS tongDiem
    FROM TienTrinhHocTap
    WHERE NgayBatDau BETWEEN :fromDate AND :toDate
""", nativeQuery = true)
    response.AnalyticsOverviewDTO overview(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    @Query(value = """
    SELECT TOP (:limit)
        h.MaHoatDong AS maHoatDong,
        h.TieuDe AS tieuDe,
        l.TenLoai AS tenLoai,
        m.TenMonHoc AS tenMonHoc,
        COUNT(t.MaTienTrinh) AS soLuot
    FROM TienTrinhHocTap t
    JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
    JOIN LoaiHoatDong l ON h.MaLoai = l.MaLoai
    JOIN MonHoc m ON h.MaMonHoc = m.MaMonHoc
    WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
    GROUP BY h.MaHoatDong, h.TieuDe, l.TenLoai, m.TenMonHoc
    ORDER BY soLuot DESC
""", nativeQuery = true)
    List<response.ActivityPopularityDTO> topActivities(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("limit") int limit
    );
    @Query(value = """
    SELECT TOP (:limit)
        h.MaHoatDong AS maHoatDong,
        h.TieuDe AS tieuDe,
        l.TenLoai AS tenLoai,
        m.TenMonHoc AS tenMonHoc,
        COUNT(t.MaTienTrinh) AS soLuot
    FROM HoatDongHocTap h
    LEFT JOIN TienTrinhHocTap t 
        ON h.MaHoatDong = t.MaHoatDong
        AND t.NgayBatDau BETWEEN :fromDate AND :toDate
    JOIN LoaiHoatDong l ON h.MaLoai = l.MaLoai
    JOIN MonHoc m ON h.MaMonHoc = m.MaMonHoc
    GROUP BY h.MaHoatDong, h.TieuDe, l.TenLoai, m.TenMonHoc
    ORDER BY soLuot ASC
""", nativeQuery = true)
    List<response.ActivityPopularityDTO> bottomActivities(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("limit") int limit
    );
    @Query(value = """
    SELECT
        h.MaHoatDong AS maHoatDong,
        h.TieuDe AS tieuDe,
        COUNT(*) AS tongLuot,
        SUM(CASE WHEN t.DaHoanThanh = 1 THEN 1 ELSE 0 END) AS hoanThanh,
        CAST(
            SUM(CASE WHEN t.DaHoanThanh = 1 THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(*),0)
        AS DECIMAL(5,2)) AS tyLeHoanThanh
    FROM TienTrinhHocTap t
    JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
    WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
    GROUP BY h.MaHoatDong, h.TieuDe
""", nativeQuery = true)
    List<response.ActivityCompletionDTO> completionByActivity(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    @Query(value = """
    SELECT
        h.MaHoatDong AS maHoatDong,
        h.TieuDe AS tieuDe,
        CAST(AVG(DATEDIFF(MINUTE, t.NgayBatDau, t.NgayHoanThanh)) AS DECIMAL(10,2)) AS thoiGianTBPhut
    FROM TienTrinhHocTap t
    JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
    WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
      AND t.NgayHoanThanh IS NOT NULL
    GROUP BY h.MaHoatDong, h.TieuDe
""", nativeQuery = true)
    List<response.ActivityAvgTimeDTO> avgTimeByActivity(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    @Query(value = """
    SELECT
        m.MaMonHoc AS maMonHoc,
        m.TenMonHoc AS tenMonHoc,
        COUNT(*) AS soLuot
    FROM TienTrinhHocTap t
    JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
    JOIN MonHoc m ON h.MaMonHoc = m.MaMonHoc
    WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
    GROUP BY m.MaMonHoc, m.TenMonHoc
    ORDER BY soLuot DESC
""", nativeQuery = true)
    List<response.SubjectPopularityDTO> subjectPopularity(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    @Query(value = """
    SELECT TOP (:limit)
        nd.MaNguoiDung AS maNguoiDung,
        nd.TenDangNhap AS tenDangNhap,
        COUNT(t.MaTienTrinh) AS soLuot,
        SUM(CASE WHEN t.DaHoanThanh = 1 THEN 1 ELSE 0 END) AS soHoanThanh,
        SUM(t.DiemDatDuoc) AS tongDiem
    FROM TienTrinhHocTap t
    JOIN NguoiDung nd ON t.MaNguoiDung = nd.MaNguoiDung
    WHERE t.NgayBatDau BETWEEN :fromDate AND :toDate
    GROUP BY nd.MaNguoiDung, nd.TenDangNhap
    ORDER BY soLuot DESC
""", nativeQuery = true)
    List<response.TopStudentDTO> topStudents(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("limit") int limit
    );

}

