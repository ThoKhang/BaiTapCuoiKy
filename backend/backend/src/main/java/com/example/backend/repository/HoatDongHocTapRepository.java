package com.example.backend.repository;

import com.example.backend.entity.HoatDongHocTap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HoatDongHocTapRepository extends JpaRepository<HoatDongHocTap, String> {
    List<HoatDongHocTap> findByMaMonHocAndMaLoai(String maMonHoc, String maLoai);
}
