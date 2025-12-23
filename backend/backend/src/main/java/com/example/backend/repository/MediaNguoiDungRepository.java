package com.example.backend.repository;

import com.example.backend.entity.MediaNguoiDung;
import com.example.backend.entity.MediaNguoiDungId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaNguoiDungRepository extends JpaRepository<MediaNguoiDung, MediaNguoiDungId> {
    Optional<MediaNguoiDung> findById_MaMediaAndId_MaNguoiDung(Long maMedia, String maNguoiDung);
}
