package com.example.backend.dto.response;

import java.time.LocalDateTime;

public class ChatTongResponse {
    private Long id;

    private String maNguoiGui;
    private String tenDangNhapNguoiGui;

    private String noiDung;
    private LocalDateTime ngayGui;

    private Boolean daThuHoi;

    private Long idTraLoi;
    private String traLoiPreview; // optional: trích 1 đoạn tin nhắn được reply

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaNguoiGui() { return maNguoiGui; }
    public void setMaNguoiGui(String maNguoiGui) { this.maNguoiGui = maNguoiGui; }

    public String getTenDangNhapNguoiGui() { return tenDangNhapNguoiGui; }
    public void setTenDangNhapNguoiGui(String tenDangNhapNguoiGui) { this.tenDangNhapNguoiGui = tenDangNhapNguoiGui; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public LocalDateTime getNgayGui() { return ngayGui; }
    public void setNgayGui(LocalDateTime ngayGui) { this.ngayGui = ngayGui; }

    public Boolean getDaThuHoi() { return daThuHoi; }
    public void setDaThuHoi(Boolean daThuHoi) { this.daThuHoi = daThuHoi; }

    public Long getIdTraLoi() { return idTraLoi; }
    public void setIdTraLoi(Long idTraLoi) { this.idTraLoi = idTraLoi; }

    public String getTraLoiPreview() { return traLoiPreview; }
    public void setTraLoiPreview(String traLoiPreview) { this.traLoiPreview = traLoiPreview; }
}
