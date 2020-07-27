package tech.minesoft.minetv.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MineMeta {

    @Id
    private Long id;

    private String meta;
    private String code;
    private String val;
    @Generated(hash = 961597343)
    public MineMeta(Long id, String meta, String code, String val) {
        this.id = id;
        this.meta = meta;
        this.code = code;
        this.val = val;
    }
    @Generated(hash = 285905870)
    public MineMeta() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMeta() {
        return this.meta;
    }
    public void setMeta(String meta) {
        this.meta = meta;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getVal() {
        return this.val;
    }
    public void setVal(String val) {
        this.val = val;
    }
}
