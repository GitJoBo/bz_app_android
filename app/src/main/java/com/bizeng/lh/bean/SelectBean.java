package com.bizeng.lh.bean;


public class SelectBean<T> {
    private boolean selected = false;
    private String name;
    private String legend;
    private int type;//1 api 未绑定，不可选,2api已绑定，3已选,不可取消;   9下方显示输入框[自定义仓位容量];
    private T extra;

    public SelectBean(String name,String legend) {
        this.name = name;
        this.legend = legend;
    }
    public SelectBean(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSelected() {
        this.selected = !selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getExtra() {
        return extra;
    }

    public void setExtra(T extra) {
        this.extra = extra;
    }

    public SelectBean(boolean selected, String name,String legend, int type, T extra) {
        this.selected = selected;
        this.name = name;
        this.legend = legend;
        this.type = type;
        this.extra = extra;
    }
}
