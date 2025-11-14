package com.example.apphoctapchotre.Utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class StrokeTextView extends AppCompatTextView {

    private Paint strokePaint;

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(8); // độ dày viền
        strokePaint.setColor(Color.WHITE); // màu viền
        strokePaint.setTextSize(getTextSize());
        strokePaint.setTypeface(getTypeface());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Vẽ viền
        CharSequence text = getText();
        if (text != null) {
            float x = getPaddingLeft();
            float y = getBaseline();
            canvas.drawText(text.toString(), x, y, strokePaint);
        }

        // Vẽ chữ chính
        getPaint().setStyle(Paint.Style.FILL);
        getPaint().setColor(Color.parseColor("#15DCEF")); // màu chữ chính
        super.onDraw(canvas);
    }
}
