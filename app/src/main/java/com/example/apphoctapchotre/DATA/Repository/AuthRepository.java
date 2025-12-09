package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.AuthenticationResponse;
import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;
import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final ApiService api;

    public AuthRepository() {
        api = RetrofitClient.getClient().create(ApiService.class);
    }

    // ===== ĐĂNG KÝ =====
    public void dangKy(String email, String matKhau, OnLoginResult callback) {
        Map<String, String> body = new HashMap<>();
        body.put("tenDangNhap", email);
        body.put("email", email);
        body.put("matKhau", matKhau);

        api.register(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String msg = "Đăng ký thất bại!";
                boolean success = false;

                try {
                    if (response.isSuccessful()) {
                        success = true;
                        msg = "Đăng ký thành công! Vui lòng kiểm tra email để lấy OTP.";
                    } else if (response.errorBody() != null) {
                        String raw = response.errorBody().string();
                        if (raw != null) {
                            raw = raw.trim();
                            if (raw.contains("Email đã tồn tại")) {
                                msg = "Email này đã được sử dụng, vui lòng dùng email khác!";
                            } else {
                                msg = raw;
                            }
                        }
                    }
                } catch (Exception ignored) {}

                callback.onCompleted(new KetQuaDangNhap(success, msg, email));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onCompleted(
                        new KetQuaDangNhap(false, "Lỗi kết nối: " + t.getMessage(), email)
                );
            }
        });
    }

    // ===== Gửi lại OTP (dùng cho đăng nhập, đăng ký, quên MK) =====
    public void sendOtp(String email, Callback<ResponseBody> callback) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        api.sendOtp(request).enqueue(callback);
    }

    // ===== Xác thực OTP → trả AuthenticationResponse (token) =====
    public void verifyOtp(String email, String otp, Callback<AuthenticationResponse> callback) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("otp", otp);
        api.verifyOTP(request).enqueue(callback);
    }

    // ===== Lấy thông tin người dùng theo email =====
    public void getUserByEmail(String email, Callback<NguoiDung> callback) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        api.getByEmail(body).enqueue(callback);
    }

    // ===== ĐĂNG NHẬP =====
    public void dangNhap(String email, String matKhau, OnLoginResult callback) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("matKhau", matKhau);

        api.dangNhap(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String msg = response.body() != null
                            ? response.body().string()
                            : "Không nhận được phản hồi!";
                    KetQuaDangNhap result = new KetQuaDangNhap(true, msg, email);
                    callback.onCompleted(result);
                } catch (Exception e) {
                    callback.onCompleted(
                            new KetQuaDangNhap(false, "Lỗi xử lý!", email)
                    );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onCompleted(
                        new KetQuaDangNhap(false, "Lỗi kết nối!", email)
                );
            }
        });
    }

    // ===== QUÊN MẬT KHẨU: đặt lại mật khẩu =====
    public void resetPassword(String email, String newPassword, OnLoginResult callback) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("newPassword", newPassword);

        api.resetPassword(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean success = false;
                String msg = "Không thể đặt lại mật khẩu!";

                try {
                    if (response.isSuccessful()) {
                        success = true;
                        msg = "Đặt lại mật khẩu thành công!";
                    } else if (response.errorBody() != null) {
                        String raw = response.errorBody().string();
                        if (raw != null && !raw.isEmpty()) {
                            msg = raw;
                        }
                    }
                } catch (Exception ignored) {}

                callback.onCompleted(new KetQuaDangNhap(success, msg, email));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onCompleted(
                        new KetQuaDangNhap(false, "Lỗi kết nối: " + t.getMessage(), email)
                );
            }
        });
    }
}
