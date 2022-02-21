package tech.minesoft.minetv.v5app.vo;

import com.shuyu.gsyvideoplayer.model.GSYVideoModel;

public class VideoPlayerModel extends GSYVideoModel {

    private UrlInfo urlInfo;
    private String itemName;

    public VideoPlayerModel(String url, String title) {
        super(url, title);
    }

    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
