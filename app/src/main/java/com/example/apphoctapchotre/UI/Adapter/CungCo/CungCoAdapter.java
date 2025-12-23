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
    private List<CungCoDaLamResponse> list;
    private OnItemClickListener listener;
    private String maMon;


    public interface OnItemClickListener {
        void onItemClick(CungCoDaLamResponse item, int position);
    }

    public CungCoAdapter(Context context, List<CungCoDaLamResponse> list, String maMon) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.maMon = maMon;
    }


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

        View root = convertView;

        if ("MH002".equals(maMon)) {
            root.setBackgroundResource(R.drawable.bg_banner_blue);
        } else if ("MH001".equals(maMon)) {
            root.setBackgroundResource(R.drawable.bg_banner_green);
        }

        CungCoDaLamResponse item = list.get(position);

        TextView tvTieuDe = convertView.findViewById(R.id.tvTieuDe);
        TextView tvDiem = convertView.findViewById(R.id.tvDiem);

        tvTieuDe.setText(item.getTieuDe());

        if (item.isDaHoanThanh()) {

            tvDiem.setText(item.getSoCauDung() + "/" + item.getSoCauDaLam());

            tvDiem.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                            context, android.R.color.white
                    )
            );

            tvDiem.setBackgroundResource(R.drawable.border_done);

        } else {

            tvDiem.setText("+" + item.getTongDiemToiDa() + " điểm");

            tvDiem.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                            context, android.R.color.holo_orange_light
                    )
            );

            tvDiem.setBackground(null);
        }

        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });

        return convertView;
    }

}
