package com.example.backend.repository;

import com.example.backend.model.LanThuBaiKiemTraNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanThuBaiKiemTraRepository extends JpaRepository<LanThuBaiKiemTraNguoiDung, Integer> {}