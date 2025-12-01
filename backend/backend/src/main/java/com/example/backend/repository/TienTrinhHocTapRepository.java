package com.example.backend.repository;

import com.example.backend.entity.TienTrinhHocTap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TienTrinhHocTapRepository extends JpaRepository<TienTrinhHocTap, String> {
    boolean existsByNguoiDung_EmailAndHoatDong_MaHoatDongAndDaHoanThanhTrue(String email, String maHoatDong);
}