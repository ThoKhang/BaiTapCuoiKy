package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;

public class DangKyViewModel extends ViewModel {

    private static final boolean TEST_MODE = false;

    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _registerSuccess = new MutableLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> toastMessage = _toastMessage;
    public LiveData<Boolean> registerSuccess = _registerSuccess;

    public void dangKy(String email, String matKhau, String nhapLaiMatKhau) {

        // ==== VALIDATE ====
        if (email == null || email.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()
                || nhapLaiMatKhau == null || nhapLaiMatKhau.trim().isEmpty()) {
            _toastMessage.setValue("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!matKhau.equals(nhapLaiMatKhau)) {
            _toastMessage.setValue("Mật khẩu không trùng!");
            return;
        }

        // ==== TEST MODE ====
        if (TEST_MODE) {
            // Giả lập đăng ký thành công
            _toastMessage.setValue("Đăng ký thành công (test mode)! Vui lòng kiểm tra email để lấy OTP.");
            _registerSuccess.setValue(true);
            return;
        }

        // ==== GỌI REPO ====
        _loading.setValue(true);

        authRepository.register(email, matKhau, new AuthRepository.RegisterListener() {
            @Override
            public void onSuccess(String message) {
                _loading.postValue(false);
                _toastMessage.postValue(message);
                _registerSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                _loading.postValue(false);

                // Map message "Email đã tồn tại" sang text đẹp như Activity cũ
                if (message != null && message.contains("Email đã tồn tại")) {
                    _toastMessage.postValue("Email này đã được sử dụng, vui lòng dùng email khác!");
                } else {
                    _toastMessage.postValue(
                            message != null && !message.isEmpty()
                                    ? message
                                    : "Đăng ký thất bại!"
                    );
                }

                _registerSuccess.postValue(false);
            }
        });
    }
}
