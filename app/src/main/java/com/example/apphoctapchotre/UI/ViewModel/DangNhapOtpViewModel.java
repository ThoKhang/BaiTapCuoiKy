package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.model.AuthenticationResponse;
import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapOtpViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public DangNhapOtpViewModel() {
        authRepository = new AuthRepository();
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;

    public static class LoginResult {
        private final String token;
        private final NguoiDung nguoiDung;

        public LoginResult(String token, NguoiDung nguoiDung) {
            this.token = token;
            this.nguoiDung = nguoiDung;
        }

        public String getToken() {
            return token;
        }

        public NguoiDung getNguoiDung() {
            return nguoiDung;
        }
    }

    private final MutableLiveData<LoginResult> _loginSuccess = new MutableLiveData<>();
    public LiveData<LoginResult> loginSuccess = _loginSuccess;

    // ===== Gửi lại OTP =====
    public void sendOtp(String email) {
        _isLoading.setValue(true);

        authRepository.sendOtp(email, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                _isLoading.postValue(false);
                try {
                    String msg = response.body() != null
                            ? response.body().string()
                            : "Không nhận được phản hồi!";
                    _message.postValue(msg);
                } catch (Exception e) {
                    _message.postValue("Lỗi gửi lại mã!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _isLoading.postValue(false);
                _message.postValue("Lỗi kết nối!");
            }
        });
    }

    // ===== Verify OTP -> token -> user =====
    public void verifyOtpAndGetUser(String email, String otp) {
        _isLoading.setValue(true);

        authRepository.verifyOtp(email, otp, new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    _isLoading.postValue(false);
                    try {
                        String msg = response.errorBody() != null
                                ? response.errorBody().string().trim()
                                : "Lỗi xác thực OTP!";
                        _message.postValue(msg);
                    } catch (Exception e) {
                        _message.postValue("Lỗi xác thực OTP!");
                    }
                    return;
                }

                String token = response.body().getToken();
                if (token == null || token.isEmpty()) {
                    _isLoading.postValue(false);
                    _message.postValue("Không nhận được token từ server!");
                    return;
                }

                // Có token rồi → tiếp tục lấy thông tin người dùng
                authRepository.getUserByEmail(email, new Callback<NguoiDung>() {
                    @Override
                    public void onResponse(Call<NguoiDung> call, Response<NguoiDung> resUser) {
                        _isLoading.postValue(false);
                        if (!resUser.isSuccessful() || resUser.body() == null) {
                            _message.postValue("Không lấy được thông tin người dùng!");
                            return;
                        }

                        NguoiDung nd = resUser.body();
                        _loginSuccess.postValue(new LoginResult(token, nd));
                    }

                    @Override
                    public void onFailure(Call<NguoiDung> call, Throwable t) {
                        _isLoading.postValue(false);
                        _message.postValue("Lỗi lấy thông tin người dùng!");
                    }
                });
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                _isLoading.postValue(false);
                _message.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
