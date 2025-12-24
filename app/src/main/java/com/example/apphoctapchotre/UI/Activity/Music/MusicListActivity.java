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

    // ================= SERVICE CONNECTION =================

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            bound = true;

            adapter.setPlayingState(
                    musicService.getCurrentIndex(),
                    musicService.isPlaying()
            );
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
        setContentView(R.layout.activity_music);

        rvMusic = findViewById(R.id.rvMusic);
        repository = new MediaRepository();

        findViewById(R.id.back).setOnClickListener(v -> finish());

        adapter = new MusicAdapter(new ArrayList<>(), (media, position) -> {
            if (!bound || musicService == null) return;

            int current = musicService.getCurrentIndex();

            if (position == current) {
                if (musicService.isPlaying()) {
                    musicService.getPlayer().pause();
                } else {
                    musicService.getPlayer().play();
                }
            } else {
                musicService.getPlayer().seekTo(position, 0);
            }

            openDetail(position);

            adapter.setPlayingState(
                    musicService.getCurrentIndex(),
                    musicService.isPlaying()
            );
        });

        rvMusic.setLayoutManager(new LinearLayoutManager(this));
        rvMusic.setAdapter(adapter);

        loadMusic();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent); // 🔥 giữ service sống
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
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
