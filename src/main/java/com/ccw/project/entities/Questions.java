package com.ccw.project.entities;

import java.util.Date;

public class Questions {
    private Integer id;

    private String title;

    private Integer author;

    private String description;

    private Date time;

    private String pinId;

    private Integer group;

    private Integer views;

    public Questions(Integer id, String title, Integer author, String description, Date time, String pinId, Integer group, Integer views) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.time = time;
        this.pinId = pinId;
        this.group = group;
        this.views = views;
    }

    public Questions() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId == null ? null : pinId.trim();
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}