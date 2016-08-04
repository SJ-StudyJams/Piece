package com.sealiu.piece.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
@IgnoreExtraProperties
public class Piece {
    //username
    public String author;
    //user_id
    public String uid;
    //内容
    public String content;
    //图片
    public String image;
    //链接
    public String url;
    //音频
    public String audio;
    //视频
    public String video;
    //piece类型
    public Integer type;
    //经度
    public Double longitude;
    //维度
    public Double latitude;
    //浏览次数
    public int viewCount = 0;
    //时间
    public String date;
    //可见性
    //5km, 20km, 60km, 100km (分别为：0，1，2，3)
    public int visibility;

    public Piece() {
    }

    public Piece(String au, String ui, String co, Double la, Double ln, int vi, int ty) {
        this.author = au;
        this.uid = ui;
        this.content = co;
        this.latitude = la;
        this.longitude = ln;
        this.visibility = vi;
        this.type = ty;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.date = df.format(new Date());
    }

    public Piece(String au, String ui, String co, Double la, Double ln, int vi, int ty, String ur, String im, String aud, String vid) {
        this.author = au;
        this.uid = ui;
        this.content = co;
        this.latitude = la;
        this.longitude = ln;
        this.visibility = vi;
        this.type = ty;
        this.url = ur;
        this.image = im;
        this.audio = aud;
        this.video = vid;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.date = df.format(new Date());
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("uid", uid);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("visibility", visibility);
        result.put("type", type);
        result.put("content", content);
        result.put("url", url);
        result.put("image", image);
        result.put("audio", audio);
        result.put("video", video);
        result.put("viewCount", viewCount);
        result.put("date", date);
        return result;
    }
}
