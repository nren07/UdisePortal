package com.udise.portal.common;

import java.util.Arrays;
import java.util.List;

public class MultiValueSearchToken {
    public static enum SearchType {
        BETWEEN, IN, LIKE
    };

    private List<String> searchValues;

    private SearchType searchType;

    public MultiValueSearchToken(final SearchType searchType, final String[] searchValues) {
        this.searchType = searchType;
        if (searchValues != null && searchValues.length >= 1) {
            this.searchValues = Arrays.asList(searchValues);
        }
    }

    public List<String> getSearchValues() {
        return searchValues;
    }

    public void addSearchValue(final String[] searchValues) {
        if (searchValues != null && searchValues.length >= 1) {
            this.searchValues = Arrays.asList(searchValues);
        }
    }

    public SearchType getSearchType() {
        return searchType;
    }
}
