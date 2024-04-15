package chatapp.ce2022;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Chat_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String url = intent.getStringExtra("uid");



        Button btnHome = (Button) findViewById(R.id.btn_home);
        Button btnUser = (Button) findViewById(R.id.btn_chat);

        btnHome.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Chat_List.this,User_List.class);
                in.putExtra("uid",url);
                in.putExtra("token",token);
                startActivity(in);
            }
        }));


        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Chat_List.this, Login_Activity.class);
                startActivity(in);
                finish();
            }
        });


        findViewById(R.id.room1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Chat_List.this, Chat_Activity.class);
                startActivity(in);
            }
        });




    }





}