package com.example.apphoctapchotre.DATA.Repository;

import android.util.Log;

import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapRepository {

    private final ApiService apiService;

    public DangNhapRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public interface DangNhapCallback {
        void onResult(KetQuaDangNhap result);
    }

    public void dangNhap(String email, String matKhau, DangNhapCallback callback) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("matKhau", matKhau);

        Call<ResponseBody> call = apiService.login(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String message = response.body().string();
                        callback.onResult(new KetQuaDangNhap(true, message, email));
                    } else {
                        String message = response.errorBody() != null ?
                                response.errorBody().string() :
                                "Dang nhap that bai!";
                        callback.onResult(new KetQuaDangNhap(false, message, email));
                    }
                } catch (IOException e) {
                    Log.e("DangNhapRepository", "Loi parse: " + e.getMessage());
                    callback.onResult(new KetQuaDangNhap(false,
                            "Loi doc phan hoi tu server!", email));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onResult(new KetQuaDangNhap(false,
                        "Loi ket noi: " + t.getMessage(), email));
            }
        });
    }
}
