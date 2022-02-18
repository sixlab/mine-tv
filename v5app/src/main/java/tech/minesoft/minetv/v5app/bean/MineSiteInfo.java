package tech.minesoft.minetv.v5app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity
public class MineSiteInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Unique
    private String code;

    private String url;

    private Integer primary;
    private Integer status;
    @Generated(hash = 328745038)
    public MineSiteInfo(Long id, String code, String url, Integer primary,
            Integer status) {
        this.id = id;
        this.code = code;
        this.url = url;
        this.primary = primary;
        this.status = status;
    }
    @Generated(hash = 821031358)
    public MineSiteInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getPrimary() {
        return this.primary;
    }
    public void setPrimary(Integer primary) {
        this.primary = primary;
    }
    public Integer getStatus() {
        return this.status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
