package com.example.seelook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class Profile1_Activity extends AppCompatActivity implements Button.OnClickListener{

    private static final String TAG="Profile_Activity";
    private TextView nickname;
    private String getUserEmail;
    private String user_email_id;//realtimebase, Storage 에서 쓰기 위함
    private String user_nick_name;
    private SharedPreferences appData;
    final private DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1_);

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();//자동 로그인 정보 로드. getUserEmail 사용 위함

        int index= getUserEmail.indexOf("@");
        user_email_id = getUserEmail.substring(0,index);
        nickname = findViewById(R.id.nickname);

        getUser(user_email_id);
        getStorage(user_email_id);

        //버튼
        Button homeBt = (Button)findViewById(R.id.home_btn);
        homeBt.setOnClickListener(this);
        Button uploadBt = (Button)findViewById(R.id.upload_btn);
        uploadBt.setOnClickListener(this);
        Button profileBt = (Button)findViewById(R.id.profile_btn);
        profileBt.setOnClickListener(this);
    }

    //데이터베이스에서 users 가져와서 닉네임 설정
    public void getUser(String user_email){
        userRef.child(user_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user= dataSnapshot.getValue(UserModel.class);
                user_nick_name=user.userName;
                nickname.setText(user_nick_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nickname.setText("ERROR");
            }
        });
    }

    public void getStorage(String user_email){
        //스토리지는 어떻게 가져올까.... 일단 데이터베이스에서 썸네일, 영상 경로 얻어오고 -> 스토리지에서 가져오기??
        // 데이터베이스에 시간순으로 여러개 있는데 이건 어떻게 하지?
        DatabaseReference videoRef=FirebaseDatabase.getInstance().getReference().child("video_info");
        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //PostModel post=dataSnapshot.child(user_email).getValue(PostModel.class); 시간을 어떻게 처리할까
                //post 배열?
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //버튼 처리
   public void onClick(View view){
        switch (view.getId()){
            case R.id.home_btn:
                Intent intent1 = new Intent(getApplicationContext(),Home_Activity1.class);
                startActivity(intent1);
                break;
            case R.id.upload_btn:
                Intent intent2 = new Intent(getApplicationContext(),Post_Activity.class);
                startActivity(intent2);
                break;
            case R.id.profile_btn:
                Intent intent3 = new Intent(getApplicationContext(),Profile1_Activity.class);
                startActivity(intent3);
                break;
        }
   }
    public void load(){
       //SharedPreference 객체.get타입(저장된 이름, 기본 값)
       //저장된 이름이 존재하지 않을 시 기본 값
       getUserEmail=appData.getString("email","");
    }
}