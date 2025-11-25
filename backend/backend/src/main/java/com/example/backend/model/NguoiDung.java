package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {

    @Id
    @Column(name = "MaNguoiDung", length = 5)
    private String maNguoiDung;   // CHAR(5)

    @Column(name = "TenDangNhap", nullable = false, unique = true, length = 100)
    private String tenDangNhap;

    @Column(name = "Email", unique = true, length = 255)
    private String email;

    @Column(name = "MatKhauMaHoa", nullable = false, length = 512)
    private String matKhauMaHoa;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "LanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "SoLanTrucTuyen")
    private Integer soLanTrucTuyen = 0;

    @Column(name = "TongDiem")
    private Integer tongDiem = 0;

    public NguoiDung() {}

    // Getter & Setter
    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhauMaHoa() { return matKhauMaHoa; }
    public void setMatKhauMaHoa(String matKhauMaHoa) { this.matKhauMaHoa = matKhauMaHoa; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    public void setLanDangNhapCuoi(LocalDateTime lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }

    public Integer getSoLanTrucTuyen() { return soLanTrucTuyen; }
    public void setSoLanTrucTuyen(Integer soLanTrucTuyen) { this.soLanTrucTuyen = soLanTrucTuyen; }

    public Integer getTongDiem() { return tongDiem; }
    public void setTongDiem(Integer tongDiem) { this.tongDiem = tongDiem; }
}
