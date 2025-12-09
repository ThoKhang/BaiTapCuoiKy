package com.example.apphoctapchotre.DATA.model;

public class KetQuaDangNhap {
    private final boolean success;
    private final String message;
    private final String email;

    public KetQuaDangNhap(boolean success, String message, String email) {
        this.success = success;
        this.message = message;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}
