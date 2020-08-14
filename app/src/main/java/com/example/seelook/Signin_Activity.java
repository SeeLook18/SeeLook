package com.example.seelook;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Signin_Activity extends AppCompatActivity {

    private static final String TAG="Signin_Activity";
    EditText et_user_name, et_user_email, et_user_password,et_user_password_check;
    Button btn_register;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_);

        et_user_name = findViewById(R.id.name);
        et_user_email = findViewById(R.id.email);
        et_user_password = findViewById(R.id.password);
        et_user_password_check = findViewById(R.id.password_check);
        btn_register = findViewById(R.id.register);

        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = et_user_name.getText().toString();
                String getUserEmail = et_user_email.getText().toString();
                String getUserPassword = et_user_password.getText().toString();
                String getUserPassword_check=et_user_password_check.getText().toString();

                if(getUserPassword.equals(getUserPassword_check)){
                    Log.d(TAG,"비밀 번호 동일"+getUserName+"/"+getUserEmail+"/"+getUserPassword);
                    final ProgressDialog Dialog=new ProgressDialog(Signin_Activity.this);
                    //hashmap 만들기
                    HashMap result = new HashMap<>();

                    result.put("name", getUserName);
                    result.put("email", getUserEmail);
                    result.put("password", getUserPassword);

                    writeNewUser("1",getUserName,getUserEmail,getUserPassword);
                }else{
                    Toast.makeText(Signin_Activity.this,"비밀 번호가 틀렸습니다. 다시 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        final Button male= (Button)findViewById(R.id.male);
        final Button female= (Button)findViewById(R.id.female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setSelected(true);
                female.setSelected(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setSelected(true);
                male.setSelected(false);
            }
        });
    }

    private void writeNewUser(String userId, String name, String email, String password) {
        User user = new User(name, email, password);

        mDatabase.child("users").push().child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(Signin_Activity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(Signin_Activity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(Signin_Activity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}