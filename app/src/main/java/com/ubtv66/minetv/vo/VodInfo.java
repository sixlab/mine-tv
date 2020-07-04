package com.ubtv66.minetv.vo;

import java.io.Serializable;

public class VodInfo implements Serializable {
    static final long serialVersionUID = 727566175075960653L;

    private Integer vod_id;

    private String vod_name;

    private String vod_pic;

    private String type_name;

    private String vod_director;

    private String vod_actor;

    private String vod_area;

    private String vod_year;

    private String vod_remarks;

    private String vod_content;

    private String vod_play_from;

    private String vod_play_server;

    private String vod_play_note;

    private String vod_play_url;

    public Integer getVod_id() {
        return vod_id;
    }

    public void setVod_id(Integer vod_id) {
        this.vod_id = vod_id;
    }

    public String getVod_name() {
        return vod_name;
    }

    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }

    public String getVod_pic() {
        return vod_pic;
    }

    public void setVod_pic(String vod_pic) {
        this.vod_pic = vod_pic;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getVod_director() {
        return vod_director;
    }

    public void setVod_director(String vod_director) {
        this.vod_director = vod_director;
    }

    public String getVod_actor() {
        return vod_actor;
    }

    public void setVod_actor(String vod_actor) {
        this.vod_actor = vod_actor;
    }

    public String getVod_area() {
        return vod_area;
    }

    public void setVod_area(String vod_area) {
        this.vod_area = vod_area;
    }

    public String getVod_year() {
        return vod_year;
    }

    public void setVod_year(String vod_year) {
        this.vod_year = vod_year;
    }

    public String getVod_remarks() {
        return vod_remarks;
    }

    public void setVod_remarks(String vod_remarks) {
        this.vod_remarks = vod_remarks;
    }

    public String getVod_content() {
        return vod_content;
    }

    public void setVod_content(String vod_content) {
        this.vod_content = vod_content;
    }

    public String getVod_play_from() {
        return vod_play_from;
    }

    public void setVod_play_from(String vod_play_from) {
        this.vod_play_from = vod_play_from;
    }

    public String getVod_play_server() {
        return vod_play_server;
    }

    public void setVod_play_server(String vod_play_server) {
        this.vod_play_server = vod_play_server;
    }

    public String getVod_play_note() {
        return vod_play_note;
    }

    public void setVod_play_note(String vod_play_note) {
        this.vod_play_note = vod_play_note;
    }

    public String getVod_play_url() {
        return vod_play_url;
    }

    public void setVod_play_url(String vod_play_url) {
        this.vod_play_url = vod_play_url;
    }
}
