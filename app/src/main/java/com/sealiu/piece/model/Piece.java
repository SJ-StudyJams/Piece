package com.sealiu.piece.model;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by liuyang
 * on 2016/7/1.
 */
public class Piece extends BmobObject {
    private Integer id;
    //作者(外键)
    private Integer authorID;
    //内容
    private String content;
    private String image;
    //视频
    private String video;
    //piece类型
    private Integer type;
    //经度
    private Double longitude;
    //维度
    private Double latitude;
    //创建时间
    private Date createdTime;
    //浏览次数
    private Double viewCount;
//    点赞功能
//    private Double clickLike;
    //可见性
    private Boolean visibility;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Integer authorID) {
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Double getViewCount() {
        return viewCount;
    }

    public void setViewCount(Double viewCount) {
        this.viewCount = viewCount;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }


}
