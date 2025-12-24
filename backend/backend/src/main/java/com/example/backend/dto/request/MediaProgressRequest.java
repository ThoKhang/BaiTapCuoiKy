package com.example.backend.dto.request;

public class MediaProgressRequest {
    private int viTriGiay;
    private boolean daXem;

    public int getViTriGiay() { return viTriGiay; }
    public void setViTriGiay(int viTriGiay) { this.viTriGiay = viTriGiay; }

    public boolean isDaXem() { return daXem; }
    public void setDaXem(boolean daXem) { this.daXem = daXem; }
}
