package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)  // ← Thêm field này (map cột DB, NOT NULL để khớp SQL)
    private String username;

    @Column(unique = true, nullable = false)  // ← Cải thiện: Thêm nullable=false cho email
    private String email;

    @Column(nullable = false)  // ← Cải thiện: nullable=false cho password
    private String password;

    // Constructors (tùy chọn, nhưng tốt cho JPA)
    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }  // ← Thêm getter này (Jackson dùng để serialize JSON)
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // toString() để debug (tùy chọn)
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}