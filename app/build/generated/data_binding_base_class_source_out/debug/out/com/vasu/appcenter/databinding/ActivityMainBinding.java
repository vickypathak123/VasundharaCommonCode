// Generated by view binder compiler. Do not edit!
package com.vasu.appcenter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.appcenter.widgets.TopsMoreAppsView;
import com.github.naz013.colorslider.ColorSlider;
import com.vasu.appcenter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout clToolbar;

  @NonNull
  public final ColorSlider colorSlider;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final ImageView ivRemoveAds;

  @NonNull
  public final Button mainBtnGallery;

  @NonNull
  public final Button mainBtnStart;

  @NonNull
  public final TopsMoreAppsView moreAppView;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView tvRemoveAds;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout clToolbar, @NonNull ColorSlider colorSlider,
      @NonNull ConstraintLayout constraintLayout, @NonNull ImageView ivRemoveAds,
      @NonNull Button mainBtnGallery, @NonNull Button mainBtnStart,
      @NonNull TopsMoreAppsView moreAppView, @NonNull TextView textView,
      @NonNull TextView tvRemoveAds) {
    this.rootView = rootView;
    this.clToolbar = clToolbar;
    this.colorSlider = colorSlider;
    this.constraintLayout = constraintLayout;
    this.ivRemoveAds = ivRemoveAds;
    this.mainBtnGallery = mainBtnGallery;
    this.mainBtnStart = mainBtnStart;
    this.moreAppView = moreAppView;
    this.textView = textView;
    this.tvRemoveAds = tvRemoveAds;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cl_toolbar;
      ConstraintLayout clToolbar = ViewBindings.findChildViewById(rootView, id);
      if (clToolbar == null) {
        break missingId;
      }

      id = R.id.color_slider;
      ColorSlider colorSlider = ViewBindings.findChildViewById(rootView, id);
      if (colorSlider == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.iv_remove_ads;
      ImageView ivRemoveAds = ViewBindings.findChildViewById(rootView, id);
      if (ivRemoveAds == null) {
        break missingId;
      }

      id = R.id.main_btn_gallery;
      Button mainBtnGallery = ViewBindings.findChildViewById(rootView, id);
      if (mainBtnGallery == null) {
        break missingId;
      }

      id = R.id.main_btn_start;
      Button mainBtnStart = ViewBindings.findChildViewById(rootView, id);
      if (mainBtnStart == null) {
        break missingId;
      }

      id = R.id.moreAppView;
      TopsMoreAppsView moreAppView = ViewBindings.findChildViewById(rootView, id);
      if (moreAppView == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.tv_remove_ads;
      TextView tvRemoveAds = ViewBindings.findChildViewById(rootView, id);
      if (tvRemoveAds == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, clToolbar, colorSlider,
          constraintLayout, ivRemoveAds, mainBtnGallery, mainBtnStart, moreAppView, textView,
          tvRemoveAds);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
