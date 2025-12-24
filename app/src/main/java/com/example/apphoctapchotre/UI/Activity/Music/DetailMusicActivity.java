package com.example.apphoctapchotre.UI.Activity.Music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Service.MusicService;

import java.util.ArrayList;

public class DetailMusicActivity extends AppCompatActivity {

    private TextView txtTitle, txtHeaderTitle, txtHeaderSub, Loi;
    private SeekBar seekBar;
    private ImageButton btnPlayPause, btnNext, btnPrev;

    private ExoPlayer player;
    private MusicService musicService;
    private boolean bound = false;

    private ArrayList<Media> playlist;
    private int startIndex = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final MusicService.PlaybackListener playbackListener = new MusicService.PlaybackListener() {
        @Override
        public void onPlaybackChanged(int index, boolean isPlaying) {
            runOnUiThread(() -> syncUIImmediately());
        }

        @Override
        public void onTrackChanged(int index) {
            runOnUiThread(() -> syncUIImmediately());
        }
    };

    // ================= SERVICE CONNECTION =================

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            bound = true;

            musicService.addPlaybackListener(playbackListener);

            // ✅ chỉ set playlist nếu cần (không reset nếu trùng)
            musicService.ensurePlaylist(playlist);

            player = musicService.getPlayer();

            // Nếu chưa có bài nào đang phát (vd mở detail trực tiếp) → play bài startIndex
            if (musicService.getCurrentIndex() < 0 && playlist != null && !playlist.isEmpty()) {
                musicService.playAt(startIndex);
            } else {
                // Nếu user mở detail cho bài khác current → play bài đó
                int current = musicService.getCurrentIndex();
                if (startIndex != current && startIndex >= 0 && playlist != null && startIndex < playlist.size()) {
                    musicService.playAt(startIndex);
                }
            }

            setupControls();
            setupPlayerListener();
            syncUIImmediately();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    // ================= LIFECYCLE =================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        txtTitle = findViewById(R.id.txtTitle);
        txtHeaderTitle = findViewById(R.id.txtHeaderTitle);
        txtHeaderSub = findViewById(R.id.txtHeaderSub);
        Loi = findViewById(R.id.Loi);

        seekBar = findViewById(R.id.seekBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        playlist = (ArrayList<Media>) getIntent().getSerializableExtra("list");
        startIndex = getIntent().getIntExtra("index", 0);

        if (playlist == null || playlist.isEmpty()) finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            if (musicService != null) {
                musicService.removePlaybackListener(playbackListener);
            }
            unbindService(connection);
            bound = false;
        }
    }

    // ================= PLAYER LISTENER =================

    private void setupPlayerListener() {
        if (player == null) return;

        player.addListener(new Player.Listener() {

            @Override
            public void onMediaItemTransition(
                    androidx.media3.common.MediaItem mediaItem, int reason) {
                syncUIImmediately();
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                syncUIImmediately();
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    seekBar.setMax((int) (player.getDuration() / 1000));
                    updateSeekBar();
                    syncUIImmediately();
                }
            }
        });
    }

    // ================= CONTROLS =================

    private void setupControls() {

        btnPlayPause.setOnClickListener(v -> {
            if (musicService == null) return;

            musicService.toggle();
            // icon sẽ được sync bằng callback + listener, nhưng gọi luôn cho mượt
            syncUIImmediately();
        });

        btnNext.setOnClickListener(v -> {
            if (musicService == null) return;
            musicService.next(); // ✅ service làm + notify
        });

        btnPrev.setOnClickListener(v -> {
            if (musicService == null) return;
            musicService.previous(); // ✅ service làm + notify
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress * 1000L);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // ================= UI SYNC =================

    private void syncUIImmediately() {
        if (musicService == null || player == null || playlist == null) return;

        int index = musicService.getCurrentIndex();
        if (index < 0 || index >= playlist.size()) return;

        Media current = playlist.get(index);

        txtHeaderTitle.setText(current.getTieuDe());
        txtTitle.setText(current.getTieuDe());

        // theo yêu cầu bạn: Loi = mô tả, txtHeaderSub = loại
        Loi.setText(current.getMoTa() != null ? current.getMoTa() : "");
        txtHeaderSub.setText(current.getLoaiMedia() != null ? current.getLoaiMedia() : "");

        long dur = player.getDuration();
        if (dur > 0) seekBar.setMax((int) (dur / 1000));

        seekBar.setProgress((int) (musicService.getCurrentPosition() / 1000));

        // ✅ ICON PHẢI THEO isPlaying hiện tại
        btnPlayPause.setImageResource(
                musicService.isPlaying()
                        ? android.R.drawable.ic_media_pause
                        : android.R.drawable.ic_media_play
        );
    }

    // ================= SEEK BAR =================

    private void updateSeekBar() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    seekBar.setProgress((int) (player.getCurrentPosition() / 1000));
                    handler.postDelayed(this, 1000);
                }
            }
        }, 300);
    }
}
