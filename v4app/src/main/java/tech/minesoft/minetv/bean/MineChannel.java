package tech.minesoft.minetv.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class MineChannel {

    @Id
    private Long id;
    @Unique
    private String name;
    private Integer exclude;
    @Generated(hash = 1083518140)
    public MineChannel(Long id, String name, Integer exclude) {
        this.id = id;
        this.name = name;
        this.exclude = exclude;
    }
    @Generated(hash = 1711796907)
    public MineChannel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getExclude() {
        return this.exclude;
    }
    public void setExclude(Integer exclude) {
        this.exclude = exclude;
    }
}
