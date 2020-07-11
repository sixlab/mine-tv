package tech.minesoft.minetv.vo;

import java.util.List;

public class VodListVo {

    private Integer code;
    private String msg;
    private String page;
    private Integer pagecount;
    private String limit;
    private Integer total;

    private List<VodInfo> list;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getPagecount() {
        return pagecount;
    }

    public void setPagecount(Integer pagecount) {
        this.pagecount = pagecount;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<VodInfo> getList() {
        return list;
    }

    public void setList(List<VodInfo> list) {
        this.list = list;
    }
}
