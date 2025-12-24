package com.example.apphoctapchotre.UI.Adapter.Music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicVH> {

    public interface OnItemClick {
        void onClick(Media media);
    }

    private List<Media> list;
    private OnItemClick listener;

    public MusicAdapter(List<Media> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    // 🔥 THÊM HÀM NÀY
    public void setData(List<Media> newList) {
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music, parent, false);
        return new MusicVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicVH h, int position) {
        Media m = list.get(position);
        h.txtTitle.setText(m.getTieuDe());

        if (m.getThoiLuongGiay() != null) {
            int min = m.getThoiLuongGiay() / 60;
            int sec = m.getThoiLuongGiay() % 60;
            h.txtDuration.setText(String.format("%02d:%02d", min, sec));
        }

        h.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MusicVH extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDuration;

        public MusicVH(@NonNull View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtDuration = v.findViewById(R.id.txtDuration);
        }
    }
}
