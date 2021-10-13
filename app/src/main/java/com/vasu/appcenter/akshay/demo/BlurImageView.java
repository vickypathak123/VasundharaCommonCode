package com.vasu.appcenter.akshay.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import com.vasu.appcenter.R;

public class BlurImageView extends AppCompatImageView {


    private float defaultBitmapScale = 0.1f;
    private static final float MAX_RADIUS = 25.0f;
    private static final float MIN_RADIUS = 0.1f;
    int width = 0;
    int height = 0;
    Drawable imageOnView;


    public BlurImageView(Context context) {
        super(context);
        init(null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setImageDrawable(new BitmapDrawable(getResources(), bm));
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        //if (imageOnView == null)
            imageOnView = drawable;
    }

    private void init(AttributeSet attrs) {

        if (attrs != null && getDrawable() != null) {

            imageOnView = getDrawable();

            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BlurImageView, 0, 0);

            float radius = typedArray.getFloat(R.styleable.BlurImageView_radius, 0.7f);

            setBlur(radius);

            typedArray.recycle();

            setScaleType(ScaleType.CENTER_CROP);
        }
    }


    public void setBitmapScale(float bitmapScale) {
        this.defaultBitmapScale = bitmapScale;
    }

    public void setBlur(float radius) {

        if (imageOnView == null)
            imageOnView = getDrawable();

        // max radius = 25
        if (radius > MIN_RADIUS && radius <= MAX_RADIUS) {

            Bitmap blurred = blurRenderScript(((BitmapDrawable) imageOnView).getBitmap(), radius);
            setImageBitmap(blurred);
            invalidate();

        } else if (radius == 0) {
            setImageDrawable(imageOnView);
            invalidate();

        } else
            Log.e("BLUR", "actualRadius invalid: " + radius);
    }


    private Bitmap blurRenderScript(Bitmap smallBitmap, float radius) {

            int width = Math.round(smallBitmap.getWidth() * defaultBitmapScale);
            int height = Math.round(smallBitmap.getHeight() * defaultBitmapScale);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(smallBitmap, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {

            RenderScript renderScript = RenderScript.create(getContext());
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
            theIntrinsic.setRadius(radius);
//            theIntrinsic.setRadius(0.7f);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }

        return outputBitmap;
    }
}