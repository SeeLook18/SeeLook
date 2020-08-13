package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);

        Button btn1 = (Button)findViewById(R.id.home_btn); //새로고침 기능 넣어주기
        /*btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Post_Activity.class);
                startActivity(intent);
            }
        });*/


        Button btn2 = (Button)findViewById(R.id.upload_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Post_Activity.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button)findViewById(R.id.profile_btn);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        profile_Activity.class);
                startActivity(intent);
            }
        });

        final TextView worldwide = (TextView)findViewById(R.id.worldwide);
        worldwide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                       WorldwideActivity.class);
                startActivity(intent);
            }
        });

    }
}