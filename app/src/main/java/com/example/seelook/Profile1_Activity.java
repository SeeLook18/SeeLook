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
import android.widget.MediaController;
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

public class Profile1_Activity extends AppCompatActivity {

    private static final String TAG="Profile_Activity";

    private String getUserEmail;
    private String getUserPassword;
    private SharedPreferences appData;
    private DatabaseReference mDatabase;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReference();
    //참조가 이렇게 많다고???
    StorageReference pathReference;
    StorageReference gsReference;
    StorageReference httpsReference;

    private VideoView videoView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1_);

        videoView1 = (VideoView)findViewById(R.id.videoView1);
        videoView1.setVisibility(View.INVISIBLE);//안보이게?
        MediaController mc = new MediaController(this);
        videoView1.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();//자동 로그인 정보 로드
        getStorage();

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

    //Post에서 user property로 영상 이름을 넣어놓는 배열을 만들어서 가져오면??
    public void getStorage(){
        //이렇게 많은 참조가!!!!! 원하는 거 쓰는거임
        pathReference=storageRef.child(getUserEmail+"/[예.비.용#10] 예리,나은,도연😍 예쁜 애들은 쉬는 시간에 뭐 하고 놀아.mp4");
        gsReference=storage.getReferenceFromUrl("gs://seelook-d3a93.appspot.com/sung980524@naver.com/[예.비.용#10] 예리,나은,도연\uD83D\uDE0D 예쁜 애들은 쉬는 시간에 뭐 하고 놀아.mp4");
        httpsReference=storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/seelook-d3a93.appspot.com/o/sung980524%40naver.com%2F%5B%EC%98%88.%EB%B9%84.%EC%9A%A9%2310%5D%20%EC%98%88%EB%A6%AC%2C%EB%82%98%EC%9D%80%2C%EB%8F%84%EC%97%B0%F0%9F%98%8D%20%EC%98%88%EC%81%9C%20%EC%95%A0%EB%93%A4%EC%9D%80%20%EC%89%AC%EB%8A%94%20%EC%8B%9C%EA%B0%84%EC%97%90%20%EB%AD%90%20%ED%95%98%EA%B3%A0%20%EB%86%80%EC%95%84.mp4?alt=media&token=7257dbd5-9506-4a2f-aef4-cbfa643058c6");

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG,"성공인가염");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"실패인가염");
            }
        });
    }
    private void load(){
        //SharedPreference 객체.get타입(저장된 이름, 기본 값)
        //저장된 이름이 존재하지 않을 시 기본 값
        getUserEmail=appData.getString("email","");
        getUserPassword=appData.getString("pw","");
    }
}