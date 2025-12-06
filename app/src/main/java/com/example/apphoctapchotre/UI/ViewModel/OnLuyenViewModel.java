package com.example.apphoctapchotre.UI.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.OnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.OnLuyenTongQuan;

public class OnLuyenViewModel extends ViewModel {
    private final MutableLiveData<OnLuyenTongQuan> tongQuanLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final OnLuyenRepository repository;
    public OnLuyenViewModel(Context context) {
        repository = new OnLuyenRepository(context);
    }
    public LiveData<OnLuyenTongQuan> getTongQuanLiveData() {
        return tongQuanLiveData;
    }
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    public void loadTongQuan(String maNguoiDung) {
        repository.getTongQuan(maNguoiDung, new OnLuyenRepository.OnLuyenCallBack() {
            @Override
            public void onSuccess(OnLuyenTongQuan data) {
                tongQuanLiveData.setValue(data);
            }
            @Override
            public void onFailed(String error) {
                errorLiveData.setValue(error);
            }
        });
    }
}
