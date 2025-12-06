package com.example.apphoctapchotre.DATA.model.ui;

public class DeItem {
    private String tieuDe;
    private int diemThuong;
    private int soCauDung;
    private int tongCau;

    public DeItem(String tieuDe, int diemThuong, int soCauDung, int tongCau) {
        this.tieuDe = tieuDe;
        this.diemThuong = diemThuong;
        this.soCauDung = soCauDung;
        this.tongCau = tongCau;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public int getDiemThuong() {
        return diemThuong;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public int getTongCau() {
        return tongCau;
    }
}
