package com.example.apphoctapchotre.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class NguoiDung {
    @SerializedName("maNguoiDung")
    private int maNguoiDung;

    @SerializedName("tenDangNhap")
    private String tenDangNhap;

    @SerializedName("email")
    private String email;

    @SerializedName("matKhauMaHoa")
    private String matKhauMaHoa;

    @SerializedName("maVaiTro")
    private byte maVaiTro;

    @SerializedName("ngayTao")
    private LocalDateTime ngayTao;

    @SerializedName("lanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @SerializedName("soLanTrucTuyen")
    private int soLanTrucTuyen;

    @SerializedName("tongDiem")
    private int tongDiem;

    private String toanTienDo;
    private String tiengVietTienDo;

    // Constructors
    public NguoiDung() {}

    // Getters & Setters
    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhauMaHoa() { return matKhauMaHoa; }
    public void setMatKhauMaHoa(String matKhauMaHoa) { this.matKhauMaHoa = matKhauMaHoa; }

    public byte getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(byte maVaiTro) { this.maVaiTro = maVaiTro; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    public void setLanDangNhapCuoi(LocalDateTime lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }

    public int getSoLanTrucTuyen() { return soLanTrucTuyen; }
    public void setSoLanTrucTuyen(int soLanTrucTuyen) { this.soLanTrucTuyen = soLanTrucTuyen; }

    public int getTongDiem() { return tongDiem; }
    public void setTongDiem(int tongDiem) { this.tongDiem = tongDiem; }

    public String getToanTienDo() {
        return toanTienDo;
    }

    public void setToanTienDo(String toanTienDo) {
        this.toanTienDo = toanTienDo;
    }

    public String getTiengVietTienDo() {
        return tiengVietTienDo;
    }

    public void setTiengVietTienDo(String tiengVietTienDo) {
        this.tiengVietTienDo = tiengVietTienDo;
    }


    @Override
    public String toString() {
        return "NguoiDung{" +
                "maNguoiDung=" + maNguoiDung +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", email='" + email + '\'' +
                ", maVaiTro=" + maVaiTro +
                ", ngayTao=" + ngayTao +
                ", lanDangNhapCuoi=" + lanDangNhapCuoi +
                ", tongDiem=" + tongDiem +
                '}';
    }

}
