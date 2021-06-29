package com.example.gallery.internal.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @author: Jignesh N Patel
 * @date: 25-Feb-2021 9:24 AM
 */
public class Utils {

    public static boolean isNotValidImage(ImageView thumbnail) {
        Drawable drawable = thumbnail.getDrawable();
        return (drawable instanceof ColorDrawable);
    }
}
