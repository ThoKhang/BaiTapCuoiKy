package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.XepHangRepository;
import com.example.apphoctapchotre.DATA.model.XepHangResponse;

public class XepHangViewModel extends ViewModel {

    private final XepHangRepository repository;

    private final MutableLiveData<XepHangResponse> xepHangLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dangTaiLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> loiLiveData = new MutableLiveData<>();

    public XepHangViewModel() {
        repository = new XepHangRepository();
    }

    public LiveData<XepHangResponse> getXepHangLiveData() {
        return xepHangLiveData;
    }

    public LiveData<Boolean> getDangTaiLiveData() {
        return dangTaiLiveData;
    }

    public LiveData<String> getLoiLiveData() {
        return loiLiveData;
    }

    public void taiDuLieuXepHang(String email, int gioiHan) {
        dangTaiLiveData.setValue(true);

        repository.layXepHang(email, gioiHan, new XepHangRepository.RepositoryCallback<XepHangResponse>() {
            @Override
            public void onSuccess(XepHangResponse data) {
                dangTaiLiveData.setValue(false);
                xepHangLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                dangTaiLiveData.setValue(false);
                loiLiveData.setValue(message);
            }
        });
    }
}
