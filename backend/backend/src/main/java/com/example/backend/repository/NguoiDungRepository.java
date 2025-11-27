package com.example.backend.repository;

import com.example.backend.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {
    NguoiDung findByEmail(String email);
    NguoiDung findByTenDangNhap(String tenDangNhap);
    
    // Đếm số người có điểm cao hơn -> hạng = số người có điểm cao hơn + 1
    @Query("SELECT COUNT(n) + 1 FROM NguoiDung n WHERE n.tongDiem > :tongDiem")
    int layHangNguoiDungTheoDiem(@Param("tongDiem") Integer tongDiem);
}

    