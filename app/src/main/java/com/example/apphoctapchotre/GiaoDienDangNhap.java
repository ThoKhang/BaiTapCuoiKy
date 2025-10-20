package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiaoDienDangNhap extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau;
    private Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_dang_nhap);  // Tên layout XML của bạn

        // Lấy view từ layout
        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        // Sự kiện click nút Đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String email = eTextEmail.getText().toString().trim();
                String matKhau = eTextMatKhau.getText().toString().trim();

                // Validate input (không rỗng)
                if (email.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(GiaoDienDangNhap.this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo User object cho API
                User loginUser = new User();
                loginUser.setEmail(email);
                loginUser.setPassword(matKhau);  // Không cần username cho login

                // Gọi API login qua Retrofit
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<ResponseBody> call = apiService.login(loginUser);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Đọc body message từ backend (động, ưu tiên backend)
                            String successMsg = "";
                            try {
                                if (response.body() != null) {
                                    successMsg = response.body().string();  // Message từ ResponseEntity.ok()
                                    if (successMsg.isEmpty()) {
                                        successMsg = "Đăng nhập thành công! OTP đã gửi đến email.";  // Fallback nếu rỗng
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("LOGIN_MSG", "Lỗi đọc body: " + e.getMessage());
                                successMsg = "Đăng nhập thành công! OTP đã gửi đến email.";  // Fallback nếu lỗi
                            }
                            Toast.makeText(GiaoDienDangNhap.this, successMsg, Toast.LENGTH_SHORT).show();
                            // Chuyển sang màn hình OTP, truyền email
                            Intent intent = new Intent(GiaoDienDangNhap.this, DangNhapOTP.class);
                            intent.putExtra("EMAIL", email);
                            startActivity(intent);
                            finish();
                        } else {
                            // Lỗi từ server: Đọc error body từ backend
                            String errorMsg = "Tài khoản hoặc mật khẩu sai!";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();  // Message từ ResponseEntity.badRequest() hoặc status(401)
                                }
                            } catch (Exception e) {
                                Log.e("LOGIN_ERROR", "Lỗi đọc error body: " + e.getMessage());
                            }
                            Toast.makeText(GiaoDienDangNhap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Lỗi kết nối
                        Toast.makeText(GiaoDienDangNhap.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN_FAILURE", "Lỗi kết nối: " + t.getMessage());
                    }
                });
            }
        });
    }
}