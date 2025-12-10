package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.DATA.model.NguoiDung;

public class DangNhapOtpViewModel extends ViewModel {

    // Turn test mode on/off here
    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<NguoiDung> _loginSuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<NguoiDung> loginSuccess = _loginSuccess;


    public void resendOtp(String email) {
        if (TEST_MODE) {
            _toastMessage.setValue("Đã gửi lại mã OTP (test mode): 123456");
            return;
        }

        _loading.setValue(true);
        authRepository.resendOtp(email, new AuthRepository.ResendOtpListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
            }
        });
    }

    public void verifyOtp(String email, String otp) {
        // Test mode
        if (TEST_MODE) {
            if ("123456".equals(otp)) {
                _toastMessage.setValue("Đăng nhập thành công (test mode)!");
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung("ND004");
                _loginSuccess.setValue(nd);
            } else {
                _toastMessage.setValue("OTP sai! Trong test mode phải nhập: 123456");
            }
            return;
        }

        // Validate
        if (otp == null || otp.isEmpty() || otp.length() != 6 || !otp.matches("\\d+")) {
            _toastMessage.setValue("OTP phải là 6 chữ số!");
            return;
        }

        _loading.setValue(true);
        authRepository.verifyOtp(email, otp, new AuthRepository.VerifyOtpListener() {
            @Override
            public void onSuccess(NguoiDung nguoiDung, String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _loginSuccess.postValue(nguoiDung);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
            }
        });
    }
}
