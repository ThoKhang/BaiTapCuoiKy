package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.DATA.model.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyOtpViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _otpSuccess = new MutableLiveData<>();

    public DangKyOtpViewModel() {
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

    public void xacThucOtpDangKy(String email, String otp) {
        _loading.setValue(true);

        repository.verifyOtp(email, otp, new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                _loading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Có token nhưng đăng ký xong bạn không đăng nhập luôn, nên bỏ qua
                    // String token = response.body().getToken();
                    _message.postValue("Xác thực OTP thành công! Bạn có thể đăng nhập.");
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
