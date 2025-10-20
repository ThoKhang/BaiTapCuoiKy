package com.example.apphoctapchotre.model;

import com.google.gson.annotations.SerializedName;  // Import cho @SerializedName (nếu chưa có, thêm vào build.gradle: implementation 'com.google.code.gson:gson:2.10.1')

public class User {
    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")  // ← Thêm field này để map JSON "password" từ backend
    private String password;     // ← Field mới (gửi lên server cho login/register)

    // Getters (giữ nguyên + thêm password)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }  // ← Getter mới

    // Setters (giữ nguyên + thêm password)
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }  // ← Setter mới (fix lỗi!)

    // Constructor mặc định (tùy chọn, nhưng tốt cho Gson)
    public User() {}

    // toString() để debug (tùy chọn)
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}