package com.example.seelook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class profile_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);

        //Button Back_button=findViewById(R.id.back);
        //Back_button.setOnClickListener(new View.OnClickListener(){
        //    public void onClick(View view){
         //       finish();
         //   }
        //});

        Button homeBt = (Button)findViewById(R.id.home_btn);
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),home_Activity.class);
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
                Intent intent = new Intent(getApplicationContext(),profile_Activity.class);
                startActivity(intent);

            }
        });






    }
}