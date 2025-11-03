package com.example.backend.repository;

import com.example.backend.model.DapAnCauHoiNguoiDung;
import com.example.backend.model.DapAnKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DapAnRepository extends JpaRepository<DapAnCauHoiNguoiDung, DapAnKey> {}