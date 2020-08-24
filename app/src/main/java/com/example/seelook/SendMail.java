package com.example.seelook;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity{
    String user= "sung980524";//보내는 계정의 id
    String password="ekdms98**";//보내는 계정의 pw

    public void sendSecurityCode(Context context, String sendTo){
        try{
            MailSender mailSender=new MailSender(user,password);
            mailSender.sendMail("제목","내용",sendTo);
            Toast.makeText(context,"인증 메일을 보냈습니다.",Toast.LENGTH_SHORT).show();
        }catch (SendFailedException e){
            Toast.makeText(context,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show();
        }catch (MessagingException e){
            Toast.makeText(context,"인터넷 연결을 확인해주십시오.",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}