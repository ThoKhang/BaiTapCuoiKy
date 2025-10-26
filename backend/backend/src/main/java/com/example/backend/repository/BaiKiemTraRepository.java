/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

import com.example.backend.repository.entity.BaiKiemTraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public interface BaiKiemTraRepository extends JpaRepository<BaiKiemTraEntity, Integer>{
    int countByLoaiBaiKiemTra(String loaiBaiKiemTra);
}
