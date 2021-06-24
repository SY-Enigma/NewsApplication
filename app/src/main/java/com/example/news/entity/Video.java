package com.example.news.entity;

public class Video {
    private String playaddr;
    private String coverurl;
    private String title;
    private String author;
    private String avatar;

    public Video(String addr,String cover,String tit,String auth,String touxiang){
        this.playaddr = addr;
        this.coverurl = cover;
        this.title = tit;
        this.author = auth;
        this.avatar = touxiang;
    }

    public void setPlayaddr(String playaddr) {
        this.playaddr = playaddr;
    }

    public String getPlayaddr() {
        return playaddr;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }
}
