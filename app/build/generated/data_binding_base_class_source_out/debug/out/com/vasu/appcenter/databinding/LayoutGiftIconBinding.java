// Generated by view binder compiler. Do not edit!
package com.vasu.appcenter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.vasu.appcenter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutGiftIconBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout clGift;

  @NonNull
  public final AppCompatImageView ivGift;

  @NonNull
  public final AppCompatImageView ivGiftBlast;

  private LayoutGiftIconBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout clGift, @NonNull AppCompatImageView ivGift,
      @NonNull AppCompatImageView ivGiftBlast) {
    this.rootView = rootView;
    this.clGift = clGift;
    this.ivGift = ivGift;
    this.ivGiftBlast = ivGiftBlast;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutGiftIconBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutGiftIconBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_gift_icon, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutGiftIconBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout clGift = (ConstraintLayout) rootView;

      id = R.id.iv_gift;
      AppCompatImageView ivGift = ViewBindings.findChildViewById(rootView, id);
      if (ivGift == null) {
        break missingId;
      }

      id = R.id.iv_gift_blast;
      AppCompatImageView ivGiftBlast = ViewBindings.findChildViewById(rootView, id);
      if (ivGiftBlast == null) {
        break missingId;
      }

      return new LayoutGiftIconBinding((ConstraintLayout) rootView, clGift, ivGift, ivGiftBlast);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}