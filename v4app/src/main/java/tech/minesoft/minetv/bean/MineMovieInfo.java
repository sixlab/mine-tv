package tech.minesoft.minetv.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class MineMovieInfo {

    @Id
    private Long id;
    private Integer star_flag;
    private Integer vod_hide;
    private Integer vod_reverse;
    private Date star_time;
    private Date last_open;

    private String api_url;
    private String api_code;
    private String api_name;
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
    @Generated(hash = 1105747096)
    public MineMovieInfo(Long id, Integer star_flag, Integer vod_hide,
            Integer vod_reverse, Date star_time, Date last_open, String api_url,
            String api_code, String api_name, Integer vod_id, String vod_name,
            String vod_pic, String type_name, String vod_director, String vod_actor,
            String vod_area, String vod_year, String vod_remarks,
            String vod_content, String vod_play_from, String vod_play_server,
            String vod_play_note, String vod_play_url) {
        this.id = id;
        this.star_flag = star_flag;
        this.vod_hide = vod_hide;
        this.vod_reverse = vod_reverse;
        this.star_time = star_time;
        this.last_open = last_open;
        this.api_url = api_url;
        this.api_code = api_code;
        this.api_name = api_name;
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_pic = vod_pic;
        this.type_name = type_name;
        this.vod_director = vod_director;
        this.vod_actor = vod_actor;
        this.vod_area = vod_area;
        this.vod_year = vod_year;
        this.vod_remarks = vod_remarks;
        this.vod_content = vod_content;
        this.vod_play_from = vod_play_from;
        this.vod_play_server = vod_play_server;
        this.vod_play_note = vod_play_note;
        this.vod_play_url = vod_play_url;
    }
    @Generated(hash = 281397076)
    public MineMovieInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStar_flag() {
        return this.star_flag;
    }
    public void setStar_flag(Integer star_flag) {
        this.star_flag = star_flag;
    }
    public Integer getVod_reverse() {
        return this.vod_reverse;
    }
    public void setVod_reverse(Integer vod_reverse) {
        this.vod_reverse = vod_reverse;
    }
    public Date getStar_time() {
        return this.star_time;
    }
    public void setStar_time(Date star_time) {
        this.star_time = star_time;
    }
    public Date getLast_open() {
        return this.last_open;
    }
    public void setLast_open(Date last_open) {
        this.last_open = last_open;
    }
    public String getApi_url() {
        return this.api_url;
    }
    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }
    public String getApi_code() {
        return this.api_code;
    }
    public void setApi_code(String api_code) {
        this.api_code = api_code;
    }
    public String getApi_name() {
        return this.api_name;
    }
    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }
    public Integer getVod_id() {
        return this.vod_id;
    }
    public void setVod_id(Integer vod_id) {
        this.vod_id = vod_id;
    }
    public String getVod_name() {
        return this.vod_name;
    }
    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }
    public String getVod_pic() {
        return this.vod_pic;
    }
    public void setVod_pic(String vod_pic) {
        this.vod_pic = vod_pic;
    }
    public String getType_name() {
        return this.type_name;
    }
    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
    public String getVod_director() {
        return this.vod_director;
    }
    public void setVod_director(String vod_director) {
        this.vod_director = vod_director;
    }
    public String getVod_actor() {
        return this.vod_actor;
    }
    public void setVod_actor(String vod_actor) {
        this.vod_actor = vod_actor;
    }
    public String getVod_area() {
        return this.vod_area;
    }
    public void setVod_area(String vod_area) {
        this.vod_area = vod_area;
    }
    public String getVod_year() {
        return this.vod_year;
    }
    public void setVod_year(String vod_year) {
        this.vod_year = vod_year;
    }
    public String getVod_remarks() {
        return this.vod_remarks;
    }
    public void setVod_remarks(String vod_remarks) {
        this.vod_remarks = vod_remarks;
    }
    public String getVod_content() {
        return this.vod_content;
    }
    public void setVod_content(String vod_content) {
        this.vod_content = vod_content;
    }
    public String getVod_play_from() {
        return this.vod_play_from;
    }
    public void setVod_play_from(String vod_play_from) {
        this.vod_play_from = vod_play_from;
    }
    public String getVod_play_server() {
        return this.vod_play_server;
    }
    public void setVod_play_server(String vod_play_server) {
        this.vod_play_server = vod_play_server;
    }
    public String getVod_play_note() {
        return this.vod_play_note;
    }
    public void setVod_play_note(String vod_play_note) {
        this.vod_play_note = vod_play_note;
    }
    public String getVod_play_url() {
        return this.vod_play_url;
    }
    public void setVod_play_url(String vod_play_url) {
        this.vod_play_url = vod_play_url;
    }
    public Integer getVod_hide() {
        return this.vod_hide;
    }
    public void setVod_hide(Integer vod_hide) {
        this.vod_hide = vod_hide;
    }

}
