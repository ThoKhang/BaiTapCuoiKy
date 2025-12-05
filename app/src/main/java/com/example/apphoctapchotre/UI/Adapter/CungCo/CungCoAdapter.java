package com.example.apphoctapchotre.UI.Adapter.CungCo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.apphoctapchotre.DATA.model.CungCoDaLamResponse;
import com.example.apphoctapchotre.R;

import java.util.List;

public class CungCoAdapter extends ArrayAdapter<CungCoDaLamResponse> {

    private final Context context;
    private final List<CungCoDaLamResponse> list;

    public CungCoAdapter(Context context, List<CungCoDaLamResponse> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_cung_co, parent, false);
        }

        CungCoDaLamResponse item = list.get(position);

        TextView tvTieuDe = convertView.findViewById(R.id.tvTieuDe);
        TextView tvDiem = convertView.findViewById(R.id.tvDiem);

        // Gán tiêu đề
        tvTieuDe.setText(item.getTieuDe());

        // Kiểm tra nếu đã hoàn thành thì hiện "✓ Đã hoàn thành"
        if (item.isDaHoanThanh()) {
            tvDiem.setText("✓ Đã hoàn thành");
            tvDiem.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            tvDiem.setText("+" + item.getTongDiemToiDa() + " điểm");
            tvDiem.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
        }

        return convertView;
    }
}