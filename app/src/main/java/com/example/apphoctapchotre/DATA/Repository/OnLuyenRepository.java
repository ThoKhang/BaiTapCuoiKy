package com.example.apphoctapchotre.DATA.Repository;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.OnLuyen;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnLuyenRepository {
    private static OnLuyenRepository instance;
    private ApiService apiService= RetrofitClient.getClient().create(ApiService.class);
    public static OnLuyenRepository getInstance() {
        if (instance == null) {
            instance = new OnLuyenRepository();
        }
        return instance;
    }
    public LiveData<OnLuyen> getOnLuyen(String email){
        MutableLiveData<OnLuyen>data=new MutableLiveData<>();
        apiService.onLuyen(email).enqueue(new Callback<OnLuyen>() {
            @Override
            public void onResponse(Call<OnLuyen> call, Response<OnLuyen> response) {
                if(response!=null && response.body()!=null && response.isSuccessful())
                    data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<OnLuyen> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
