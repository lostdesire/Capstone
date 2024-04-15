package chatapp.ce2022;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.widget.ImageView;


import chatapp.ce2022.databinding.ActivitySignUpBinding;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp_Activity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySignUpBinding binding;
    public static Context contextSignin;



    Uri uri;
    CircleImageView img;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(uri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    img.setImageBitmap(imgBitmap);
                    img.setTooltipText(uri.toString());
                    instream.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextSignin = this;


        EditText id, pw, name;
        setContentView(R.layout.activity_sign_up);
        Button btnReg = (Button) findViewById(R.id.btnReg);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.35.15:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        id = (EditText) findViewById(R.id.enterId);
        pw = (EditText) findViewById(R.id.enterPw);
        name = (EditText) findViewById(R.id.enterName);

        img = findViewById(R.id.profile_image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(in,101);
            }
        });





        btnReg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String strId = id.getText().toString();
                String strPw = pw.getText().toString();
                String strName = name.getText().toString();

                HashMap<String, Object> input = new HashMap<>();
                input.put("id", strId);
                input.put("password", strPw);
                input.put("name", strName);
                input.put("msg", " ");
                input.put("img", findViewById(R.id.profile_image).getTooltipText().toString());
                retrofitAPI.postData(input).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if(response.isSuccessful()){
                            Post body = response.body();
                            Log.d("TEST", "Post Success");
                            Log.d("TEST22",findViewById(R.id.profile_image).getTooltipText().toString());
                            Toast.makeText(SignUp_Activity.this, "회원가입 되었습니다.", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(SignUp_Activity.this, Login_Activity.class);
                            startActivity(in);
                            finish();

                        }
                        else{
                            Log.d("TEST","Post Failed");
                        }

                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.d("TEST","Post Failed2");
                    }
                });


            }
        });

    }
}