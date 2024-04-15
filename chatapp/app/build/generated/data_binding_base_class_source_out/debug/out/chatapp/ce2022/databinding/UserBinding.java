// Generated by view binder compiler. Do not edit!
package chatapp.ce2022.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import chatapp.ce2022.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class UserBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView status;

  @NonNull
  public final CircleImageView userImg;

  @NonNull
  public final TextView userName;

  private UserBinding(@NonNull ConstraintLayout rootView, @NonNull TextView status,
      @NonNull CircleImageView userImg, @NonNull TextView userName) {
    this.rootView = rootView;
    this.status = status;
    this.userImg = userImg;
    this.userName = userName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UserBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.user, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.status;
      TextView status = ViewBindings.findChildViewById(rootView, id);
      if (status == null) {
        break missingId;
      }

      id = R.id.user_img;
      CircleImageView userImg = ViewBindings.findChildViewById(rootView, id);
      if (userImg == null) {
        break missingId;
      }

      id = R.id.user_name;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      return new UserBinding((ConstraintLayout) rootView, status, userImg, userName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
