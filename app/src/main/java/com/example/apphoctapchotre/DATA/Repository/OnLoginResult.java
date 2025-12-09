package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;

@FunctionalInterface   // không bắt buộc, nhưng giúp đảm bảo chỉ có 1 method
public interface OnLoginResult {
    void onCompleted(KetQuaDangNhap result);
}
