package com.example.apphoctapchotre.DATA.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class NguoiDung {

    @SerializedName("maNguoiDung")
    private String maNguoiDung;   // Đổi từ int -> String

    @SerializedName("tenDangNhap")
    private String tenDangNhap;

    @SerializedName("email")
    private String email;

    @SerializedName("matKhauMaHoa")
    private String matKhauMaHoa;

    // XÓA maVaiTro vì backend không trả trường này nữa

    @SerializedName("ngayTao")
    private LocalDateTime ngayTao;

    @SerializedName("lanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @SerializedName("soLanTrucTuyen")
    private int soLanTrucTuyen;

    @SerializedName("tongDiem")
    private int tongDiem;

    public NguoiDung() {
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhauMaHoa() {
        return matKhauMaHoa;
    }

    public void setMatKhauMaHoa(String matKhauMaHoa) {
        this.matKhauMaHoa = matKhauMaHoa;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}