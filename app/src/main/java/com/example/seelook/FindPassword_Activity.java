package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindPassword_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_);

        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(intent);
            }
        });

        final Button id= (Button)findViewById(R.id.id);
        final Button password= (Button)findViewById(R.id.password);

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id.setSelected(true);
                password.setSelected(false);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setSelected(true);
                id.setSelected(false);
            }
        });
    }
}