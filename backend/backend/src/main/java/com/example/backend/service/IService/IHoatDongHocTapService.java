/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.service.IService;

import com.example.backend.dto.response.onluyen.DeDTO;
import com.example.backend.dto.response.onluyen.OnLuyenLoaiResponse;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public interface IHoatDongHocTapService {

    OnLuyenLoaiResponse getTongQuan(String maNguoiDung);

    List<DeDTO> getDanhSachDe(String loai, String maNguoiDung);

//    ChiTietDeDTO getChiTietDe(String maHoatDong);
}
