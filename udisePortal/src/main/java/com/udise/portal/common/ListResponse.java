package com.udise.portal.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ListResponse<T> {
    private long recordsTotal;

    private long recordsFiltered;

    @JsonIgnore
    private int draw;

    private List<T> data;


    public ListResponse(long iTotalRecords, long iTotalDisplayRecords, List<T> aaData) {
        this(iTotalRecords, iTotalDisplayRecords, 0, aaData);
    }

    public ListResponse(long iTotalRecords, long recordsFiltered, int sEcho, List<T> aaData) {
        this.recordsTotal = iTotalRecords;
        this.recordsFiltered = recordsFiltered;
        this.draw = sEcho;
        this.data = aaData;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int sEcho) {
        this.draw = sEcho;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> aaData) {
        this.data = aaData;
    }

    public boolean isDataEmpty() {
        return CollectionUtils.isEmpty(data);
    }
}
