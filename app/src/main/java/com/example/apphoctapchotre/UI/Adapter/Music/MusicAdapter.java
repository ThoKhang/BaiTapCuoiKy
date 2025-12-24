package com.example.apphoctapchotre.UI.Adapter.Music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicVH> {

    // ================= INTERFACE =================

    public interface OnItemClick {
        void onClick(Media media, int position);
    }

    // ================= DATA =================

    private List<Media> list;
    private OnItemClick listener;

    // 🔥 TRẠNG THÁI BÀI ĐANG PHÁT
    private int playingIndex = -1;
    private boolean isPlaying = false;

    public MusicAdapter(List<Media> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    // ================= PUBLIC API =================

    public List<Media> getList() {
        return list;
    }

    public void setData(List<Media> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * 🔥 GỌI HÀM NÀY ĐỂ CẬP NHẬT BÀI ĐANG PHÁT
     */
    public void setPlayingState(int index, boolean playing) {
        this.playingIndex = index;
        this.isPlaying = playing;
        notifyDataSetChanged();
    }

    // ================= ADAPTER =================

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

        Integer d = m.getThoiLuongGiay();
        if (d != null && d > 0) {
            int min = d / 60;
            int sec = d % 60;
            h.txtDuration.setText(String.format("%02d:%02d", min, sec));
        } else {
            h.txtDuration.setText("00:00");
        }

        // ================= ĐÁNH DẤU BÀI ĐANG PHÁT =================

        if (position == playingIndex) {
            h.txtTitle.setTextColor(
                    h.itemView.getContext().getColor(R.color.black)
            );
            h.imgPlaying.setVisibility(View.VISIBLE);
            h.imgPlaying.setImageResource(
                    isPlaying
                            ? android.R.drawable.ic_media_pause
                            : android.R.drawable.ic_media_play
            );
        } else {
            h.txtTitle.setTextColor(
                    h.itemView.getContext().getColor(android.R.color.black)
            );
            h.imgPlaying.setVisibility(View.GONE);
        }

        // ================= CLICK =================

        h.itemView.setOnClickListener(v ->
                listener.onClick(m, position)
        );
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // ================= VIEW HOLDER =================

    static class MusicVH extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDuration;
        ImageView imgPlaying;

        public MusicVH(@NonNull View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtDuration = v.findViewById(R.id.txtDuration);
            imgPlaying = v.findViewById(R.id.imgPlaying);
        }
    }
}
