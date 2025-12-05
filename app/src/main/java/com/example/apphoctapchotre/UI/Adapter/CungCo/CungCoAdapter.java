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
    private List<CungCoDaLamResponse> list; // ❌ bỏ final để cho phép update
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CungCoDaLamResponse item, int position);
    }

    public CungCoAdapter(Context context, List<CungCoDaLamResponse> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    // Setter cho listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void updateData(List<CungCoDaLamResponse> newList) {
        this.list = newList;
        clear();
        addAll(newList);
        notifyDataSetChanged();
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

        tvTieuDe.setText(item.getTieuDe());

        // Nếu đã hoàn thành
        if (item.isDaHoanThanh()) {
            tvDiem.setText("✓ Đã hoàn thành");
            tvDiem.setTextColor(context.getColor(android.R.color.holo_green_light));

        } else {
            tvDiem.setText("+" + item.getTongDiemToiDa() + " điểm");
            tvDiem.setTextColor(context.getColor(android.R.color.holo_orange_light));
        }

        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });

        return convertView;
    }
}
