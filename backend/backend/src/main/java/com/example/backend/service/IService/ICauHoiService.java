/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.service.IService;

import com.example.backend.dto.response.DeOnLuyenResponse;

/**
 *
 * @author ADMIN
 */
public interface ICauHoiService {
    DeOnLuyenResponse getDeCoBan(String tieuDe);
    DeOnLuyenResponse getDe();
}
