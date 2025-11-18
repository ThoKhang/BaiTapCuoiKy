package com.example.apphoctapchotre.model;

public class XepHangItem {
    private String username;
    private int score;

    public XepHangItem(String username, int score) {
        this.username = username;
        this.score = score;
    }

    // ===== THÊM 2 DÒNG GETTER NÀY =====
    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
    // ================================
}