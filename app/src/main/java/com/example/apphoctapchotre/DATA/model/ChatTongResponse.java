package com.example.apphoctapchotre.DATA.model;

public class ChatTongResponse {

    private Long id;
    private String maNguoiGui;
    private String tenDangNhapNguoiGui;
    private String noiDung;
    private String ngayGui;
    private Boolean daThuHoi;
    private Long idTraLoi;
    private String traLoiPreview;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaNguoiGui() { return maNguoiGui; }
    public void setMaNguoiGui(String maNguoiGui) { this.maNguoiGui = maNguoiGui; }

    public String getTenDangNhapNguoiGui() { return tenDangNhapNguoiGui; }
    public void setTenDangNhapNguoiGui(String tenDangNhapNguoiGui) {
        this.tenDangNhapNguoiGui = tenDangNhapNguoiGui;
    }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayGui() { return ngayGui; }
    public void setNgayGui(String ngayGui) { this.ngayGui = ngayGui; }

    public Boolean getDaThuHoi() { return daThuHoi; }
    public void setDaThuHoi(Boolean daThuHoi) { this.daThuHoi = daThuHoi; }

    public Long getIdTraLoi() { return idTraLoi; }
    public void setIdTraLoi(Long idTraLoi) { this.idTraLoi = idTraLoi; }

    public String getTraLoiPreview() { return traLoiPreview; }
    public void setTraLoiPreview(String traLoiPreview) {
        this.traLoiPreview = traLoiPreview;
    }
}
