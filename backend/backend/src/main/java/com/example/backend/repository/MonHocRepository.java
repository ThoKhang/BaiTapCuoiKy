package com.example.backend.repository;

import com.example.backend.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonHocRepository extends JpaRepository<MonHoc, String> {
    boolean existsByTenMonHoc(String tenMonHoc);
}
