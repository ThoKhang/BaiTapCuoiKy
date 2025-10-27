package com.example.apphoctapchotre.model;

import com.google.gson.annotations.SerializedName;

public class BaiKiemTra {

    // --- Thống kê số bài (nếu có API khác dùng) ---
    @SerializedName("toan")
    private int toan;

    @SerializedName("tiengViet")
    private int tiengViet;

    public int getToan() { return toan; }
    public void setToan(int toan) { this.toan = toan; }

    public int getTiengViet() { return tiengViet; }
    public void setTiengViet(int tiengViet) { this.tiengViet = tiengViet; }

    // --- Thông tin bài kiểm tra ---
    @SerializedName("MaBaiKiemTra")
    private int maBaiKiemTra;

    @SerializedName("TenBaiKiemTra")
    private String tenBaiKiemTra;

    @SerializedName("LoaiBaiKiemTra")
    private String loaiBaiKiemTra;

    @SerializedName("MaMonHoc")
    private int maMonHoc;

    @SerializedName("TenMonHoc")
    private String tenMonHoc;

    @SerializedName("CoXaoTron")
    private boolean coXaoTron;

    @SerializedName("TongSoCauHoi")
    private int tongSoCauHoi;

    @SerializedName("NgayTao")
    private String ngayTao;

    // 🔹 Thêm annotation để Gson nhận đúng từ backend
    @SerializedName("MaTieuDePhu")
    private int maTieuDePhu;

    @SerializedName("TieuDePhu")
    private String tieuDePhu;

    private boolean daHoanThanh = false;

    public int getMaTieuDePhu() {
        return maTieuDePhu;
    }

    public void setMaTieuDePhu(int maTieuDePhu) {
        this.maTieuDePhu = maTieuDePhu;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

    // --- Getter & Setter ---
    public int getMaBaiKiemTra() { return maBaiKiemTra; }
    public void setMaBaiKiemTra(int maBaiKiemTra) { this.maBaiKiemTra = maBaiKiemTra; }

    public String getTenBaiKiemTra() { return tenBaiKiemTra; }
    public void setTenBaiKiemTra(String tenBaiKiemTra) { this.tenBaiKiemTra = tenBaiKiemTra; }

    public String getLoaiBaiKiemTra() { return loaiBaiKiemTra; }
    public void setLoaiBaiKiemTra(String loaiBaiKiemTra) { this.loaiBaiKiemTra = loaiBaiKiemTra; }

    public int getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(int maMonHoc) { this.maMonHoc = maMonHoc; }

    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    public boolean isCoXaoTron() { return coXaoTron; }
    public void setCoXaoTron(boolean coXaoTron) { this.coXaoTron = coXaoTron; }

    public int getTongSoCauHoi() { return tongSoCauHoi; }
    public void setTongSoCauHoi(int tongSoCauHoi) { this.tongSoCauHoi = tongSoCauHoi; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public String getTieuDePhu() { return tieuDePhu; }
    public void setTieuDePhu(String tieuDePhu) { this.tieuDePhu = tieuDePhu; }

    @Override
    public String toString() {
        return "BaiKiemTra{" +
                "maBaiKiemTra=" + maBaiKiemTra +
                ", tenBaiKiemTra='" + tenBaiKiemTra + '\'' +
                ", tieuDePhu='" + tieuDePhu + '\'' +
                ", maMonHoc=" + maMonHoc +
                ", tongSoCauHoi=" + tongSoCauHoi +
                '}';
    }
}
