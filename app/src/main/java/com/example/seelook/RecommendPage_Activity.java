package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecommendPage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_page_);

        Button homeBt = (Button)findViewById(R.id.home_btn);
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Home_Activity1.class);
                startActivity(intent);
            }
        });

        Button uploadBt = (Button)findViewById(R.id.upload_btn);
        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Post_Activity.class);
                startActivity(intent);
            }
        });

        Button profileBt = (Button)findViewById(R.id.profile_btn);
        profileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Profile1_Activity.class);
                startActivity(intent);
            }
        });
    }
}