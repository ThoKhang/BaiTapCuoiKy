package com.example.apphoctapchotre.UI.Activity.Music;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.Repository.MediaRepository;
import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Adapter.Music.MusicAdapter;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView rvMusic;
    private MediaRepository repository;
    private MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        rvMusic = findViewById(R.id.rvMusic);

        repository = new MediaRepository();

        adapter = new MusicAdapter(new ArrayList<>(), this::openDetail);
        rvMusic.setLayoutManager(new LinearLayoutManager(this));
        rvMusic.setAdapter(adapter);

        loadMusic();
    }

    // ================= OPEN DETAIL =================

    private void openDetail(Media media) {
        Intent intent = new Intent(this, DetailMusicActivity.class);
        intent.putExtra("media", media);
        startActivity(intent);
    }

    // ================= DATA =================

    private void loadMusic() {
        repository.getAudioList().observe(this, list -> {
            if (list == null) {
                Log.e("MUSIC", "Danh sách nhạc = null");
                return;
            }

            Log.e("MUSIC", "Số bài hát: " + list.size());

            adapter.setData(list);
        });
    }

}
