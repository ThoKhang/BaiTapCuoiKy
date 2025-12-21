package com.example.backend.repository;

import com.example.backend.entity.HoatDongCauHoi;
import com.example.backend.entity.HoatDongCauHoiId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoatDongCauHoiRepository
        extends JpaRepository<HoatDongCauHoi, HoatDongCauHoiId> {
}
