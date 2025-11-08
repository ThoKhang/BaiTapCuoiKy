package com.example.backend.repository;

import com.example.backend.model.LuaChon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LuaChonRepository extends JpaRepository<LuaChon, Integer> {
    List<LuaChon> findByMaCauHoi(Integer maCauHoi);
}