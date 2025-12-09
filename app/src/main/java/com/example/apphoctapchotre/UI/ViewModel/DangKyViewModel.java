package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;

public class DangKyViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<KetQuaDangNhap> _dangKyResult = new MutableLiveData<>();

    public DangKyViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public LiveData<KetQuaDangNhap> getDangKyResult() {
        return _dangKyResult;
    }

    public void dangKy(String email, String matKhau) {
        _loading.setValue(true);

        repository.dangKy(email, matKhau, result -> {
            _loading.postValue(false);
            _dangKyResult.postValue(result);
        });
    }
}
