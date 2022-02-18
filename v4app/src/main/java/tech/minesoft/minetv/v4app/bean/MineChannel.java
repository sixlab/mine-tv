package tech.minesoft.minetv.v4app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity
public class MineChannel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @Unique
    private String name;
    private Integer weight;
    private Integer status;
    @Generated(hash = 1928651380)
    public MineChannel(Long id, String name, Integer weight, Integer status) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.status = status;
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
    public Integer getWeight() {
        return this.weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public Integer getStatus() {
        return this.status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
