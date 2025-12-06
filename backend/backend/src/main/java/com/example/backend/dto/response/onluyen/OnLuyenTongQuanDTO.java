/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response.onluyen;

/**
 *
 * @author ADMIN
 */
public class OnLuyenTongQuanDTO {
    public int tongDe;
    public int tongDaLam;

    public OnLuyenTongQuanDTO() {}

    public OnLuyenTongQuanDTO(int tongDe, int tongDaLam) {
        this.tongDe = tongDe;
        this.tongDaLam = tongDaLam;
    }

    public int getTongDe() {
        return tongDe;
    }

    public void setTongDe(int tongDe) {
        this.tongDe = tongDe;
    }

    public int getTongDaLam() {
        return tongDaLam;
    }

    public void setTongDaLam(int tongDaLam) {
        this.tongDaLam = tongDaLam;
    }
    
}
