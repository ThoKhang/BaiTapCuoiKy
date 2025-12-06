package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CauHoiRepository {

    private final ApiService apiService;

    public CauHoiRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    // =======================================================
    // Lấy danh sách câu hỏi theo mã hoạt động
    // =======================================================
    public LiveData<List<CauHoi>> getDanhSachCauHoi(String maHoatDong) {
        MutableLiveData<List<CauHoi>> data = new MutableLiveData<>();

        apiService.getDanhSachCauHoi(maHoatDong).enqueue(new Callback<List<CauHoi>>() {
            @Override
            public void onResponse(Call<List<CauHoi>> call, Response<List<CauHoi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // API trả về dữ liệu không đúng cấu trúc, cần xử lý
                    List<CauHoi> processedList = xuLyDuLieuTuAPI(response.body());
                    data.setValue(processedList);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<CauHoi>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    // =======================================================
    // Xử lý dữ liệu từ API (vì API trả về dữ liệu không đúng cấu trúc)
    // =======================================================
    private List<CauHoi> xuLyDuLieuTuAPI(List<CauHoi> rawData) {
        // Tạo map để nhóm dữ liệu theo maCauHoi
        Map<String, CauHoi> cauHoiMap = new HashMap<>();

        // Duyệt qua tất cả dữ liệu thô từ API
        for (CauHoi item : rawData) {
            String maCauHoi = item.getMaCauHoi();

            if (!cauHoiMap.containsKey(maCauHoi)) {
                // Tạo mới CauHoi
                CauHoi cauHoi = new CauHoi();
                cauHoi.setMaCauHoi(maCauHoi);
                cauHoi.setThuTu(item.getThuTu());
                cauHoi.setNoiDungCauHoi(item.getNoiDungCauHoi());
                cauHoi.setDiemToiDa(item.getDiemToiDa());
                cauHoi.setDapAn(new ArrayList<>());
                cauHoiMap.put(maCauHoi, cauHoi);
            }

            // Thêm dữ liệu đáp án (nếu có)
            if (item.getDapAn() != null && !item.getDapAn().isEmpty()) {
                cauHoiMap.get(maCauHoi).getDapAn().addAll(item.getDapAn());
            }
        }

        return new ArrayList<>(cauHoiMap.values());
    }
}