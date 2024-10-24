package com.udise.portal.vo.job;

import java.util.Arrays;

public class TableRowData {
    private String[] cellTexts;
    private String buttonXPath;

    public TableRowData(String[] cellTexts, String buttonXPath) {
        this.cellTexts = cellTexts;
        this.buttonXPath = buttonXPath;
    }

    public String[] getCellTexts() {
        return cellTexts;
    }

    public String getButtonXPath() {
        return buttonXPath;
    }

    @Override
    public String toString() {
        return "TableRowData{" +
                "cellTexts=" + Arrays.toString(cellTexts) +
                ", buttonXPath='" + buttonXPath + '\'' +
                '}';
    }
}

