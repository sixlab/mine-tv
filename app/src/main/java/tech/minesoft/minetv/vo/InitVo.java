package tech.minesoft.minetv.vo;

import java.io.Serializable;
import java.util.List;

import tech.minesoft.minetv.bean.MineSiteInfo;
public class InitVo implements Serializable {

    private Integer version;
    private Integer minVersion;
    private String updateUrl;
    private String primary;
    private List<MineSiteInfo> sites;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Integer minVersion) {
        this.minVersion = minVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<MineSiteInfo> getSites() {
        return sites;
    }

    public void setSites(List<MineSiteInfo> sites) {
        this.sites = sites;
    }
}
