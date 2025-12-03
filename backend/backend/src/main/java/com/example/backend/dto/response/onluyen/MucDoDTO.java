/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response.onluyen;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class MucDoDTO {
    public int tongDe;
    public int tongDeDaLam;
    public List<DeDTO> danhSach;

    public MucDoDTO() {
    }

    public MucDoDTO(int tongDe, int tongDeDaLam, List<DeDTO> danhSach) {
        this.tongDe = tongDe;
        this.tongDeDaLam = tongDeDaLam;
        this.danhSach = danhSach;
    }

    public int getTongDe() {
        return tongDe;
    }

    public void setTongDe(int tongDe) {
        this.tongDe = tongDe;
    }

    public int getTongDeDaLam() {
        return tongDeDaLam;
    }

    public void setTongDeDaLam(int tongDeDaLam) {
        this.tongDeDaLam = tongDeDaLam;
    }

    public List<DeDTO> getDanhSach() {
        return danhSach;
    }

    public void setDanhSach(List<DeDTO> danhSach) {
        this.danhSach = danhSach;
    }
    
}

