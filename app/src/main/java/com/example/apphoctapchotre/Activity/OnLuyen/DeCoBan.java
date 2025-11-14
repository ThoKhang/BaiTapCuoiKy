package com.example.apphoctapchotre.Activity.OnLuyen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.DeItem;

import java.util.ArrayList;
import java.util.List;

public class DeCoBan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_co_ban);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<DeItem> list = new ArrayList<>();
        list.add(new DeItem("Đề cơ bản số 1", 50, 9, 10));
        for (int i = 2; i <= 10; i++) {
            list.add(new DeItem("Đề cơ bản số " + i, 50, 0, 10));
        }

        DeAdapter adapter = new DeAdapter(this, list, R.drawable.gadient_decoban);
        rv.setAdapter(adapter);
    }
}