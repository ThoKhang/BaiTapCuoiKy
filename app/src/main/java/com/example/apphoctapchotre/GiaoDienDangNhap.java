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
        setContentView(R.layout.activity_giao_dien_dang_nhap);  // Thay bằng tên file XML của bạn (activity_giaodien_dangnhap.xml nếu cần)

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
                        if (response.isSuccessful()) {  // 200 OK
                            // Đăng nhập thành công: Backend đã gửi email (từ code trước)
                            Toast.makeText(GiaoDienDangNhap.this, "Đăng nhập thành công! Email đã gửi.", Toast.LENGTH_SHORT).show();
                            // Chuyển sang màn hình DangNhapOTP
                            Intent intent = new Intent(GiaoDienDangNhap.this, DangNhapOTP.class);
                            startActivity(intent);
                            finish();  // Đóng màn hình hiện tại
                        } else {
                            // Lỗi từ server (ví dụ: 401 Sai mật khẩu)
                            String errorMsg = "Tài khoản hoặc mật khẩu sai!";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();  // Lấy message từ backend
                                }
                            } catch (Exception e) {
                                Log.e("LOGIN_ERROR", "Lỗi đọc response: " + e.getMessage());
                            }
                            Toast.makeText(GiaoDienDangNhap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Lỗi kết nối (mạng, server down)
                        Toast.makeText(GiaoDienDangNhap.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN_FAILURE", "Lỗi kết nối: " + t.getMessage());
                    }
                });
            }
        });
    }
}