package chatapp.ce2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class Login_Activity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Activity mActivity;




    EditText enterId, enterPw;
    ProgressBar progressBar;

    String strId;
    String strPw;

    private Socket mSocket;

    public static Context contextLogin;

    String token;
    String tok;

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit("clientMessage", "hi");
        }
    };

    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try{
                JSONObject receivedData = (JSONObject) args[0];
                Log.d(TAG, receivedData.getString("msg"));
                Log.d(TAG, receivedData.getString("data"));
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    };


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://3.39.35.15:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        contextLogin = this;









        enterId = (EditText) findViewById(R.id.enterId);
        enterPw = (EditText) findViewById(R.id.enterPw);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);



        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strId = enterId.getText().toString();
                strPw = enterPw.getText().toString();


                if(strId.isEmpty()){
                    Toast.makeText(Login_Activity.this,"이메일을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }
                if(strPw.isEmpty()){
                    Toast.makeText(Login_Activity.this,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }



                login();






                Intent in = new Intent(Login_Activity.this, User_List.class);
                in.putExtra("token", tok);


                enterPw.setText("");



            }
        });





        Button btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(in);
            }
        });
    }



    private void login() {

        String strId = enterId.getText().toString();
        String strPw = enterPw.getText().toString();


        Login login = new Login(strId, strPw);
        /*HashMap<String,Object> login = new HashMap<>();
        String id = "abc";
        String pw = "abc";
        login.put("id",id);
        login.put("password",pw);*/


        Call<Login> call = retrofitAPI.login(login);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()){
                    token = response.body().getToken();
                    Log.d("TEST", "Post Success");



                    Intent in = new Intent(Login_Activity.this, User_List.class);
                    in.putExtra("token", token);
                    in.putExtra("uid",strId);
                    Log.d("TEST22",token);

                    startActivity(in);

                }
                else{
                    Toast.makeText(Login_Activity.this, "login not correct", Toast.LENGTH_SHORT).show();
                    Log.d("TEST", "Post Failed");
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(Login_Activity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "Post Failed2");
            }
        });






    }






}