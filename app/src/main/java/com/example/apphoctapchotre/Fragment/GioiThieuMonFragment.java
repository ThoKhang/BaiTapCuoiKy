package com.example.apphoctapchotre.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.KhoaHocGioiThieuAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.KhoaHocGioiThieu;

import java.util.ArrayList;

public class GioiThieuMonFragment extends Fragment {

    private static final String ARG_MON = "mon";
    private String monHoc;

    public static GioiThieuMonFragment newInstance(String mon) {
        GioiThieuMonFragment fragment = new GioiThieuMonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MON, mon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gioi_thieu_mon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rvCourses);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        monHoc = getArguments().getString(ARG_MON);

        ArrayList<KhoaHocGioiThieu> ds = new ArrayList<>();

        switch (monHoc) {
            case "english":
                ds.add(new KhoaHocGioiThieu("Tiếng anh lớp 1", 4.5f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Tiếng anh lớp 2", 4.4f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Tiếng anh lớp 3", 4.4f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Tiếng anh lớp 4", 4.6f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Tiếng anh lớp 5", 4.6f, R.drawable.thuthachoc));
                break;

            case "math":
                ds.add(new KhoaHocGioiThieu("Toán lớp 1", 4.5f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Toán lớp 2", 4.4f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Toán lớp 3", 4.4f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Toán lớp 4", 4.6f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Toán lớp 5", 4.6f, R.drawable.thuthachoc));
                break;

            case "fun":
                ds.add(new KhoaHocGioiThieu("Giải trí 1", 4.5f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Giải trí 2", 4.4f, R.drawable.thuthachoc));
                ds.add(new KhoaHocGioiThieu("Giải trí 3", 4.6f, R.drawable.thuthachoc));
                break;
        }

        KhoaHocGioiThieuAdapter adapter = new KhoaHocGioiThieuAdapter(ds);
        rv.setAdapter(adapter);
    }
}
