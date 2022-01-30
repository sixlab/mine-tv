package tech.minesoft.minetv.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class MineSiteInfo {

    @Id
    private Long id;

    @Unique
    private String code;

    private String name;
    private String url;

    private Integer primary;
    private Integer status;
    @Generated(hash = 1178978546)
    public MineSiteInfo(Long id, String code, String name, String url,
            Integer primary, Integer status) {
        this.id = id;
        this.code = code;
        this.name = name;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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
