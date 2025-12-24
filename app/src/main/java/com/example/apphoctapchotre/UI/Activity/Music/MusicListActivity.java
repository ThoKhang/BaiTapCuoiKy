package com.example.apphoctapchotre.UI.Activity.Music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.Repository.MediaRepository;
import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Adapter.Music.MusicAdapter;
import com.example.apphoctapchotre.UI.Service.MusicService;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView rvMusic;
    private MediaRepository repository;
    private MusicAdapter adapter;

    private MusicService musicService;
    private boolean bound = false;

    private final MusicService.PlaybackListener playbackListener = new MusicService.PlaybackListener() {
        @Override
        public void onPlaybackChanged(int index, boolean isPlaying) {
            runOnUiThread(() -> adapter.setPlayingState(index, isPlaying));
        }

        @Override
        public void onTrackChanged(int index) {
            runOnUiThread(() -> adapter.setPlayingState(index, musicService != null && musicService.isPlaying()));
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

            // sync ngay khi bind
            adapter.setPlayingState(
                    musicService.getCurrentIndex(),
                    musicService.isPlaying()
            );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (musicService != null) {
                musicService.removePlaybackListener(playbackListener);
            }
            bound = false;
        }
    };

    // ================= LIFECYCLE =================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        rvMusic = findViewById(R.id.rvMusic);
        repository = new MediaRepository();

        findViewById(R.id.back).setOnClickListener(v -> finish());

        adapter = new MusicAdapter(new ArrayList<>(), (media, position) -> {
            if (!bound || musicService == null) return;

            // ✅ Service quyết định toggle hay play bài mới (không reset nếu click bài đang phát)
            musicService.onUserClickFromList(new ArrayList<>(adapter.getList()), position);

            // ✅ mở detail để điều khiển
            openDetail(position);
        });

        rvMusic.setLayoutManager(new LinearLayoutManager(this));
        rvMusic.setAdapter(adapter);

        loadMusic();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent); // giữ service sống
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // sync lại khi quay về list
        if (bound && musicService != null) {
            adapter.setPlayingState(musicService.getCurrentIndex(), musicService.isPlaying());
        }
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

    // ================= OPEN DETAIL =================

    private void openDetail(int index) {
        Intent intent = new Intent(this, DetailMusicActivity.class);
        intent.putExtra("list", new ArrayList<>(adapter.getList()));
        intent.putExtra("index", index);
        startActivity(intent);
    }

    // ================= DATA =================

    private void loadMusic() {
        repository.getAudioList().observe(this, list -> {
            if (list == null) {
                Log.e("MUSIC", "Danh sách nhạc = null");
                return;
            }

            adapter.setData(list);

            if (bound && musicService != null) {
                adapter.setPlayingState(
                        musicService.getCurrentIndex(),
                        musicService.isPlaying()
                );
            }
        });
    }
}
