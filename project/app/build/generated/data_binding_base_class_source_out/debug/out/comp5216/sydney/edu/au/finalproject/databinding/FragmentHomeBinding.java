// Generated by view binder compiler. Do not edit!
package comp5216.sydney.edu.au.finalproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import comp5216.sydney.edu.au.finalproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView postRefresh;

  @NonNull
  public final TextView postSort;

  @NonNull
  public final RecyclerView rv;

  @NonNull
  public final ImageView search;

  private FragmentHomeBinding(@NonNull LinearLayout rootView, @NonNull ImageView postRefresh,
      @NonNull TextView postSort, @NonNull RecyclerView rv, @NonNull ImageView search) {
    this.rootView = rootView;
    this.postRefresh = postRefresh;
    this.postSort = postSort;
    this.rv = rv;
    this.search = search;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.post_refresh;
      ImageView postRefresh = ViewBindings.findChildViewById(rootView, id);
      if (postRefresh == null) {
        break missingId;
      }

      id = R.id.post_sort;
      TextView postSort = ViewBindings.findChildViewById(rootView, id);
      if (postSort == null) {
        break missingId;
      }

      id = R.id.rv;
      RecyclerView rv = ViewBindings.findChildViewById(rootView, id);
      if (rv == null) {
        break missingId;
      }

      id = R.id.search;
      ImageView search = ViewBindings.findChildViewById(rootView, id);
      if (search == null) {
        break missingId;
      }

      return new FragmentHomeBinding((LinearLayout) rootView, postRefresh, postSort, rv, search);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
