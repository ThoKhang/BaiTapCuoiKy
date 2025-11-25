package com.example.backend.repository;

import com.example.backend.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {
    NguoiDung findByEmail(String email);
    NguoiDung findByTenDangNhap(String tenDangNhap);
}
