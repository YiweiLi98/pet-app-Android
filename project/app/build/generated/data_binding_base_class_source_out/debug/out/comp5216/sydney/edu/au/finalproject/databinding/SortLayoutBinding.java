// Generated by view binder compiler. Do not edit!
package comp5216.sydney.edu.au.finalproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import comp5216.sydney.edu.au.finalproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SortLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView sort1;

  @NonNull
  public final TextView sort2;

  private SortLayoutBinding(@NonNull LinearLayout rootView, @NonNull TextView sort1,
      @NonNull TextView sort2) {
    this.rootView = rootView;
    this.sort1 = sort1;
    this.sort2 = sort2;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SortLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SortLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.sort_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SortLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.sort1;
      TextView sort1 = ViewBindings.findChildViewById(rootView, id);
      if (sort1 == null) {
        break missingId;
      }

      id = R.id.sort2;
      TextView sort2 = ViewBindings.findChildViewById(rootView, id);
      if (sort2 == null) {
        break missingId;
      }

      return new SortLayoutBinding((LinearLayout) rootView, sort1, sort2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}