package com.example.backend.repository;

import com.example.backend.model.BaiKiemTraCauHoi;
import com.example.backend.model.BaiKiemTraCauHoiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiKiemTraCauHoiRepository extends JpaRepository<BaiKiemTraCauHoi, BaiKiemTraCauHoiKey> {
    List<BaiKiemTraCauHoi> findByKeyMaBaiKiemTra(Integer maBaiKiemTra);
}