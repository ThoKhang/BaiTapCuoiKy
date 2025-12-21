package com.example.backend.repository;

import com.example.backend.entity.NguoiDung;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {
    NguoiDung findByEmail(String email);
    NguoiDung findByTenDangNhap(String tenDangNhap);
    
    // Đếm số người có điểm cao hơn -> hạng = số người có điểm cao hơn + 1
    @Query("SELECT COUNT(n) + 1 FROM NguoiDung n WHERE n.tongDiem > :tongDiem")
    int layHangNguoiDungTheoDiem(@Param("tongDiem") Integer tongDiem);

    @Query("SELECT n FROM NguoiDung n ORDER BY n.tongDiem DESC")
    List<NguoiDung> topNguoiDungTheoDiem(Pageable pageable);
    //start update : người dùng
    @Modifying
    @Transactional
    @Query("UPDATE NguoiDung n SET n.tenDangNhap = :ten WHERE n.email=:email")
    int updateTenDangNhap(@Param("email") String email,@Param("ten") String tenDangNhap);
    //end update : người dùng
}

    