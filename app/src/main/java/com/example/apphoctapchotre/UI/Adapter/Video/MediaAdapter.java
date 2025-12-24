package com.example.apphoctapchotre.UI.Adapter.Video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<Media> mediaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Media media);
    }

    public MediaAdapter(List<Media> mediaList, OnItemClickListener listener) {
        this.mediaList = mediaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Media media = mediaList.get(position);
        holder.txtTieuDe.setText(media.getTieuDe());
        holder.txtMoTa.setText(media.getMoTa());

        holder.itemView.setOnClickListener(v -> listener.onClick(media));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTieuDe, txtMoTa;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDe = itemView.findViewById(com.example.apphoctapchotre.R.id.txtTieuDe);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
        }
    }
}

