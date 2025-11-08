package com.example.backend.repository;

import com.example.backend.model.BaiKiemTra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiKiemTraRepository extends JpaRepository<BaiKiemTra, Integer> {

    List<BaiKiemTra> findByLoaiBaiKiemTraAndMaMonHoc(String loai, Byte maMonHoc);

    long countByLoaiBaiKiemTra(String loaiBaiKiemTra); // ✅ thêm dòng này
}
