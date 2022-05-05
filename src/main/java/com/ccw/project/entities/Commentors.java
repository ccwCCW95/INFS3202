package com.ccw.project.entities;

import java.util.Date;

public class Commentors {
    private Integer id;

    private Integer qId;

    private Integer cId;

    private String c_name;

    private Date time;

    private Integer rating;

    private String content;

    public Commentors(Integer id, Integer qId, Integer cId, String c_name, Date time, Integer rating, String content) {
        this.id = id;
        this.qId = qId;
        this.cId = cId;
        this.c_name = c_name;
        this.time = time;
        this.rating = rating;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public Integer getqId() {
        return qId;
    }

    public Integer getcId() {
        return cId;
    }

    public String getC_name() {
        return c_name;
    }

    public Date getTime() {
        return time;
    }

    public Integer getRating() {
        return rating;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setqId(Integer qId) {
        this.qId = qId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
