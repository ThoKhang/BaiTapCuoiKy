package com.example.backend.dto.response;

public class LyThuyetDetailResponse {
    private String tieuDe;
    private String noiDung;
    private boolean daHoanThanh;

    public LyThuyetDetailResponse() {}

    public LyThuyetDetailResponse(String tieuDe, String noiDung, boolean daHoanThanh) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.daHoanThanh = daHoanThanh;
    }

    // Getter & Setter
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public boolean isDaHoanThanh() { return daHoanThanh; }
    public void setDaHoanThanh(boolean daHoanThanh) { this.daHoanThanh = daHoanThanh; }
}
