package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.CungCoMonHocResponse;
import com.example.apphoctapchotre.DATA.model.CungCoResponse;
import com.example.apphoctapchotre.DATA.model.CungCoDaLamResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CungCoRepository {

    private final ApiService apiService;

    public CungCoRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    // =======================================================
    // 1) Lấy TIẾN ĐỘ môn học
    // =======================================================
    public LiveData<List<CungCoResponse>> getTienDo(String maNguoiDung) {

        MutableLiveData<List<CungCoResponse>> data = new MutableLiveData<>();

        apiService.getTienDo(maNguoiDung).enqueue(new Callback<List<CungCoResponse>>() {
            @Override
            public void onResponse(Call<List<CungCoResponse>> call, Response<List<CungCoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CungCoResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    // =======================================================
    // 2) Lấy danh sách Củng cố theo môn
    // =======================================================
    public LiveData<List<CungCoMonHocResponse>> getDanhSachCungCo(String maMonHoc) {

        MutableLiveData<List<CungCoMonHocResponse>> data = new MutableLiveData<>();

        apiService.getDanhSachCungCo(maMonHoc).enqueue(new Callback<List<CungCoMonHocResponse>>() {
            @Override
            public void onResponse(Call<List<CungCoMonHocResponse>> call, Response<List<CungCoMonHocResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CungCoMonHocResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    // =======================================================
    // 3) Lấy danh sách Củng cố ĐÃ LÀM
    // =======================================================
    public LiveData<List<CungCoDaLamResponse>> getDanhSachCungCoDaLam(String maMonHoc, String maNguoiDung) {

        MutableLiveData<List<CungCoDaLamResponse>> data = new MutableLiveData<>();

        apiService.getCungCoDaLam(maMonHoc, maNguoiDung).enqueue(new Callback<List<CungCoDaLamResponse>>() {
            @Override
            public void onResponse(Call<List<CungCoDaLamResponse>> call, Response<List<CungCoDaLamResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CungCoDaLamResponse>> call, Throwable t) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }
}
