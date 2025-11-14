package com.example.apphoctapchotre.Activity.OnLuyen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.DeItem;

import java.util.ArrayList;
import java.util.List;

public class DeNangCao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_nang_cao);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<DeItem> list = new ArrayList<>();
        list.add(new DeItem("Đề nâng cao số 1", 100, 9, 10));
        for (int i = 2; i <= 10; i++) {
            list.add(new DeItem("Đề nâng cao số " + i, 100, 0, 10));
        }

        rv.setAdapter(new DeAdapter(this, list, R.drawable.gadient_denangcao));
    }
}
