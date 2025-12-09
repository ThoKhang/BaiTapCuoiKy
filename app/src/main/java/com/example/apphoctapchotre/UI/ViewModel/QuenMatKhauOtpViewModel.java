package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.DATA.model.AuthenticationResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauOtpViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _otpSuccess = new MutableLiveData<>();

    public QuenMatKhauOtpViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public LiveData<String> getMessage() {
        return _message;
    }

    public LiveData<Boolean> getOtpSuccess() {
        return _otpSuccess;
    }

    // Gửi OTP về email
    public void sendOtp(String email) {
        _loading.setValue(true);

        repository.sendOtp(email, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                _loading.postValue(false);
                if (response.isSuccessful()) {
                    _message.postValue("Đã gửi OTP đến email của bạn!");
                } else {
                    _message.postValue("Email không tồn tại hoặc gửi OTP thất bại!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _loading.postValue(false);
                _message.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Xác thực OTP quên mật khẩu
    public void verifyOtp(String email, String otp) {
        _loading.setValue(true);

        repository.verifyOtp(email, otp, new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                _loading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Có token nhưng với flow quên mật khẩu hiện tại ta không dùng token
                    _message.postValue("Xác thực OTP thành công!");
                    _otpSuccess.postValue(true);
                } else {
                    _message.postValue("OTP không đúng hoặc đã hết hạn!");
                    _otpSuccess.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                _loading.postValue(false);
                _message.postValue("Lỗi kết nối: " + t.getMessage());
                _otpSuccess.postValue(false);
            }
        });
    }
}
