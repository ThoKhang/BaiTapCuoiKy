package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.LyThuyetNoiDungResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyThuyetNoiDungRepository {

    private final ApiService apiService;

    public LyThuyetNoiDungRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<LyThuyetNoiDungResponse> getNoiDung(String maHoatDong) {
        MutableLiveData<LyThuyetNoiDungResponse> data = new MutableLiveData<>();

        apiService.getNoiDungLyThuyet(maHoatDong).enqueue(new Callback<LyThuyetNoiDungResponse>() {
            @Override
            public void onResponse(Call<LyThuyetNoiDungResponse> call, Response<LyThuyetNoiDungResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LyThuyetNoiDungResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}