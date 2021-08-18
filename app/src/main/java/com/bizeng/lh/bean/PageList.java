package com.bizeng.lh.bean;


import java.util.ArrayList;
import java.util.List;

public class PageList<T> {

    private int     curPage; //当前页数
    private int     pageCount; //总页数
    private int     total; //总条数
    private List<T> rows;

    public int getCurPage() {
        return curPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRows(ArrayList<T> rows) {
        this.rows = rows;
    }
}
