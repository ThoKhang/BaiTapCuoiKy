package com.example.apphoctapchotre.UI.Activity.Games;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.SodokuViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class sudoku extends AppCompatActivity {

    int[][] solution = new int[9][9];
    int[][] puzzle = new int[9][9];
    TextView[][] cells = new TextView[9][9];

    int selectedNumber = 0;
    int errorCount = 0;
    View selectedPickerView = null;
    private TienTrinh tienTrinh = new TienTrinh();
    private SodokuViewModel sodokuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        sodokuViewModel = new ViewModelProvider(this).get(SodokuViewModel.class);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email=prefs.getString("userEmail",null);
        if(email!=null){
            tienTrinh.setEmail(email);
            tienTrinh.setMaHoatDong("TC004");
        }
        generateSudoku();
        generatePuzzle();

        createBoardUI();
        createNumberPicker();
        MaterialButton btnTroVeTrangChu = findViewById(R.id.btnTroVeTrangChu);
        btnTroVeTrangChu.setOnClickListener(v->{
            Intent intent = new Intent(this, GiaoDienTong.class);
            startActivity(intent);
        });

        //imgback
        ImageView ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());
    }
    private void ketThucBaiLam() {
        tienTrinh.setSoCauDung(3);
        tienTrinh.setSoCauDaLam(3);
        tienTrinh.setDiemDatDuoc(50);
        tienTrinh.setDaHoanThanh(1);
        sodokuViewModel.guiTienTrinh(tienTrinh);
    }
    // --------------------------
    // TẠO SUDOKU
    // --------------------------
    void generateSudoku() {
        fill(0, 0);
    }

    boolean fill(int r, int c) {
        if (r == 9) return true;

        int nr = c == 8 ? r + 1 : r;
        int nc = c == 8 ? 0 : c + 1;

        int[] nums = {1,2,3,4,5,6,7,8,9};
        shuffle(nums);

        for (int n : nums) {
            if (isSafe(r, c, n)) {
                solution[r][c] = n;
                if (fill(nr, nc)) return true;
            }
        }

        solution[r][c] = 0;
        return false;
    }

    boolean isSafe(int r, int c, int n) {
        for (int i = 0; i < 9; i++)
            if (solution[r][i] == n || solution[i][c] == n) return false;

        int br = r - r % 3, bc = c - c % 3;
        for (int i = br; i < br + 3; i++)
            for (int j = bc; j < bc + 3; j++)
                if (solution[i][j] == n) return false;

        return true;
    }

    void shuffle(int[] a) {
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = r.nextInt(a.length);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    // --------------------------
    // TẠO PUZZLE
    // --------------------------
    void generatePuzzle() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                puzzle[i][j] = solution[i][j];

        Random r = new Random();
        for (int k = 0; k < 40; k++) {
            puzzle[r.nextInt(9)][r.nextInt(9)] = 0;
        }
    }

    // --------------------------
    // UI BOARD
    // --------------------------
    void createBoardUI() {
        GridLayout grid = findViewById(R.id.gridSudoku);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                View cellView = getLayoutInflater().inflate(R.layout.item_cell, grid, false);
                TextView cell = cellView.findViewById(R.id.txtCell);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                params.setMargins(2, 2, 2, 2);
                cellView.setLayoutParams(params);

                if (puzzle[i][j] != 0) {
                    cell.setText(String.valueOf(puzzle[i][j]));
                    cell.setBackgroundResource(R.drawable.bg_cell_fixed);
                } else {
                    final int r = i, c = j;
                    cell.setOnClickListener(v -> onCellClick(cell, r, c));
                }

                cells[i][j] = cell;
                grid.addView(cellView);
            }
        }
    }

    void onCellClick(TextView cell, int r, int c) {
        if (selectedNumber == 0) return;

        if (selectedNumber == solution[r][c]) {
            cell.setText(String.valueOf(selectedNumber));
            cell.setBackgroundResource(R.drawable.bg_cell);

            puzzle[r][c] = selectedNumber;

            if (ketthuc()) {
                Toast.makeText(this, "🎉 Hoàn thành Sudoku!", Toast.LENGTH_LONG).show();
                ketThucBaiLam();
                finish();
            }
        } else {
            errorCount++;
            updateError();

            cell.setBackgroundColor(Color.parseColor("#FF9999"));

            if (errorCount >= 3) {
                Toast.makeText(this, "Bạn đã thua! Sai quá 3 lần!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    boolean ketthuc() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (puzzle[i][j] == 0)
                    return false;
        return true;
    }

    // --------------------------
    // NUMBER PICKER
    // --------------------------
    void createNumberPicker() {
        GridLayout picker = findViewById(R.id.numberPicker);

        for (int n = 1; n <= 9; n++) {
            View v = getLayoutInflater().inflate(R.layout.item_number, picker, false);
            TextView t = v.findViewById(R.id.txtNumber);
            t.setText(String.valueOf(n));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = dp(48);
            params.columnSpec = GridLayout.spec(n - 1, 1f);
            params.setMargins(4, 4, 4, 4);
            v.setLayoutParams(params);

            final int num = n;
            t.setOnClickListener(view -> {
                selectedNumber = num;
                highlightSelectedPicker(t);
            });

            picker.addView(v);
        }
    }

    void highlightSelectedPicker(TextView v) {
        // reset cho số đang chọn trước đó
        if (selectedPickerView != null)
            selectedPickerView.setBackgroundResource(R.drawable.bg_number);

        // đặt drawable khi chọn
        v.setBackgroundResource(R.drawable.bg_number_selected);
        selectedPickerView = v;
    }

    void updateError() {
        TextView txt = findViewById(R.id.txtErrors);
        txt.setText("Sai: " + errorCount + "/3");
    }

    int dp(int v) {
        return Math.round(getResources().getDisplayMetrics().density * v);
    }
}
