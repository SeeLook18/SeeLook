package com.example.seelook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;

public class Post_Activity extends AppCompatActivity {

    private final int SELECT_IMAGE = 1;
    private final int SELECT_MOVIE = 2;
    private StorageReference mStorageRef;
    private VideoView videoView;
    private Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);

        Button home_btn = (Button)findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Home_Activity1.class);
                startActivity(intent);
            }
        });

        Button face_btn = (Button)findViewById(R.id.profile_btn);
        face_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Profile1_Activity.class);
                startActivity(intent);
            }
        });

        add_btn = (Button)findViewById(R.id.add_button);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivityForResult(i, SELECT_MOVIE);

                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);

                Log.e("###",
                        "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            } else if (requestCode == SELECT_MOVIE) {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);

                Log.e("###",
                        "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(path);
                add_btn.setVisibility(View.INVISIBLE);

            }
        }
    }

    // 실제 경로 찾기

    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }



    // 파일명 찾기

    private String getName(Uri uri) {

        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }



    // uri 아이디 찾기

    private String getUriId(Uri uri) {

        String[] projection = { MediaStore.Images.ImageColumns._ID };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }


    public void done_clicked(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시물 작성").setMessage("게시물을 작성하시겠습니까?");
        builder.setIcon(R.drawable.done);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "확인버튼이 눌렸습니다", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("취소", null).show();
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void upload_clicked(View v){
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };
    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivity(intent);
    }
}


