package com.sealiu.piece.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public class Piece extends BmobObject {
    //作者(外键，用户的objectId)
    private String authorID;
    //内容
    private String content;
    //图片
    private String image;
    //链接
    private String url;
    //音频
    private String audio;
    //视频
    private String video;
    //piece类型
    //小纸条（1），交友（2），活动（3），故事（4），新闻（5），天气（6），广告（7）
    private Integer type;
    //经度
    private Double longitude;
    //维度
    private Double latitude;
    //浏览次数
    private Integer viewCount = 0;
    //可见性
    //5km, 20km, 60km, 100km (分别为：0，1，2，3)
    private Integer visibility;

    public Piece() {
        super();
    }

    public Piece(String authorID, String content, Double lat, Double lng, Integer visib) {
        this.authorID = authorID;
        this.content = content;
        this.latitude = lat;
        this.longitude = lng;
        this.visibility = visib;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
}
