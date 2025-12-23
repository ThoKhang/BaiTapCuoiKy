package com.example.apphoctapchotre.UI.Adapter.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.ChatTongResponse;
import com.example.apphoctapchotre.R;

import java.util.List;

public class ChatTongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ME = 1;
    private static final int TYPE_OTHER = 2;

    private final List<ChatTongResponse> list;
    private final String maNguoiDung;

    public ChatTongAdapter(List<ChatTongResponse> list, String maNguoiDung) {
        this.list = list;
        this.maNguoiDung = maNguoiDung;
    }

    @Override
    public int getItemViewType(int position) {
        ChatTongResponse msg = list.get(position);
        return maNguoiDung.equals(msg.getMaNguoiGui()) ? TYPE_ME : TYPE_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ME) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tin_nhan_me, parent, false);
            return new MeHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tin_nhan, parent, false);
            return new OtherHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatTongResponse msg = list.get(position);

        if (holder instanceof MeHolder) {
            ((MeHolder) holder).bind(msg);
        } else {
            ((OtherHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ===== VIEW HOLDERS =====

    static class MeHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtTime;

        MeHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtTime = itemView.findViewById(R.id.txtTime);
        }

        void bind(ChatTongResponse msg) {
            txtContent.setText(msg.getDaThuHoi() ? "Tin nhắn đã thu hồi" : msg.getNoiDung());
            txtTime.setText(formatTime(msg.getNgayGui()));
        }
    }

    static class OtherHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtTime, txtUsername;
        ImageView imgAvatar;

        OtherHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

        void bind(ChatTongResponse msg) {
            txtUsername.setText(msg.getTenDangNhapNguoiGui());
            txtContent.setText(msg.getDaThuHoi() ? "Tin nhắn đã thu hồi" : msg.getNoiDung());
            txtTime.setText(formatTime(msg.getNgayGui()));
        }
    }

    private static String formatTime(String time) {
        if (time == null) return "";
        return time.substring(11, 16); // HH:mm
    }
}