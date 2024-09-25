package com.dataan.vo;

import java.util.List;

/**
 * @author zhan bing liang
 * @date 2024/6/4 16:17
 */
public class Pageable<T> {
    private List<T> data;
    private Integer total;
    private Integer page;
    private Integer size;

    public Pageable(List<T> data, Integer total, Integer page, Integer size) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
