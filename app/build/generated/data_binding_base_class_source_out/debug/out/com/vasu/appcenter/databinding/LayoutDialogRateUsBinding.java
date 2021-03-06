// Generated by view binder compiler. Do not edit!
package com.vasu.appcenter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.hsalf.smilerating.SmileRating;
import com.vasu.appcenter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutDialogRateUsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView rateBtnDismiss;

  @NonNull
  public final CardView rateCardDismiss;

  @NonNull
  public final SmileRating rateSmileRating;

  @NonNull
  public final TextView rateTvTitle;

  private LayoutDialogRateUsBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView rateBtnDismiss, @NonNull CardView rateCardDismiss,
      @NonNull SmileRating rateSmileRating, @NonNull TextView rateTvTitle) {
    this.rootView = rootView;
    this.rateBtnDismiss = rateBtnDismiss;
    this.rateCardDismiss = rateCardDismiss;
    this.rateSmileRating = rateSmileRating;
    this.rateTvTitle = rateTvTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutDialogRateUsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutDialogRateUsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_dialog_rate_us, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutDialogRateUsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rate_btn_dismiss;
      TextView rateBtnDismiss = ViewBindings.findChildViewById(rootView, id);
      if (rateBtnDismiss == null) {
        break missingId;
      }

      id = R.id.rate_card_dismiss;
      CardView rateCardDismiss = ViewBindings.findChildViewById(rootView, id);
      if (rateCardDismiss == null) {
        break missingId;
      }

      id = R.id.rate_smile_rating;
      SmileRating rateSmileRating = ViewBindings.findChildViewById(rootView, id);
      if (rateSmileRating == null) {
        break missingId;
      }

      id = R.id.rate_tv_title;
      TextView rateTvTitle = ViewBindings.findChildViewById(rootView, id);
      if (rateTvTitle == null) {
        break missingId;
      }

      return new LayoutDialogRateUsBinding((ConstraintLayout) rootView, rateBtnDismiss,
          rateCardDismiss, rateSmileRating, rateTvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
