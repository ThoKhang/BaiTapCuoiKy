package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.DangNhapRepository;
import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;

public class DangNhapViewModel extends ViewModel {

    private final DangNhapRepository repository;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<KetQuaDangNhap> _dangNhapResult = new MutableLiveData<>();

    public DangNhapViewModel() {
        repository = new DangNhapRepository();
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public LiveData<KetQuaDangNhap> getDangNhapResult() {
        return _dangNhapResult;
    }

    public void dangNhap(String email, String matKhau) {
        _loading.setValue(true);

        repository.dangNhap(email, matKhau, result -> {
            _loading.postValue(false);
            _dangNhapResult.postValue(result);
        });
    }
}
