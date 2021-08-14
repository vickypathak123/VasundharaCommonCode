// Generated by view binder compiler. Do not edit!
package com.vasu.appcenter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.vasu.appcenter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoadAdBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout flNativeAdPlaceHolderBig;

  @NonNull
  public final FrameLayout flNativeAdPlaceHolderMedium;

  @NonNull
  public final FrameLayout flNativeAdPlaceHolderSmall;

  @NonNull
  public final AllScreenHeaderBinding layoutHeader;

  @NonNull
  public final Button showInterstitialAds;

  @NonNull
  public final Button showRewardInterstitialAds;

  @NonNull
  public final Button showRewardVideoAds;

  private ActivityLoadAdBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout flNativeAdPlaceHolderBig,
      @NonNull FrameLayout flNativeAdPlaceHolderMedium,
      @NonNull FrameLayout flNativeAdPlaceHolderSmall, @NonNull AllScreenHeaderBinding layoutHeader,
      @NonNull Button showInterstitialAds, @NonNull Button showRewardInterstitialAds,
      @NonNull Button showRewardVideoAds) {
    this.rootView = rootView;
    this.flNativeAdPlaceHolderBig = flNativeAdPlaceHolderBig;
    this.flNativeAdPlaceHolderMedium = flNativeAdPlaceHolderMedium;
    this.flNativeAdPlaceHolderSmall = flNativeAdPlaceHolderSmall;
    this.layoutHeader = layoutHeader;
    this.showInterstitialAds = showInterstitialAds;
    this.showRewardInterstitialAds = showRewardInterstitialAds;
    this.showRewardVideoAds = showRewardVideoAds;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoadAdBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoadAdBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_load_ad, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoadAdBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fl_native_ad_place_holder_big;
      FrameLayout flNativeAdPlaceHolderBig = ViewBindings.findChildViewById(rootView, id);
      if (flNativeAdPlaceHolderBig == null) {
        break missingId;
      }

      id = R.id.fl_native_ad_place_holder_medium;
      FrameLayout flNativeAdPlaceHolderMedium = ViewBindings.findChildViewById(rootView, id);
      if (flNativeAdPlaceHolderMedium == null) {
        break missingId;
      }

      id = R.id.fl_native_ad_place_holder_small;
      FrameLayout flNativeAdPlaceHolderSmall = ViewBindings.findChildViewById(rootView, id);
      if (flNativeAdPlaceHolderSmall == null) {
        break missingId;
      }

      id = R.id.layout_header;
      View layoutHeader = ViewBindings.findChildViewById(rootView, id);
      if (layoutHeader == null) {
        break missingId;
      }
      AllScreenHeaderBinding binding_layoutHeader = AllScreenHeaderBinding.bind(layoutHeader);

      id = R.id.show_interstitial_ads;
      Button showInterstitialAds = ViewBindings.findChildViewById(rootView, id);
      if (showInterstitialAds == null) {
        break missingId;
      }

      id = R.id.show_reward_interstitial_ads;
      Button showRewardInterstitialAds = ViewBindings.findChildViewById(rootView, id);
      if (showRewardInterstitialAds == null) {
        break missingId;
      }

      id = R.id.show_reward_video_ads;
      Button showRewardVideoAds = ViewBindings.findChildViewById(rootView, id);
      if (showRewardVideoAds == null) {
        break missingId;
      }

      return new ActivityLoadAdBinding((ConstraintLayout) rootView, flNativeAdPlaceHolderBig,
          flNativeAdPlaceHolderMedium, flNativeAdPlaceHolderSmall, binding_layoutHeader,
          showInterstitialAds, showRewardInterstitialAds, showRewardVideoAds);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
