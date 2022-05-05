package com.ccw.project.entities;

import java.util.Date;

public class Comments {
    private Integer id;

    private Integer qId;

    private Integer cId;

    private Date time;

    private Integer rating;

    private String content;

    public Comments(Integer id, Integer qId, Integer cId, Date time, Integer rating, String content) {
        this.id = id;
        this.qId = qId;
        this.cId = cId;
        this.time = time;
        this.rating = rating;
        this.content = content;
    }

    public Comments() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getqId() {
        return qId;
    }

    public void setqId(Integer qId) {
        this.qId = qId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}