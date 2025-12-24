package com.example.apphoctapchotre.UI.Activity.Music;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;

public class DetailMusicActivity extends AppCompatActivity {

    private TextView txtTitle;
    private SeekBar seekBar;
    private ImageButton btnPlayPause;

    private ExoPlayer player;
    private Media media;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        txtTitle = findViewById(R.id.txtTitle);
        seekBar = findViewById(R.id.seekBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        media = (Media) getIntent().getSerializableExtra("media");
        if (media == null) {
            finish();
            return;
        }

        txtTitle.setText(media.getTieuDe());

        setupPlayer();
        setupControls();
        playMusic();
    }

    // ================= PLAYER =================

    private void setupPlayer() {
        player = new ExoPlayer.Builder(this).build();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    seekBar.setMax((int) (player.getDuration() / 1000));
                    updateSeekbar();
                }
            }
        });
    }

    //  FIX 404 Ở ĐÂY
    private void playMusic() {

        String url = "http://10.0.2.2:8080/" + media.getDuongDanFile();
        Log.e("PLAYER_URL", url);

        MediaItem item = MediaItem.fromUri(Uri.parse(url));
        player.setMediaItem(item);
        player.prepare();
        player.play();

        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
    }


    // ================= CONTROLS =================

    private void setupControls() {
        btnPlayPause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            } else {
                player.play();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
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

    // ================= SEEK BAR =================

    private void updateSeekbar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    seekBar.setProgress((int) (player.getCurrentPosition() / 1000));
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    // ================= LIFECYCLE =================

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
