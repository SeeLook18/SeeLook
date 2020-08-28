package com.example.seelook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Post_Activity extends AppCompatActivity {

    private final int SELECT_MOVIE = 2;
    private FirebaseAuth mAuth;
    private VideoView videoView;
    private Button add_btn; //영상 가져오기

    private StorageReference storageRef;
    private UploadTask uploadTask;

    //하단 바 메뉴
    private Button home_btn;
    private Button profile_btn;
    private Button upload_btn;

    private String saveLoginData;
    private String getUserEmail;
    private String getUserPassword;
    private SharedPreferences appData;

    private String path;//절대 경로
    private EditText et_title;
    private EditText et_content;
    private String title;//제목
    private String content;//내용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();//자동 로그인 정보 로드
        mAuth=FirebaseAuth.getInstance();

        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);//안보이게?

        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정

        storageRef = FirebaseStorage.getInstance().getReference();

        home_btn = (Button)findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Home_Activity1.class);
                startActivity(intent);
            }
        });

        profile_btn = (Button)findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Profile1_Activity.class);
                startActivity(intent);
            }
        });

        //영상 불러오기
        add_btn = (Button)findViewById(R.id.add_button);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivityForResult(i,SELECT_MOVIE);

                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });

        //업로드 버튼(체크)
        upload_btn=(Button)findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_title=(EditText)findViewById(R.id.editTextTitle);
                et_content=(EditText)findViewById(R.id.editTextContent);
                title = et_title.getText().toString();//제목
                content = et_content.getText().toString();//내용

                final String uid=mAuth.getCurrentUser().getUid();
                alert();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_MOVIE) {
                Uri uri = intent.getData();
                path = getPath(uri);//경로 찾기



                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(uri);
                add_btn.setVisibility(View.INVISIBLE);

                videoView.requestFocus(); // 포커스 얻어오기
                videoView.start();
            }
        }
    }

    // 실제 경로 찾기: uri 절대 경로 가져오기
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //게시물 작성 알림창
    private void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시물 작성").setMessage("게시물을 작성하시겠습니까?");
        builder.setIcon(R.drawable.done);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uploadFirebase();
                Toast.makeText(getApplicationContext(), "게시물 작성 완료", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(
                        getApplicationContext(),
                        Home_Activity1.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", null).show();
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    private void load(){
        //SharedPreference 객체.get타입(저장된 이름, 기본 값)
        //저장된 이름이 존재하지 않을 시 기본 값
        getUserEmail=appData.getString("email","");
        getUserPassword=appData.getString("pw","");
    }

    private void uploadFirebase() {
        //firebase 업로드
        Uri file = Uri.fromFile(new File(path));//절대 경로 uri를 file에 할당
        //storage에 절대경로 파일 저장
        StorageReference mStorageRef = storageRef.child(getUserEmail + "/" + file.getLastPathSegment());
        uploadTask = mStorageRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Post_Activity.this, "영상 업로드 실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                PostModel postModel=new PostModel();
                postModel.myid=getUserEmail;
                Toast.makeText(Post_Activity.this, "영상 업로드 성공", Toast.LENGTH_LONG).show();
            }
        });
    }
}


