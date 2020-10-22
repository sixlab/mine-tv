package tech.minesoft.minetv.vo;

import java.util.List;


public class Title {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String tabCode;
        private String name;
        private String icon;

        public String getTabCode() {
            return tabCode;
        }

        public void setTabCode(String tabCode) {
            this.tabCode = tabCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
