package tech.minesoft.minetv.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class MineViewInfo {

    @Id
    private Long id;
    private Long info_id;
    private String vod_name;
    private String vod_from;
    private String vod_item_name;
    private Integer view_position;
    private Date vod_time;
    @Generated(hash = 714643202)
    public MineViewInfo(Long id, Long info_id, String vod_name, String vod_from,
            String vod_item_name, Integer view_position, Date vod_time) {
        this.id = id;
        this.info_id = info_id;
        this.vod_name = vod_name;
        this.vod_from = vod_from;
        this.vod_item_name = vod_item_name;
        this.view_position = view_position;
        this.vod_time = vod_time;
    }
    @Generated(hash = 1004844870)
    public MineViewInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getInfo_id() {
        return this.info_id;
    }
    public void setInfo_id(Long info_id) {
        this.info_id = info_id;
    }
    public String getVod_name() {
        return this.vod_name;
    }
    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }
    public String getVod_from() {
        return this.vod_from;
    }
    public void setVod_from(String vod_from) {
        this.vod_from = vod_from;
    }
    public String getVod_item_name() {
        return this.vod_item_name;
    }
    public void setVod_item_name(String vod_item_name) {
        this.vod_item_name = vod_item_name;
    }
    public Integer getView_position() {
        return this.view_position;
    }
    public void setView_position(Integer view_position) {
        this.view_position = view_position;
    }
    public Date getVod_time() {
        return this.vod_time;
    }
    public void setVod_time(Date vod_time) {
        this.vod_time = vod_time;
    }

}
