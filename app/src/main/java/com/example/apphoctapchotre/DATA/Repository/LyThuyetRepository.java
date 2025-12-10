package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.LyThuyetMonHocResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetDaLamResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyThuyetRepository {

    private final ApiService apiService;

    public LyThuyetRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<List<LyThuyetResponse>> getTienDo(String maNguoiDung) {

        MutableLiveData<List<LyThuyetResponse>> data = new MutableLiveData<>();

        apiService.getTienDoLyThuyet(maNguoiDung).enqueue(new Callback<List<LyThuyetResponse>>() {
            @Override
            public void onResponse(Call<List<LyThuyetResponse>> call, Response<List<LyThuyetResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<LyThuyetResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    public LiveData<List<LyThuyetMonHocResponse>> getDanhSachLyThuyet(String maMonHoc) {

        MutableLiveData<List<LyThuyetMonHocResponse>> data = new MutableLiveData<>();

        apiService.getDanhSachLyThuyet(maMonHoc).enqueue(new Callback<List<LyThuyetMonHocResponse>>() {
            @Override
            public void onResponse(Call<List<LyThuyetMonHocResponse>> call, Response<List<LyThuyetMonHocResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<LyThuyetMonHocResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    public LiveData<List<LyThuyetDaLamResponse>> getDanhSachLyThuyetDaLam(String maMonHoc, String maNguoiDung) {

        MutableLiveData<List<LyThuyetDaLamResponse>> data = new MutableLiveData<>();

        apiService.getLyThuyetDaLam(maMonHoc, maNguoiDung).enqueue(new Callback<List<LyThuyetDaLamResponse>>() {
            @Override
            public void onResponse(Call<List<LyThuyetDaLamResponse>> call, Response<List<LyThuyetDaLamResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<LyThuyetDaLamResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }
}