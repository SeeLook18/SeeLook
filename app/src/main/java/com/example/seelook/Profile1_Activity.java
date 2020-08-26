package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile1_Activity extends AppCompatActivity {

    private String getUserEmail;
    private String getUserPassword;
    private SharedPreferences appData;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReference();
    StorageReference pathReference=storageRef.child("getUserEmail");//storage내부의 폴더안의 파일명을 가리키는 참조 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1_);

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();//자동 로그인 정보 로드

        Button homeBt = (Button)findViewById(R.id.home_btn);//홈버튼
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Home_Activity1.class);
                startActivity(intent);
            }
        });

        Button uploadBt = (Button)findViewById(R.id.upload_btn);//업로드 버튼
        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Post_Activity.class);
                startActivity(intent);
            }
        });

        Button profileBt = (Button)findViewById(R.id.profile_btn);//프로필 버튼
        profileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Profile1_Activity.class);
                startActivity(intent);

            }
        });
    }
    public void getStorage(){
        //개수만큼 영상 보이게
        //Glide??
    }
    private void load(){
        //SharedPreference 객체.get타입(저장된 이름, 기본 값)
        //저장된 이름이 존재하지 않을 시 기본 값
        getUserEmail=appData.getString("email","");
        getUserPassword=appData.getString("pw","");
    }
}