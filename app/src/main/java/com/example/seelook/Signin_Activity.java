package com.example.seelook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Signin_Activity extends AppCompatActivity {

    private static final String TAG="Signin_Activity";

    private FirebaseAuth firebaseAuth;
    private EditText et_user_name, et_user_email, et_user_password,et_user_password_check;
    private Button btn_register,btn_email;

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

        //레이아웃이랑 연결
        et_user_name =(EditText) findViewById(R.id.name);
        et_user_email = (EditText) findViewById(R.id.email);
        et_user_password = (EditText) findViewById(R.id.password);
        et_user_password_check = (EditText) findViewById(R.id.password_check);
        btn_register = (Button)findViewById(R.id.register);
        btn_email=(Button)findViewById(R.id.email_button);//이메일 인증 버튼

        //이메일 인증 버튼
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMail mailServer=new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(),et_user_email.getText().toString());
                Intent intent = new Intent(
                        getApplicationContext(),
                        Auth_Email_Activity.class);
                startActivity(intent);
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

                //이메일, 이름, 비밀번호가 공백이 아닌 경우
                if(!getUserEmail.equals("")&&!getUserPassword.equals("")&&!getUserName.equals("")){
                    //동일한 비밀번호 체크
                    if(getUserPassword.equals(getUserPassword_check)){
                        Log.d(TAG,"비밀 번호 동일/"+getUserName+"/"+getUserEmail+"/"+getUserPassword);
                        createUser(getUserEmail,getUserPassword,getUserName);
                    }
                    else{
                        Toast.makeText(Signin_Activity.this,"비밀번호 일치하지않습니다. 다시 입력해 주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //공백인 경우
                else{
                    Toast.makeText(Signin_Activity.this,"계정과 비밀번호를 입력하세요",Toast.LENGTH_LONG).show();
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
    private void createUser(String email,String password,String name){
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
                    FirebaseUser user = firebaseAuth.getCurrentUser();//현재 로그인한 사용자 가져오기
                    Toast.makeText(Signin_Activity.this,"회원가입 성공",Toast.LENGTH_SHORT).show();
                    finish();
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
            Toast.makeText(Signin_Activity.this,"비밀 번호는 대소문자 한개 포함, 숫자 한개 포함, 특수 문자 한개 포함해야 합니다",Toast.LENGTH_SHORT).show();
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
}