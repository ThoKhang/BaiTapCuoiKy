package com.example.apphoctapchotre.UI.Activity.Video;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.UI.Adapter.Video.MediaAdapter;
import com.example.apphoctapchotre.UI.ViewModel.MediaViewModel;

import java.util.ArrayList;

public class Video extends AppCompatActivity {

    private MediaViewModel viewModel;
    private MediaAdapter adapter;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        back =findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, GiaoDienTong.class);
            startActivity(intent);
        });

        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });

        RecyclerView rvVideo = findViewById(R.id.rvVideo);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MediaAdapter(new ArrayList<>(), media ->
                Toast.makeText(this, media.getTieuDe(), Toast.LENGTH_SHORT).show()
        );
        rvVideo.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        viewModel.getVideoList().observe(this, mediaList -> {
            if (mediaList != null) {
                adapter = new MediaAdapter(mediaList, media -> {});
                rvVideo.setAdapter(adapter);
            }
        });
    }
}
