package chatapp.ce2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;


public class Chat_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    MyAdapter_2 mAdapter;


    private RecyclerView.LayoutManager layoutManager;
    EditText enterTxt;
    Button btnSend;
    Button btnLogout;
    ArrayList<MsgData2> chatArrayList;
    private Socket mSocket;
    Gson gson = new Gson();
    Intent in = getIntent();
    Boolean isRight = true;
    public static Context contextChat;


    public String uid = ((Login_Activity)Login_Activity.contextLogin).strId;
    public String rid = "123";
    public String cuid = "asd";
    public String rsa = "456";
    public String msg;
    public String name ="김짤톡";


    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("https://pred.ga:8083/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RetrofitAPI retrofitAPI2 = retrofit2.create(RetrofitAPI.class);





    private void init() {
        try {
            mSocket = IO.socket("http://3.39.35.15:3000");
            mSocket.connect();

            Log.d("Login", uid);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("Socket1", (String) mSocket.id());
                    Log.d("GET UID", uid);
                }
            });

            mSocket.on(Socket.EVENT_CONNECT, args-> {
               mSocket.emit("enter", gson.toJson(new RoomData(uid, rid, cuid, rsa)));
            });




        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("Socket", "Connection failed");
        }

        mSocket.emit("connection", "connected?");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        contextChat = this;


        chatArrayList = new ArrayList<MsgData2>();
        btnSend = (Button)findViewById(R.id.btnSend);
        //btnLogout = (Button)findViewById(R.id.btnLogout);
        enterTxt = (EditText)findViewById((R.id.enterTxt));

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter_2(chatArrayList);
        //mAdapter = new MyAdapter_2(getApplicationContext());


        init();

        /*mSocket.on("msg", args ->{
           MsgData msgData = gson.fromJson(args[0].toString(), MsgData.class);

           chatArrayList.add(msgData);
           mAdapter.notifyDataSetChanged();
        });*/


        /*btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("leave", gson.toJson(new RoomData(uid, rid)));
                finish();
                //mSocket.disconnect();
            }
        });*/



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat();
                chat.text = enterTxt.getText().toString();
                msg = enterTxt.getText().toString();
                //chatArrayList.add(chat);
                enterTxt.setText("");
                //mAdapter.notifyDataSetChanged();

                mSocket.emit("msg", gson.toJson(new MsgData(uid,rid,msg,name)));
                Log.d("Message", "Msg Sent");




            }
        });

        mSocket.on("enter", args -> {

            enterData enterData = gson.fromJson(args[0].toString(), enterData.class);
            Log.d("ENTER", enterData.getContent());
        });

        recyclerView.setAdapter(mAdapter);

        mSocket.on("update", args ->{
            Log.d("Message", "Got Message");
            MsgData2 msgData2 = gson.fromJson(args[0].toString(), MsgData2.class);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    Log.d("UID", msgData2.getSender());

                    if(msgData2.getSender().equals(uid)){
                        msgData2.setPosition(0);
                        chatArrayList.add(msgData2);
                        Log.d("testing", "same");
                    }
                    else{
                        msgData2.setPosition(1);
                        chatArrayList.add(msgData2);
                        Log.d("testing", "different");
                    }
                    Log.d("UID", String.valueOf(isRight));
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                        Log.d("Message", msgData2.getContent());
                    }
                    else Log.d("Message", "Adapter NULL");
                }
            });



        });






    }


}