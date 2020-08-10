package com.example.seelook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Post_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);
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
}