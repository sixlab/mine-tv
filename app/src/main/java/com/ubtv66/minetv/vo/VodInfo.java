package com.ubtv66.minetv.vo;

import java.io.Serializable;

public class VodInfo implements Serializable {
    static final long serialVersionUID = 727566175075960653L;
    private Integer id;

    private String vodName;

    private String vodGroup;

    private String vodPic;

    private String vodArea;

    private String vodLang;

    private String vodYear;

    private String vodActor;

    private String vodDirector;

    private String vodIntro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getVodGroup() {
        return vodGroup;
    }

    public void setVodGroup(String vodGroup) {
        this.vodGroup = vodGroup;
    }

    public String getVodPic() {
        return vodPic;
    }

    public void setVodPic(String vodPic) {
        this.vodPic = vodPic;
    }

    public String getVodArea() {
        return vodArea;
    }

    public void setVodArea(String vodArea) {
        this.vodArea = vodArea;
    }

    public String getVodLang() {
        return vodLang;
    }

    public void setVodLang(String vodLang) {
        this.vodLang = vodLang;
    }

    public String getVodYear() {
        return vodYear;
    }

    public void setVodYear(String vodYear) {
        this.vodYear = vodYear;
    }

    public String getVodActor() {
        return vodActor;
    }

    public void setVodActor(String vodActor) {
        this.vodActor = vodActor;
    }

    public String getVodDirector() {
        return vodDirector;
    }

    public void setVodDirector(String vodDirector) {
        this.vodDirector = vodDirector;
    }

    public String getVodIntro() {
        return vodIntro;
    }

    public void setVodIntro(String vodIntro) {
        this.vodIntro = vodIntro;
    }
}
