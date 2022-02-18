package tech.minesoft.minetv.v4app.vo;

import java.io.Serializable;

public class UrlInfo implements Serializable {
    static final long serialVersionUID = 727566175075960653L;

    private Integer vodId;
    private Long infoId;
    private String vodName;
    private String groupName;
    private String itemName;
    private String playUrl;
    private boolean viewed;

    private String url;
    private String[] urls;

    public Integer getVodId() {
        return vodId;
    }

    public void setVodId(Integer vodId) {
        this.vodId = vodId;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }
}
