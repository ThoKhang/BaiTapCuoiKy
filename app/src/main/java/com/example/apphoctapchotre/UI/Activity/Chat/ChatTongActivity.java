package com.example.apphoctapchotre.UI.Activity.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.Repository.ChatTongRepository;
import com.example.apphoctapchotre.DATA.model.ChatTongResponse;
import com.example.apphoctapchotre.DATA.model.ChatTongSendRequest;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Adapter.Chat.ChatTongAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatTongActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText edtMessage;
    private ImageButton btnSend;

    private ChatTongAdapter adapter;
    private final List<ChatTongResponse> messageList = new ArrayList<>();

    private ChatTongRepository repository;
    private String maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_tin);

        // 🔥 DÙNG CHUNG PREFS VỚI CUNGCOACTIVITY
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getString("MA_NGUOI_DUNG", null);

        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            Toast.makeText(this,
                    "Không tìm thấy mã người dùng. Vui lòng đăng nhập lại.",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        initData();
        initAction();
        loadMessages();

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void initView() {
        rvChat = findViewById(R.id.rvChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rvChat.setLayoutManager(lm);

        adapter = new ChatTongAdapter(messageList, maNguoiDung);
        rvChat.setAdapter(adapter);
    }

    private void initData() {
        repository = new ChatTongRepository();
    }

    private void initAction() {
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        repository.getRecentMessages(50, null)
                .enqueue(new Callback<List<ChatTongResponse>>() {
                    @Override
                    public void onResponse(Call<List<ChatTongResponse>> call,
                                           Response<List<ChatTongResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            messageList.clear();
                            messageList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            rvChat.scrollToPosition(messageList.size() - 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChatTongResponse>> call, Throwable t) {
                        Toast.makeText(ChatTongActivity.this,
                                "Không tải được tin nhắn", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMessage() {
        String content = edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;

        ChatTongSendRequest request = new ChatTongSendRequest();
        request.setMaNguoiGui(maNguoiDung);
        request.setNoiDung(content);

        repository.sendMessage(request)
                .enqueue(new Callback<ChatTongResponse>() {
                    @Override
                    public void onResponse(Call<ChatTongResponse> call,
                                           Response<ChatTongResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            messageList.add(response.body());
                            adapter.notifyItemInserted(messageList.size() - 1);
                            rvChat.scrollToPosition(messageList.size() - 1);
                            edtMessage.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatTongResponse> call, Throwable t) {
                        Toast.makeText(ChatTongActivity.this,
                                "Gửi thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
