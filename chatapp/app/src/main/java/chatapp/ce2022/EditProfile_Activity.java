package chatapp.ce2022;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile_Activity extends AppCompatActivity {

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
                    img.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    img.setTooltipText(uri.toString());
                    instream.close();   // 스트림 닫아주기
                    //saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);

        Button btnEdit = (Button) findViewById(R.id.profile_button);

        EditText name = (EditText) findViewById(R.id.profile_name);
        EditText status = (EditText) findViewById(R.id.profile_status);

        Intent intent = getIntent();
        String n = intent.getStringExtra("name");
        String s = intent.getStringExtra("status");
        String i;
        name.setText(n);
        status.setText(s);


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






        btnEdit.setOnClickListener((new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String status1 = status.getText().toString();
                String image1 = findViewById(R.id.profile_image).getTooltipText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://3.39.35.15:3000")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                Intent intent = getIntent();
                String token = intent.getStringExtra("token");
                String url = intent.getStringExtra("uid");
                UserInfo info = new UserInfo();

                Log.d("TOKEN-P", ""+token);

                Call<UserInfo> call = retrofitAPI.changeData("Bearer "+token, url, name1, status1, image1);

                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.isSuccessful()){
                            Log.d("Profile", "Post Success");
                            Log.d("Profile", ""+response.body().getUser_name() );
                            Log.d("Profile",image1);


                        }
                        else{
                            Log.d("Profile", "Post Failed");
                        }


                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.d("Profile", "Post Failed2");
                    }
                });


                Intent in = new Intent(EditProfile_Activity.this,User_List.class);
                in.putExtra("token",token);
                in.putExtra("uid",url);
                startActivity(in);
            }
        }));

    }
}