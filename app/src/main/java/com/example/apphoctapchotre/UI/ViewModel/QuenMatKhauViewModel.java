package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

public class QuenMatKhauViewModel extends ViewModel {

    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _resetSuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<Boolean> resetSuccess = _resetSuccess;

    public void resetPassword(String email, String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập đầy đủ mật khẩu!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            _toastMessage.setValue("Mật khẩu nhập lại không khớp!");
            return;
        }

        if (TEST_MODE) {
            _toastMessage.setValue("Đặt lại mật khẩu thành công (test mode)!");
            _resetSuccess.setValue(true);
            return;
        }

        _loading.setValue(true);
        authRepository.resetPassword(email, newPassword, new AuthRepository.ResetPasswordListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _resetSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _resetSuccess.postValue(false);
            }
        });
    }
}
