package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private int maNguoiDung;

    @Column(name = "TenDangNhap", nullable = false, unique = true, length = 100)
    private String tenDangNhap;

    @Column(name = "Email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "MatKhauMaHoa", nullable = false, length = 512)
    private String matKhauMaHoa;

    @Column(name = "MaVaiTro", nullable = false)
    private byte maVaiTro;  // TINYINT -> byte

    @Column(name = "NgayTao", insertable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "LanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "SoLanTrucTuyen")
    private int soLanTrucTuyen = 0;

    @Column(name = "TongDiem")
    private int tongDiem = 0;

    @Transient
    private String toanTienDo;

    @Transient
    private String tiengVietTienDo;


    // Constructors
    public NguoiDung() {}

    public NguoiDung(String tenDangNhap, String email, String matKhauMaHoa, byte maVaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.matKhauMaHoa = matKhauMaHoa;
        this.maVaiTro = maVaiTro;
    }

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

    public String getToanTienDo() { return toanTienDo; }
    public void setToanTienDo(String val) { this.toanTienDo = val; }

    public String getTiengVietTienDo() { return tiengVietTienDo; }
    public void setTiengVietTienDo(String val) { this.tiengVietTienDo = val; }


    // toString() để debug
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