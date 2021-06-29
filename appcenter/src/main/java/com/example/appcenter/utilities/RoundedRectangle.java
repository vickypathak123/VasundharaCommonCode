package com.example.appcenter.utilities;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by jinesh on 24/5/17.
 */

public class RoundedRectangle extends Drawable {
    private Paint rectPaint;
    private RectF drawableBounds;

    public RoundedRectangle(int rectBackground) {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(rectBackground);
        drawableBounds = new RectF();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        drawableBounds.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        canvas.drawRoundRect(drawableBounds, 35, 35, rectPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        rectPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        rectPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}