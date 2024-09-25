package com.dataan.vo;

/**指定注解修饰方法参数 springmvc拦截
 * @author zhan bing liang
 * @date 2024/6/25 14:14
 */
public class OrderVo {

    private String orderColumn;
    private Boolean desc;

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }
}
