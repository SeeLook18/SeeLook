package com.example.seelook;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;

public class Signin_Activity extends AppCompatActivity {

    private static final String TAG="Signin_Activity";

    private CountDownTimer countDownTimer;

    private FirebaseAuth firebaseAuth;
    //realtime database 위함
    private FirebaseDatabase firebaseDatabase;
    //private FirebaseStorage firebaseStorage;

    private EditText et_user_name, et_user_email, et_user_password,et_user_password_check,et_user_email_code;
    private TextView password_error, email_confirm_alarm, time_counter;
    private Button btn_register,btn_email,btn_email_confirm,auth_Button;
    private String randomcode; //인증번호
    private Boolean email_confirm; //이메일인증여부

    final int MILLISINFUTURE=180*1000;
    final int COUNT_DOWN_INTERVAL=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .permitDiskReads()
        .permitDiskWrites()
        .permitNetwork().build());

        //firebase 정의
        firebaseAuth=FirebaseAuth.getInstance();
        //database위함
        firebaseDatabase=FirebaseDatabase.getInstance();
        //firebaseStorage=FirebaseStorage.getInstance();

        //레이아웃이랑 연결
        et_user_name =(EditText) findViewById(R.id.name);
        et_user_email = (EditText) findViewById(R.id.user_email);
        et_user_password = (EditText) findViewById(R.id.password);
        et_user_password_check = (EditText) findViewById(R.id.password_check);
        et_user_email_code=(EditText)findViewById(R.id.email_code);

        btn_register = (Button)findViewById(R.id.register);
        btn_email=(Button)findViewById(R.id.email_check);//이메일 인증 버튼
        btn_email_confirm=(Button)findViewById(R.id.email_confirm); //이메일 인증코드 확인 버튼

        password_error=(TextView)findViewById(R.id.textView3);//비밀번호 조건 경고 멘트
        password_error.setVisibility(View.INVISIBLE);

        email_confirm_alarm=(TextView)findViewById(R.id.textView11);//이메일 인증 성공여부 멘트
        email_confirm_alarm.setVisibility(View.INVISIBLE);

        email_confirm=Boolean.FALSE;//이메일 인증 여부

        //이메일 인증 버튼
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메일 보낼 곳 설정
                SendMail mailServer=new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(),et_user_email.getText().toString());
                randomcode= mailServer.getRandomcode();//인증 번호
                countDownTimer();//타이머 시작
            }
        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //이거 여기 넣는거맞남?? (합치면 위치 바뀌나 해서...)
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        btn_email_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserEmail=et_user_email_code.getText().toString();

                if(getUserEmail.equals(randomcode)){ //이메일 인증번호 일치하는 경우
                    Toast.makeText(Signin_Activity.this,"인증번호 일치",Toast.LENGTH_SHORT).show();
                    email_confirm=Boolean.TRUE; //이메일 인증 성공여부
                    email_confirm_alarm.setText("이메일 인증 성공");
                    email_confirm_alarm.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(Signin_Activity.this,"인증번호 불일치",Toast.LENGTH_SHORT).show();
                    email_confirm_alarm.setText("이메일 인증 실패");
                    email_confirm_alarm.setVisibility(View.VISIBLE);
                }
            }
        });

        //회원가입 버튼
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문자열로
                final String getUserName = et_user_name.getText().toString();
                final String getUserEmail = et_user_email.getText().toString();
                final String getUserPassword = et_user_password.getText().toString();
                final String getUserPassword_check=et_user_password_check.getText().toString();

                if(email_confirm == Boolean.TRUE){//이메일 인증 한 경우
                    //이메일, 이름, 비밀번호가 공백이 아닌 경우
                    if(!getUserEmail.equals("")&&!getUserPassword.equals("")&&!getUserName.equals("")){
                        //동일한 비밀번호 체크
                        if(getUserPassword.equals(getUserPassword_check)){
                            Log.d(TAG,"비밀 번호 동일/"+getUserName+"/"+getUserEmail+"/"+getUserPassword);
                            createUser(getUserEmail,getUserPassword,getUserName);
                        }
                        else{
                            Toast.makeText(Signin_Activity.this,"비밀번호가 일치하지않습니다. 다시 입력해 주세요",Toast.LENGTH_SHORT).show();
                            //return;
                        }
                    }
                    //공백인 경우
                    else{
                        Toast.makeText(Signin_Activity.this,"계정과 비밀번호를 입력하세요",Toast.LENGTH_LONG).show();
                    }
                }
                else{//이메일 인증 하지 않은 경우
                    Toast.makeText(Signin_Activity.this,"이메일 인증이 필요합니다.",Toast.LENGTH_LONG).show();
                }

            }
        });

        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
    //회원가입
    private void createUser(final String email, final String password, final String name){
        if(!isValidEmail(email)){//email 검사 isValidEmail=false 일 경우
            Log.e(TAG,"createAccount: email이 유효하지 않음");
            Toast.makeText(Signin_Activity.this,"Email이 유효하지 않습니다",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPassword(password)){//password 검사
            Log.e(TAG,"createAccount: password가 유효하지 않음");
            Toast.makeText(Signin_Activity.this,"password가 유효하지 않습니다",Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"createUserWithEmail:success");
                    Toast.makeText(Signin_Activity.this,"회원가입 성공",Toast.LENGTH_SHORT).show();
                    finish();

                    final String uid=task.getResult().getUser().getUid();//현재 로그인한 사용
                    UserModel userModel=new UserModel();
                    userModel.userName=name;
                    userModel.email=email;
                    userModel.password=password;
                    userModel.uid=uid;

                    firebaseDatabase.getReference().child("users").child(uid).setValue(userModel);
                }
                else{//계정이 중복된 경우
                    Log.w(TAG,"createUserWithEmail:fail",task.getException());
                    Toast.makeText(Signin_Activity.this,"이미 존재하는 계정입니다.",Toast.LENGTH_SHORT).show();
                    //UI
                }
            }
        });
    }

    private boolean isValidPassword(String target){//비밀 번호 검사: 6자리 이상 한글 미포함
        String pwPattern ="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{5,}$";
        Boolean tt=Pattern.matches(pwPattern,target);

        if (tt==true){
            Log.d(TAG,"비밀 번호 조건 만족");
            return true;
        }
        else{
            password_error.setVisibility(View.VISIBLE); //왜 바탕색이 회색인게냐!!!!!!
            return false;
        }
    }
    private boolean isValidEmail(String target) {//email 검사
        if (target == null || TextUtils.isEmpty(target)){
            return false;
        }else{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void countDownTimer(){
        time_counter = (TextView) findViewById(R.id.time_counter);
        auth_Button=(Button)findViewById(R.id.email_confirm);//확인 버튼

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