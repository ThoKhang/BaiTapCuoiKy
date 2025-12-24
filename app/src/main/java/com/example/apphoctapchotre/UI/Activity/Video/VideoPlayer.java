package com.example.apphoctapchotre.UI.Activity.Video;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.MediaViewModel;

public class VideoPlayer extends AppCompatActivity {
    private TextView back;
    private MediaViewModel VideoviewModel;
    private Long maMedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playe);

        back =findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Video.class);
            startActivity(intent);
        });
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
        VideoView videoView = findViewById(R.id.videoView);
        maMedia = getIntent().getLongExtra("maMedia", -1);
        String duongDanFile = getIntent().getStringExtra("video_url");

        String fullUrl = "http://10.0.2.2:8080/" + duongDanFile;

        videoView.setVideoURI(Uri.parse(fullUrl));

        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);

        videoView.setOnPreparedListener(mp -> {
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();

            int parentWidth = ((android.view.View) videoView.getParent()).getWidth();
            int parentHeight = ((android.view.View) videoView.getParent()).getHeight();

            float scaleX = (float) parentWidth / videoWidth;
            float scaleY = (float) parentHeight / videoHeight;
            float scale = Math.min(scaleX, scaleY); // giữ tỉ lệ, không méo

            int newWidth = Math.round(videoWidth * scale);
            int newHeight = Math.round(videoHeight * scale);

            android.widget.FrameLayout.LayoutParams params =
                    new android.widget.FrameLayout.LayoutParams(newWidth, newHeight);
            params.gravity = android.view.Gravity.CENTER;

            videoView.setLayoutParams(params);

            mp.setLooping(false); // tuỳ bạn
        });

        videoView.start();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email=prefs.getString("userEmail",null);
        videoView.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Bạn đã xem xong video 🎉", Toast.LENGTH_SHORT).show();

            VideoviewModel.saveProgress(
                    maMedia,
                    email,
                    videoView.getDuration() / 1000,
                    true
            );
        });

    }
}