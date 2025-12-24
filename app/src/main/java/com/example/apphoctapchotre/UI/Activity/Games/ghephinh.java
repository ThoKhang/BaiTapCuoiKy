package com.example.apphoctapchotre.UI.Activity.Games;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.GhepHinhViewModel;

import java.util.Random;

public class ghephinh extends AppCompatActivity {

    private GridLayout grid;
    private Button btnNext;
    private TextView txtLevel;
    private ImageButton ibtnBack;
    private TienTrinh tienTrinh = new TienTrinh();
    private GhepHinhViewModel ghepHinhViewModel;
    int[] imageList = {
            R.drawable.bosua,
            R.drawable.thochamhoc,
            R.drawable.caheo
    };

    int currentImageIndex = 0;

    Bitmap[] pieces = new Bitmap[9];
    Bitmap[] correct = new Bitmap[9];
    ImageView[] imageViews = new ImageView[9];

    int firstPick = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghephinh);

        ghepHinhViewModel = new ViewModelProvider(this)
                .get(GhepHinhViewModel.class);

        grid = findViewById(R.id.gridPuzzle);
        btnNext = findViewById(R.id.btnNext);
        txtLevel = findViewById(R.id.txtLevel);
        ibtnBack = findViewById(R.id.ibtnBack);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email=prefs.getString("userEmail",null);
        if(email!=null){
            tienTrinh.setEmail(email);
            tienTrinh.setMaHoatDong("TC005");
        }
        ibtnBack.setOnClickListener(v -> finish());

        loadPuzzle();

        btnNext.setOnClickListener(v -> nextImage());
    }

    void loadPuzzle() {
        btnNext.setEnabled(false);
        firstPick = -1;

        txtLevel.setText("Hình " + (currentImageIndex + 1) + " / " + imageList.length);

        splitImage(imageList[currentImageIndex]);
        shufflePieces();
        createBoard();
    }

    void splitImage(int resId) {
        Bitmap original = BitmapFactory.decodeResource(getResources(), resId);

        int rows = 3, cols = 3;
        int w = original.getWidth() / cols;
        int h = original.getHeight() / rows;

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Bitmap piece = Bitmap.createBitmap(original, j * w, i * h, w, h);
                pieces[index] = piece;
                correct[index] = piece;
                index++;
            }
        }
    }

    void shufflePieces() {
        Random r = new Random();
        for (int i = 0; i < pieces.length; i++) {
            int j = r.nextInt(pieces.length);
            Bitmap t = pieces[i];
            pieces[i] = pieces[j];
            pieces[j] = t;
        }
    }

    void createBoard() {
        grid.removeAllViews();

        for (int i = 0; i < 9; i++) {
            ImageView img = new ImageView(this);
            img.setImageBitmap(pieces[i]);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.setMargins(4, 4, 4, 4);
            img.setLayoutParams(params);

            final int index = i;
            img.setOnClickListener(v -> onPieceClick(index));

            imageViews[i] = img;
            grid.addView(img);
        }
    }

    void onPieceClick(int index) {
        if (firstPick == -1) {
            firstPick = index;
        } else {
            swap(firstPick, index);
            firstPick = -1;
            checkWin();
        }
    }

    void swap(int a, int b) {
        Bitmap t = pieces[a];
        pieces[a] = pieces[b];
        pieces[b] = t;

        imageViews[a].setImageBitmap(pieces[a]);
        imageViews[b].setImageBitmap(pieces[b]);
    }

    void checkWin() {
        for (int i = 0; i < 9; i++) {
            if (pieces[i] != correct[i]) return;
        }
        Toast.makeText(this, "✔ Ghép đúng! Bấm Tiếp tục", Toast.LENGTH_SHORT).show();
        btnNext.setEnabled(true);
    }


    void nextImage() {
        currentImageIndex++;
        if (currentImageIndex >= imageList.length) {
            Toast.makeText(this, "🎉 Hoàn thành tất cả hình!", Toast.LENGTH_LONG).show();
            ketThucBaiLam();
            finish();
        } else {
            loadPuzzle();
        }
    }

    private void ketThucBaiLam() {
        tienTrinh.setSoCauDung(3);
        tienTrinh.setSoCauDaLam(3);
        tienTrinh.setDiemDatDuoc(50);
        tienTrinh.setDaHoanThanh(1);
        ghepHinhViewModel.guiTienTrinh(tienTrinh);
    }
}