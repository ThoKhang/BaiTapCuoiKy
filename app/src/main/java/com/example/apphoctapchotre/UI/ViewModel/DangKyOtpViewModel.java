package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

public class DangKyOtpViewModel extends ViewModel {

    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _verifySuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<Boolean> verifySuccess = _verifySuccess;

    public void verifyOtp(String email, String otp) {

        if (otp == null || otp.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập mã OTP!");
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
