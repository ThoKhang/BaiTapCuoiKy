package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.DeOnLuyen;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeOnLuyenRepository {
    private static DeOnLuyenRepository instance;
    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    public static DeOnLuyenRepository getInstance() {
        if(instance==null)
            instance = new DeOnLuyenRepository();
        return instance;
    }
    public LiveData<DeOnLuyen> getDeOnLuyen(String tieuDe) {
        MutableLiveData<DeOnLuyen> deOnLuyenMutableLiveData = new MutableLiveData<>();
        apiService.deOnLuyen(tieuDe).enqueue(new Callback<DeOnLuyen>() {
            @Override
            public void onResponse(Call<DeOnLuyen> call, Response<DeOnLuyen> response) {
                if(response.body()!=null && response.isSuccessful())
                    deOnLuyenMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DeOnLuyen> call, Throwable t) {
                deOnLuyenMutableLiveData.setValue(null);
            }
        });
        return deOnLuyenMutableLiveData;
    }
}
