package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.annotations.NotNull;


public class Home_Activity1 extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_1);

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
                        Profile1_Activity.class);
                startActivity(intent);
            }
        });

        final TextView hot_page = (TextView)findViewById(R.id.hot_page);
        hot_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        HotPage_Activity.class);
                startActivity(intent);
            }
        });

        final TextView new_page = (TextView)findViewById(R.id.new_page);
        new_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        NewPage_Activity.class);
                startActivity(intent);
            }
        });

        final TextView recommend_page = (TextView)findViewById(R.id.recommend_page);
        recommend_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        RecommendPage_Activity.class);
                startActivity(intent);
            }
        });
    }
}