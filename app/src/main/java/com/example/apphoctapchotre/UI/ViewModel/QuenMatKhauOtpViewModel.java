package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

public class QuenMatKhauOtpViewModel extends ViewModel {

    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _verifySuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<Boolean> verifySuccess = _verifySuccess;

    public void sendOtp(String email) {
        if (email == null || email.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập email!");
            return;
        }

        if (TEST_MODE) {
            _toastMessage.setValue("Đã gửi OTP (test mode): 123456");
            return;
        }

        _loading.setValue(true);
        authRepository.resendOtp(email, new AuthRepository.ResendOtpListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message.isEmpty()
                        ? "Đã gửi OTP đến email của bạn!"
                        : message);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message.isEmpty()
                        ? "Email không tồn tại hoặc gửi OTP thất bại!"
                        : message);
            }
        });
    }

    public void verifyOtp(String email, String otp) {
        if (email == null || email.trim().isEmpty()
                || otp == null || otp.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (TEST_MODE) {
            if ("123456".equals(otp)) {
                _toastMessage.setValue("Xác thực OTP thành công (test mode)!");
                _verifySuccess.setValue(true);
            } else {
                _toastMessage.setValue("OTP sai! Trong test mode phải nhập: 123456");
                _verifySuccess.setValue(false);
            }
            return;
        }

        _loading.setValue(true);
        authRepository.verifyOtpOnly(email, otp, new AuthRepository.VerifyOtpOnlyListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _verifySuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _verifySuccess.postValue(false);
            }
        });
    }
}
