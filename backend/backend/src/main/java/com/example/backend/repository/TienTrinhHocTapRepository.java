package com.example.backend.repository;

import com.example.backend.entity.TienTrinhHocTap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

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
}

