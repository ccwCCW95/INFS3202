package com.ccw.project.entities;

public class User {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String image;

    private String salt;

    private String usertype;

    private String sequs1;

    private Integer column9;

    private String sequs2;

    private boolean rememberme;


    public User(Integer id, String username, String password, String email, String image, String salt, String usertype, String sequs1, Integer column9, String sequs2) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.image = image;
        this.salt = salt;
        this.usertype = usertype;
        this.sequs1 = sequs1;
        this.column9 = column9;
        this.sequs2 = sequs2;
    }

    public boolean getRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype == null ? null : usertype.trim();
    }

    public String getSequs1() {
        return sequs1;
    }

    public void setSequs1(String sequs1) {
        this.sequs1 = sequs1 == null ? null : sequs1.trim();
    }

    public Integer getColumn9() {
        return column9;
    }

    public void setColumn9(Integer column9) {
        this.column9 = column9;
    }

    public String getSequs2() {
        return sequs2;
    }

    public void setSequs2(String sequs2) {
        this.sequs2 = sequs2 == null ? null : sequs2.trim();
    }
}