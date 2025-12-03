/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.service.IService;

import com.example.backend.dto.response.onluyen.MucDoDTO;
import com.example.backend.dto.response.onluyen.ResponseOnLuyenDTO;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public interface IHoatDongHocTapService {
    ResponseOnLuyenDTO layThongTinOnLuyen(String maNguoiDung);
    MucDoDTO taoMucDo(String pattern, Set<String> dsDaLam);
}
