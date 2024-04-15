package chatapp.ce2022;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_List extends AppCompatActivity {
    private Activity mActivity;
    public static Context contextUserList;
    EditText name, status;
    Uri uri;
    CircleImageView img;

    public void setImg(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,101);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(uri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    img.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        Button btnHome = (Button) findViewById(R.id.btn_chat);
        Button btnChat = (Button) findViewById(R.id.btn_user);

        name = (EditText)findViewById(R.id.user_name);
        status = (EditText)findViewById(R.id.status);

        img = findViewById(R.id.user_img);
        ContentResolver resolver = getContentResolver();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.35.15:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String url = intent.getStringExtra("uid");
        UserInfo info = new UserInfo();

        Log.d("TOKEN", ""+token);

        Call<UserInfo> call = retrofitAPI.getData("Bearer "+token, url);



        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    Log.d("USER", "Post Success");
                    Log.d("USER", ""+response.body().getUser_name() );

                    name.setText(response.body().getUser_name());
                    status.setText(response.body().getStatus_msg());
                    uri = Uri.parse(response.body().getProfile_img());
                    Log.d("USER", uri.toString());
                    //setImg();

                    try {

                        InputStream instream = resolver.openInputStream(uri);
                        Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                        img.setImageBitmap(imgBitmap);
                        instream.close();
                        Log.d("USER", "SUCCESSED " +uri.toString());
                    } catch (Exception e) {
                        Log.d("USER", "FAILED " +uri.toString());
                    }


                }
                else{
                    Log.d("USER", "Post Failed");
                }


            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("USER", "Post Failed2");
            }
        });

        String n = name.getText().toString();






        btnHome.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(User_List.this,Login_Activity.class);
                startActivity(in);
                finish();
            }
        }));

        btnChat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(User_List.this, Chat_List.class);
                in.putExtra("uid",url);
                in.putExtra("token",token);
                startActivity(in);
            }
        }));

        findViewById(R.id.myProfile).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<UserInfo> call = retrofitAPI.getData("Bearer "+token, url);

                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.isSuccessful()){
                            Log.d("EDIT", "Post Success");


                            Intent in = new Intent(User_List.this, EditProfile_Activity.class);
                            in.putExtra("name", name.getText().toString());
                            in.putExtra("token",token);
                            in.putExtra("status", status.getText().toString());
                            in.putExtra("uid",url);
                            in.putExtra("uri",uri);
                            startActivity(in);

                        }
                        else{
                            Log.d("EDIT", "Post Failed");
                        }


                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.d("EDIT", "Post Failed2");
                    }
                });





            }
        }));






    }
}