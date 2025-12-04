package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.CungCoResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CungCoRepository {

    private final ApiService apiService;

    public CungCoRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<List<CungCoResponse>> getTienDo(String maNguoiDung) {
        MutableLiveData<List<CungCoResponse>> data = new MutableLiveData<>();

        apiService.getTienDo(maNguoiDung).enqueue(new Callback<List<CungCoResponse>>() {
            @Override
            public void onResponse(Call<List<CungCoResponse>> call, Response<List<CungCoResponse>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<CungCoResponse>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
