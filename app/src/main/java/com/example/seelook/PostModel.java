package com.example.seelook;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PostModel implements Serializable {
    //public String myuid; // 로그인한 uid
    //public String video; // 게시 영상
    //public String videoName; // 게시글사진 이름(사진삭제할때 필요, 절대경로를 뜻함)
    public String video_path;
    public String contents; // 게시 내용
    public String email;
    public String thumbnail_path;//썸네일

    //public int starCount = 0; // 좋아요 갯수
    //public Map<String, Boolean> stars = new HashMap<>(); // 좋아요 한 사람
    // String 값은 아이디를 뜻하고, boolean 은 true
    public PostModel(){

    }
    public PostModel(String video_path, String thumbnail_path, String contents, String email){
        this.video_path = video_path;
        this.thumbnail_path = thumbnail_path;
        this.contents = contents;
        this.email = email;
        //this.videoUrl=videoUrl;
        //this.thumbUrl=thumbUrl;
    }
    public String getVideo_path(){
        return video_path;
    }
    public String getThumbnail_path(){
        return thumbnail_path;
    }
    public String getEmail(){
        return email;
    }
    public String getContents(){
        return contents;
    }
}
