package com.example.gallery.imagecrop;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.gallery.R;

public class CropImageActivity extends FragmentActivity {

    private static final String TAG = CropImageActivity.class.getSimpleName();

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, CropImageActivity.class);
    }

    public static Intent createIntent(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, CropImageActivity.class);
        intent.setData(uri);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, CropImageFragment.newInstance()).commit();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void startResultActivity(Uri uri) {
        if (isFinishing()) return;
        // Start ResultActivity
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(RESULT_OK, intent);
        finish();

        //  startActivity(ResultActivity.createIntent(this, uri));
    }
}
