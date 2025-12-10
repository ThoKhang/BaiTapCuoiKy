/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.service.IService;

import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.dto.response.TienTrinhResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface ITienTrinhService {
    void taoTienTrinh(TienTrinhRequest request);
    List<TienTrinhResponse> getCauDungAndDaLam(String email,String maHoatDong);
}
