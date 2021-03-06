// Generated by view binder compiler. Do not edit!
package com.vasu.appcenter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.vasu.appcenter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityTestBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSpeak;

  @NonNull
  public final EditText etText;

  private ActivityTestBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnSpeak,
      @NonNull EditText etText) {
    this.rootView = rootView;
    this.btnSpeak = btnSpeak;
    this.etText = etText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTestBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTestBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_test, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTestBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_speak;
      Button btnSpeak = ViewBindings.findChildViewById(rootView, id);
      if (btnSpeak == null) {
        break missingId;
      }

      id = R.id.et_text;
      EditText etText = ViewBindings.findChildViewById(rootView, id);
      if (etText == null) {
        break missingId;
      }

      return new ActivityTestBinding((ConstraintLayout) rootView, btnSpeak, etText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
