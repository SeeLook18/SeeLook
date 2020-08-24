package com.example.seelook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Auth_Email_Activity extends AppCompatActivity{

    TextView time_counter;
    Button auth_Button;//인증 번호 확인 버튼
    CountDownTimer countDownTimer;

    final int MILLISINFUTURE=180*1000;
    final int COUNT_DOWN_INTERVAL=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__email_);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

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
