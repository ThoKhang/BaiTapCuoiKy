package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;

public class QuenMatKhauViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _resetSuccess = new MutableLiveData<>();

    public QuenMatKhauViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public LiveData<String> getMessage() {
        return _message;
    }

    public LiveData<Boolean> getResetSuccess() {
        return _resetSuccess;
    }

    public void resetPassword(String email, String newPassword) {
        _loading.setValue(true);

        repository.resetPassword(email, newPassword, result -> {
            _loading.postValue(false);
            _message.postValue(result.getMessage());
            _resetSuccess.postValue(result.isSuccess());
        });
    }
}
