package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChatTong")
public class ChatTong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    // FK -> NguoiDung.MaNguoiDung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguoiGui", referencedColumnName = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiGui;

    @Column(name = "NoiDung", nullable = false, length = 1000)
    private String noiDung;

    @Column(name = "NgayGui", nullable = false)
    private LocalDateTime ngayGui;

    @Column(name = "DaThuHoi", nullable = false)
    private Boolean daThuHoi;

    // Self FK -> ChatTong.Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTraLoi", referencedColumnName = "Id")
    private ChatTong traLoiTinNhan;

    @PrePersist
    public void prePersist() {
        if (ngayGui == null) ngayGui = LocalDateTime.now();
        if (daThuHoi == null) daThuHoi = false;
    }

    // ===== getters/setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NguoiDung getNguoiGui() { return nguoiGui; }
    public void setNguoiGui(NguoiDung nguoiGui) { this.nguoiGui = nguoiGui; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public LocalDateTime getNgayGui() { return ngayGui; }
    public void setNgayGui(LocalDateTime ngayGui) { this.ngayGui = ngayGui; }

    public Boolean getDaThuHoi() { return daThuHoi; }
    public void setDaThuHoi(Boolean daThuHoi) { this.daThuHoi = daThuHoi; }

    public ChatTong getTraLoiTinNhan() { return traLoiTinNhan; }
    public void setTraLoiTinNhan(ChatTong traLoiTinNhan) { this.traLoiTinNhan = traLoiTinNhan; }
}
