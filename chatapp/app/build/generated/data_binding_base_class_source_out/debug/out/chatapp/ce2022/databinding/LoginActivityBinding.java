// Generated by view binder compiler. Do not edit!
package chatapp.ce2022.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import chatapp.ce2022.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LoginActivityBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView appTitle;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final Button btnReg;

  @NonNull
  public final EditText enterId;

  @NonNull
  public final EditText enterPw;

  @NonNull
  public final ProgressBar progressBar;

  private LoginActivityBinding(@NonNull ConstraintLayout rootView, @NonNull TextView appTitle,
      @NonNull Button btnLogin, @NonNull Button btnReg, @NonNull EditText enterId,
      @NonNull EditText enterPw, @NonNull ProgressBar progressBar) {
    this.rootView = rootView;
    this.appTitle = appTitle;
    this.btnLogin = btnLogin;
    this.btnReg = btnReg;
    this.enterId = enterId;
    this.enterPw = enterPw;
    this.progressBar = progressBar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LoginActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LoginActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.login_activity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LoginActivityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appTitle;
      TextView appTitle = ViewBindings.findChildViewById(rootView, id);
      if (appTitle == null) {
        break missingId;
      }

      id = R.id.btnLogin;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.btnReg;
      Button btnReg = ViewBindings.findChildViewById(rootView, id);
      if (btnReg == null) {
        break missingId;
      }

      id = R.id.enterId;
      EditText enterId = ViewBindings.findChildViewById(rootView, id);
      if (enterId == null) {
        break missingId;
      }

      id = R.id.enterPw;
      EditText enterPw = ViewBindings.findChildViewById(rootView, id);
      if (enterPw == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      return new LoginActivityBinding((ConstraintLayout) rootView, appTitle, btnLogin, btnReg,
          enterId, enterPw, progressBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}