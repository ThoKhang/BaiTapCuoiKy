package com.example.apphoctapchotre.UI.Adapter.Chat;

import android.util.Log;
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
    private final String maNguoiDung; // user đang login
    private OnChatActionListener listener;

    // ===== CALLBACK INTERFACE =====
    public interface OnChatActionListener {
        void onRecall(ChatTongResponse msg, int position);
    }

    public void setOnChatActionListener(OnChatActionListener listener) {
        this.listener = listener;
    }

    public ChatTongAdapter(List<ChatTongResponse> list, String maNguoiDung) {
        this.list = list;
        this.maNguoiDung = maNguoiDung != null ? maNguoiDung.trim() : "";
    }

    @Override
    public int getItemViewType(int position) {
        ChatTongResponse msg = list.get(position);

        String senderId = msg.getMaNguoiGui() != null
                ? msg.getMaNguoiGui().trim()
                : "";

        boolean isMe = !maNguoiDung.isEmpty()
                && !senderId.isEmpty()
                && maNguoiDung.equals(senderId);

        Log.d("CHAT_ADAPTER",
                "currentUser=" + maNguoiDung +
                        " | sender=" + senderId +
                        " | isMe=" + isMe);

        return isMe ? TYPE_ME : TYPE_OTHER;
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
            ((MeHolder) holder).bind(msg, listener, position);
        } else {
            ((OtherHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ================= VIEW HOLDERS =================

    static class MeHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtTime;

        MeHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtTime = itemView.findViewById(R.id.txtTime);
        }

        void bind(ChatTongResponse msg,
                  OnChatActionListener listener,
                  int position) {

            txtContent.setText(
                    Boolean.TRUE.equals(msg.getDaThuHoi())
                            ? "Tin nhắn đã thu hồi"
                            : msg.getNoiDung()
            );
            txtTime.setText(formatTime(msg.getNgayGui()));

            // 🔥 LONG PRESS – CHỈ ÁP DỤNG CHO TIN CỦA MÌNH
            itemView.setOnLongClickListener(v -> {
                if (listener != null && !Boolean.TRUE.equals(msg.getDaThuHoi())) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onRecall(msg, pos);
                    }
                }
                return true;
            });

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
            txtContent.setText(
                    Boolean.TRUE.equals(msg.getDaThuHoi())
                            ? "Tin nhắn đã thu hồi"
                            : msg.getNoiDung()
            );
            txtTime.setText(formatTime(msg.getNgayGui()));
        }
    }

    // ================= UTIL =================

    private static String formatTime(String time) {
        if (time == null || time.length() < 16) return "";
        return time.substring(11, 16); // HH:mm
    }
}
