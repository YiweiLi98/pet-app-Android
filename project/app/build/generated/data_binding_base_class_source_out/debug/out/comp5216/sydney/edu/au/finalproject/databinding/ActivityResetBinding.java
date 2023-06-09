// Generated by view binder compiler. Do not edit!
package comp5216.sydney.edu.au.finalproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import comp5216.sydney.edu.au.finalproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityResetBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnCancel;

  @NonNull
  public final Button btnOk;

  @NonNull
  public final Button btnSendCode;

  @NonNull
  public final EditText inputNewPassword;

  @NonNull
  public final EditText inputResetEmail;

  @NonNull
  public final EditText inputResetUsername;

  @NonNull
  public final EditText inputVerification;

  @NonNull
  public final TextView textView;

  private ActivityResetBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnCancel,
      @NonNull Button btnOk, @NonNull Button btnSendCode, @NonNull EditText inputNewPassword,
      @NonNull EditText inputResetEmail, @NonNull EditText inputResetUsername,
      @NonNull EditText inputVerification, @NonNull TextView textView) {
    this.rootView = rootView;
    this.btnCancel = btnCancel;
    this.btnOk = btnOk;
    this.btnSendCode = btnSendCode;
    this.inputNewPassword = inputNewPassword;
    this.inputResetEmail = inputResetEmail;
    this.inputResetUsername = inputResetUsername;
    this.inputVerification = inputVerification;
    this.textView = textView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityResetBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityResetBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_reset, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityResetBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCancel;
      Button btnCancel = ViewBindings.findChildViewById(rootView, id);
      if (btnCancel == null) {
        break missingId;
      }

      id = R.id.btnOk;
      Button btnOk = ViewBindings.findChildViewById(rootView, id);
      if (btnOk == null) {
        break missingId;
      }

      id = R.id.btnSendCode;
      Button btnSendCode = ViewBindings.findChildViewById(rootView, id);
      if (btnSendCode == null) {
        break missingId;
      }

      id = R.id.inputNewPassword;
      EditText inputNewPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputNewPassword == null) {
        break missingId;
      }

      id = R.id.inputResetEmail;
      EditText inputResetEmail = ViewBindings.findChildViewById(rootView, id);
      if (inputResetEmail == null) {
        break missingId;
      }

      id = R.id.inputResetUsername;
      EditText inputResetUsername = ViewBindings.findChildViewById(rootView, id);
      if (inputResetUsername == null) {
        break missingId;
      }

      id = R.id.inputVerification;
      EditText inputVerification = ViewBindings.findChildViewById(rootView, id);
      if (inputVerification == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new ActivityResetBinding((ConstraintLayout) rootView, btnCancel, btnOk, btnSendCode,
          inputNewPassword, inputResetEmail, inputResetUsername, inputVerification, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
