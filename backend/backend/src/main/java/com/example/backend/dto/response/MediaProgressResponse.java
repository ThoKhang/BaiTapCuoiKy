package com.example.backend.dto.response;

import java.time.LocalDateTime;

public class MediaProgressResponse {
    private Long maMedia;
    private String maNguoiDung;
    private boolean daXem;
    private int viTriGiay;
    private LocalDateTime lanXemCuoi;

    public Long getMaMedia() { return maMedia; }
    public void setMaMedia(Long maMedia) { this.maMedia = maMedia; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public boolean isDaXem() { return daXem; }
    public void setDaXem(boolean daXem) { this.daXem = daXem; }

    public int getViTriGiay() { return viTriGiay; }
    public void setViTriGiay(int viTriGiay) { this.viTriGiay = viTriGiay; }

    public LocalDateTime getLanXemCuoi() { return lanXemCuoi; }
    public void setLanXemCuoi(LocalDateTime lanXemCuoi) { this.lanXemCuoi = lanXemCuoi; }
}
