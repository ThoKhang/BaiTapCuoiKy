package com.example.backend.repository;

import com.example.backend.entity.NguoiDungVaiTro;
import com.example.backend.entity.NguoiDungVaiTroId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NguoiDungVaiTroRepository
        extends JpaRepository<NguoiDungVaiTro, NguoiDungVaiTroId> {

    List<NguoiDungVaiTro> findByMaNguoiDung(String maNguoiDung);
}
