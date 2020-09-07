package com.example.seelook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Post_Activity extends AppCompatActivity {

    private static final String TAG="Post_Activity";

    private final int SELECT_THUMB = 1;
    private final int SELECT_MOVIE = 2;
    private final int FROM_CAMERA = 101;

    private ConstraintLayout videoView_layout;
    private ConstraintLayout thumbnailview_layout;
    private VideoView videoView;
    private ImageView thumbnailView;
    private Button add_btn; //영상 가져오기
    private Button thumbnail_btn;//썸네일 버튼

    //database위함
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

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

    private Task<Uri> imageUrl;
    private String videoUrl;
    private String thumbUrl;

    private String path;//절대 경로
    private String path_t; //썸네일 경로
    private EditText et_content;
    private String content;//내용
    private String time;//업로드 시간
    private String user_email_id;

    private Boolean check_thumb =false;//썸네일 선택했는지 체크
    private Boolean check_video = false;//영상 선택 했는지 체크


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);

        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();//자동 로그인 정보 로드
        //사용자 이메일에서 @앞만따와서 폴더 지정하기 위해...(함수따로 만들긴 귀찮)
        int index= getUserEmail.indexOf("@");
        user_email_id = getUserEmail.substring(0,index);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();//storage reference 얻어오기

        videoView_layout=findViewById(R.id.videoview_layout);
        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);//안보이게

        thumbnailview_layout=findViewById(R.id.thumbnailview_layout);
        thumbnailView = (ImageView)findViewById(R.id.thumbnail_view);
        thumbnailView.setVisibility(View.INVISIBLE);//안보이게

        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정

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

        //영상 불러오기-> 선택
        add_btn = (Button)findViewById(R.id.add_button);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog();
            }
        });

        //썸네일 이미지 선택
        thumbnail_btn=(Button)findViewById(R.id.thumbnail_btn);
        thumbnail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        //업로드 버튼(체크)
        upload_btn=(Button)findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_thumb==true && check_video==true)
                {
                    alert();//알림창이랑 업로드
                }
                else if(check_thumb==false&&check_video==true)
                {
                    Toast.makeText(Post_Activity.this,"썸네일을 선택 해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(check_video==false&&check_thumb==true)
                {
                    Toast.makeText(Post_Activity.this,"업로드할 영상을 선택 해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(check_thumb==false&&check_video==false) //만약을 대비해서 else로 안한거임 ㅅㄱ
                {
                    Toast.makeText(Post_Activity.this,"영상과 썸네일을 선택해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode!=RESULT_OK){
            return;
        }
        else if (requestCode == SELECT_MOVIE){//영상 업로드-갤러리
            Uri uri = intent.getData();
            path = getPath(uri);//경로 찾기

            //Toast.makeText(Post_Activity.this,"영상 선택 성공",Toast.LENGTH_SHORT).show();

            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
            add_btn.setVisibility(View.INVISIBLE);

            videoView.requestFocus(); // 포커스 얻어오기
            videoView.start();

            videoView_layout.setBackgroundColor(Color.WHITE);
            check_video=true;

        }
        else if (requestCode == FROM_CAMERA){//영상 업로드-카메라 촬영

        }
        else if (requestCode == SELECT_THUMB){//썸네일 업로드드
            Uri uri = intent.getData();
            path_t = getPath(uri);//경로 찾기
            //Toast.makeText(Post_Activity.this,"썸네일 업로드 완료",Toast.LENGTH_SHORT).show();

            Glide.with(this)
                    .load(path_t)
                    .into(thumbnailView);

            thumbnailview_layout.setBackgroundColor(Color.WHITE);
            thumbnailView.setVisibility(View.VISIBLE);
            thumbnail_btn.setVisibility(View.INVISIBLE);

            check_thumb = true;
        }
    }

    // 실제 경로 찾기: uri 절대 경로 가져오기
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(this,uri, projection, null, null, null);

        Cursor cursor= cursorLoader.loadInBackground();
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
                //uploadFirebase();//업로드!!!!!
                upload();
                //loadFirebase_t();//썸네일 업로드
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

    //선택 창
    private void makeDialog(){
        AlertDialog.Builder alt_bld=new AlertDialog.Builder(this);
        alt_bld.setTitle("영상 업로드");
        alt_bld.setIcon(R.drawable.add_push).setCancelable(false).setPositiveButton("영상 선택",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v("알림","다이얼로그> 앨범선택 선택");
                selectVideo();
            }
        }).setNeutralButton("영상 촬영",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("알림","다이얼로그> 사진촬영 선택");
                        takePhoto();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("알림","다이얼로그> 취소 선택");
                        dialogInterface.cancel();
                    }
                });
            AlertDialog alert=alt_bld.create();
            alert.show();
    }

    //사진 찍기 선택
    public void takePhoto() {

    }

    //영상 선택
    public void selectVideo() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivityForResult(i,SELECT_MOVIE);

        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //썸네일 고르기
    public void selectPhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivityForResult(i,SELECT_THUMB);

        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //파이어베이스 업로드
    public void upload() {
        getTime();
        et_content=(EditText)findViewById(R.id.editTextContent);
        content = et_content.getText().toString();//내용

        //방법1 - 절대경로
        final Uri video_file = Uri.fromFile(new File(path));//절대 경로 uri를 file에 할당
        final Uri thumb_file = Uri.fromFile(new File(path_t));//절대 경로 uri를 file에 할당

        uploadStorage_video();
        uploadStorage_thumb();

        PostModel postModel = new PostModel(video_file.getLastPathSegment(), thumb_file.getLastPathSegment(), content, getUserEmail);
        firebaseDatabase.getReference().child("video_info").child(user_email_id).child(time).setValue(postModel);
        //업로드할때마다 영상정보가 realtime~에 추가되어야 하므로
        //경로를 사용자이메일-업로드시간 으로 구분함.
    }

    //firebase 업로드
    private void uploadStorage_video() {
        final Uri video_file = Uri.fromFile(new File(path));//절대 경로 uri를 file에 할당

        //storage에 절대경로 파일 저장
        StorageReference mStorageRef = storageRef.child(user_email_id).child(time).child(video_file.getLastPathSegment());
        uploadTask = mStorageRef.putFile(video_file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Post_Activity.this, "영상 업로드 실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    private void uploadStorage_thumb() {
        final Uri thumb_file = Uri.fromFile(new File(path_t));//절대 경로 uri를 file에 할당

        //storage에 절대경로 파일 저장
        StorageReference mStorageRef = storageRef.child(user_email_id).child(time).child(thumb_file.getLastPathSegment());
        uploadTask = mStorageRef.putFile(thumb_file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Post_Activity.this, "영상 업로드 실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    public void getTime(){
        SimpleDateFormat type = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        time = type.format (System.currentTimeMillis());
    }

    private void load(){
        //SharedPreference 객체.get타입(저장된 이름, 기본 값)
        //저장된 이름이 존재하지 않을 시 기본 값
        getUserEmail=appData.getString("email","");
        getUserPassword=appData.getString("pw","");
    }
}


