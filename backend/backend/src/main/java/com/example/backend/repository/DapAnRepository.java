package com.example.backend.repository;

import com.example.backend.entity.DapAn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DapAnRepository extends JpaRepository<DapAn, String> {
    List<DapAn> findByMaCauHoi(String maCauHoi);
}
