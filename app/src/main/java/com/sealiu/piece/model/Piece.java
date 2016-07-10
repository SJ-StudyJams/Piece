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
    private String imageUrl;
    //音频
    private String audioUrl;
    //视频
    private String videoUrl;
    //piece类型
    //小纸条（1），交友（2），活动（3），故事（4），新闻（5），天气（6），广告（7）
    private Integer type;
    //经度
    private Double longitude;
    //维度
    private Double latitude;
    //浏览次数
    private Double viewCount;
    //点赞功能
    //private Double clickLike;
    //可见性
    // -1 仅自己可见， 0 附近“30米”可见， 1 附近xxx米可见， 2 附件xxx米可见
    private Integer visibility;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public Double getViewCount() {
        return viewCount;
    }

    public void setViewCount(Double viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
}
