package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Auth_Email_Activity extends AppCompatActivity{

    private TextView time_counter;
    private EditText email_check;//인증 번호 확인 텍스트
    private Button auth_Button;//인증 번호 확인 버튼
    private CountDownTimer countDownTimer;

    final int MILLISINFUTURE=180*1000;
    final int COUNT_DOWN_INTERVAL=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__email_);

        Intent intent=new Intent(Auth_Email_Activity.this,Signin_Activity.class);
        String checkcode=intent.getStringExtra("randomcode");

        Toast.makeText(Auth_Email_Activity.this,"전달 받은 인증 번호: "+checkcode,Toast.LENGTH_SHORT).show();

        email_check=(EditText)findViewById(R.id.email_check);//사용자 인증 번호 확인
        auth_Button = (Button)findViewById(R.id.check);//인증 번호 확인 버튼

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //인증 번호 확인 버튼
        auth_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserEmail=email_check.getText().toString();

            }
        });

        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        countDownTimer();
    }

    public void countDownTimer(){
        time_counter = (TextView) findViewById(R.id.time_counter);
        auth_Button=(Button)findViewById(R.id.check);//확인 버튼

        countDownTimer=new CountDownTimer(MILLISINFUTURE,COUNT_DOWN_INTERVAL){
            public void onTick(long millisUntilFinished){
                long emailAuthCount=millisUntilFinished/1000;

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
            }
            public void onFinish(){
                time_counter.setText("00:00");
            }
        }.start();
    }
}
