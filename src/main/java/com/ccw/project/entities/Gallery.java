package com.ccw.project.entities;

public class Gallery {
    private Integer id;

    private Integer userid;

    private String img1;

    private String img2;

    private String img3;

    public Gallery(Integer id, Integer userid, String img1, String img2, String img3) {
        this.id = id;
        this.userid = userid;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }

    public Gallery() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1 == null ? null : img1.trim();
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2 == null ? null : img2.trim();
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3 == null ? null : img3.trim();
    }
}