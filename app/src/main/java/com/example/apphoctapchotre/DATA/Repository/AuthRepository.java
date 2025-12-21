package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.FacebookLoginRequest;
import com.example.apphoctapchotre.DATA.model.GoogleLoginRequest;
import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.io.IOException;
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

    // ================================= LOGIN =================================
    public interface LoginListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public void login(String email, String password, LoginListener listener) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("matKhau", password);

        api.login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        listener.onSuccess(response.body().string().trim());
                    } else {
                        String msg = response.errorBody() != null
                                ? response.errorBody().string().trim()
                                : "Đăng nhập thất bại!";
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi đọc phản hồi từ server!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // ================================ REGISTER ===============================
    public interface RegisterListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public void register(String email, String password, RegisterListener listener) {
        Map<String, String> body = new HashMap<>();
        body.put("tenDangNhap", email);
        body.put("email", email);
        body.put("matKhau", password);

        api.register(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String msg = "Đăng ký thành công!";
                        if (response.body() != null) {
                            String raw = response.body().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                        listener.onSuccess(msg);
                    } else {
                        String msg = "Đăng ký thất bại!";
                        if (response.errorBody() != null) {
                            String raw = response.errorBody().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi xử lý phản hồi đăng ký!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // =============================== RESEND OTP ===============================
    public interface ResendOtpListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public void resendOtp(String email, ResendOtpListener listener) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);

        api.sendOtp(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        listener.onSuccess(response.body().string().trim());
                    } else {
                        String msg = response.errorBody() != null
                                ? response.errorBody().string().trim()
                                : "Không nhận được phản hồi!";
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi xử lý phản hồi!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // =============================== VERIFY OTP ===============================
    public interface VerifyOtpListener {
        void onSuccess(NguoiDung nguoiDung, String message);
        void onError(String message);
    }

    public void verifyOtp(String email, String otp, VerifyOtpListener listener) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("otp", otp);

        api.verifyOTP(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String msg = response.body() != null
                                ? response.body().string().trim()
                                : "Xác thực OTP thành công!";

                        Map<String, String> req = new HashMap<>();
                        req.put("email", email);

                        api.getByEmail(req).enqueue(new Callback<NguoiDung>() {
                            @Override
                            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> resUser) {
                                if (resUser.isSuccessful() && resUser.body() != null) {
                                    listener.onSuccess(resUser.body(), msg);
                                } else {
                                    listener.onError("Không lấy được thông tin người dùng!");
                                }
                            }

                            @Override
                            public void onFailure(Call<NguoiDung> call, Throwable t) {
                                listener.onError("Lỗi lấy thông tin người dùng!");
                            }
                        });
                    } else {
                        String msg = response.errorBody() != null
                                ? response.errorBody().string().trim()
                                : "Lỗi xác thực OTP!";
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi xử lý phản hồi OTP!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // ======================= VERIFY OTP ONLY (REGISTER / RESET) =======================
    public interface VerifyOtpOnlyListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public void verifyOtpOnly(String email, String otp, VerifyOtpOnlyListener listener) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("otp", otp);

        api.verifyOTP(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String msg = response.body() != null
                                ? response.body().string().trim()
                                : "Xác thực OTP thành công!";
                        listener.onSuccess(msg);
                    } else {
                        String msg = response.errorBody() != null
                                ? response.errorBody().string().trim()
                                : "OTP không đúng hoặc đã hết hạn!";
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi xử lý phản hồi OTP!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // ================================ RESET PASSWORD ================================
    public interface ResetPasswordListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public void resetPassword(String email, String newPassword, ResetPasswordListener listener) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("newPassword", newPassword);

        api.resetPassword(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String msg = "Đặt lại mật khẩu thành công!";
                        if (response.body() != null) {
                            String raw = response.body().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                        listener.onSuccess(msg);
                    } else {
                        String msg = "Không thể đặt lại mật khẩu!";
                        if (response.errorBody() != null) {
                            String raw = response.errorBody().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                        listener.onError(msg);
                    }
                } catch (IOException e) {
                    listener.onError("Lỗi xử lý phản hồi đặt lại mật khẩu!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    // =========================== LOGIN GOOGLE ===========================
    public interface GoogleLoginListener {
        void onSuccess(NguoiDung nguoiDung);
        void onError(String message);
    }

    public void loginWithGoogle(String idToken, GoogleLoginListener listener) {
        GoogleLoginRequest body = new GoogleLoginRequest(idToken);

        api.loginWithGoogle(body).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    String msg = "Đăng nhập Google thất bại!";
                    try {
                        if (response.errorBody() != null) {
                            String raw = response.errorBody().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                    } catch (Exception ignored) {}
                    listener.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    // =========================== LOGIN FACEBOOK ===========================
    public interface FacebookLoginListener {
        void onSuccess(NguoiDung nguoiDung);
        void onError(String message);
    }

    public void loginWithFacebook(String accessToken, FacebookLoginListener listener) {
        FacebookLoginRequest body = new FacebookLoginRequest(accessToken);

        api.loginWithFacebook(body).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    String msg = "Đăng nhập Facebook thất bại!";
                    try {
                        if (response.errorBody() != null) {
                            String raw = response.errorBody().string().trim();
                            if (!raw.isEmpty()) msg = raw;
                        }
                    } catch (Exception ignored) {}
                    listener.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
