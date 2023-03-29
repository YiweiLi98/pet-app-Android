// Generated by view binder compiler. Do not edit!
package comp5216.sydney.edu.au.finalproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import comp5216.sydney.edu.au.finalproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentNewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnNewPet;

  @NonNull
  public final Button btnNewPost;

  private FragmentNewBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnNewPet,
      @NonNull Button btnNewPost) {
    this.rootView = rootView;
    this.btnNewPet = btnNewPet;
    this.btnNewPost = btnNewPost;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_new, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentNewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnNewPet;
      Button btnNewPet = ViewBindings.findChildViewById(rootView, id);
      if (btnNewPet == null) {
        break missingId;
      }

      id = R.id.btnNewPost;
      Button btnNewPost = ViewBindings.findChildViewById(rootView, id);
      if (btnNewPost == null) {
        break missingId;
      }

      return new FragmentNewBinding((ConstraintLayout) rootView, btnNewPet, btnNewPost);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
