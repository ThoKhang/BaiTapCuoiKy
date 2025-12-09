package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

public class GiaoDienDangNhapViewModel extends ViewModel {

    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loginSuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<Boolean> loginSuccess = _loginSuccess;


    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập email và mật khẩu!");
            return;
        }

        // Test mode
        if (TEST_MODE) {
            if ("test@gmail.com".equals(email) && "123456".equals(password)) {
                _toastMessage.setValue("Đăng nhập thành công (test mode)!");
                _loginSuccess.setValue(true);
            } else {
                _toastMessage.setValue("Sai email hoặc mật khẩu (test mode)!");
                _loginSuccess.setValue(false);
            }
            return;
        }

        _loading.setValue(true);
        authRepository.login(email, password, new AuthRepository.LoginListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _loginSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _loginSuccess.postValue(false);
            }
        });
    }
}
