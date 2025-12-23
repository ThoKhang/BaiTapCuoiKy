package com.example.backend.dto.request;

public class ChatTongSendRequest {
    // Bạn có thể gửi bằng maNguoiGui hoặc email (mình hỗ trợ cả 2)
    private String maNguoiGui;
    private String emailNguoiGui;

    private String noiDung;
    private Long idTraLoi;

    public String getMaNguoiGui() { return maNguoiGui; }
    public void setMaNguoiGui(String maNguoiGui) { this.maNguoiGui = maNguoiGui; }

    public String getEmailNguoiGui() { return emailNguoiGui; }
    public void setEmailNguoiGui(String emailNguoiGui) { this.emailNguoiGui = emailNguoiGui; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public Long getIdTraLoi() { return idTraLoi; }
    public void setIdTraLoi(Long idTraLoi) { this.idTraLoi = idTraLoi; }
}
