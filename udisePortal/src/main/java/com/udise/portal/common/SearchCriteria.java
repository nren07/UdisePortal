package com.udise.portal.common;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {

    // pagination
    private Integer startPosition;

    private Integer maxResult;

    // search
    private Map<String, String> searchFields;

    private Map<String, MultiValueSearchToken> multiValueSearchFields;
    // sort

    private Map<String, String> sortFields;

    private String searchScopeMedicine;

    private Boolean singleMedicine;

    private String fromDate;

    private String toDate;

    private boolean preCondition;

    private int draw;

    private String orderTable = "paytmwallet_order";

    private String settlementTable = "paytmwallet_settlement";

    private String transactionId;

    private Boolean isArchive;

    public SearchCriteria() {
        this(null, null);
    }

    public SearchCriteria(Integer startPosition, Integer maxResult) {
        this.startPosition = startPosition;
        this.maxResult = maxResult;
        this.searchFields = new HashMap<String, String>();
        this.sortFields = new HashMap<String, String>();
        this.multiValueSearchFields = new HashMap<>();
    }

    // add searhField

    public SearchCriteria addSearchField(final String fieldName, final String value) {
        if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(value)) {
            this.searchFields.put(StringUtils.trim(fieldName), StringUtils.trim(value));
        }
        return this;
    }

    public SearchCriteria addSearchField(final MultiValueSearchToken.SearchType searchType, final String fieldName, final String value) {
        if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(value)) {
            if (MultiValueSearchToken.SearchType.LIKE.equals(searchType)) {
                String[] values = StringUtils.trim(value).split(",");
                if (values.length > 1) {
                    MultiValueSearchToken searchToken = new MultiValueSearchToken(searchType, values);
                    this.multiValueSearchFields.put(StringUtils.trim(fieldName), searchToken);
                } else {
                    this.searchFields.put(StringUtils.trim(fieldName), StringUtils.trim(value));
                }

            } else if (MultiValueSearchToken.SearchType.IN.equals(searchType) || MultiValueSearchToken.SearchType.BETWEEN.equals(searchType)) {
                String[] values = StringUtils.trim(value).split(",");
                if (values.length > 1) {
                    MultiValueSearchToken searchToken = new MultiValueSearchToken(searchType, values);
                    this.multiValueSearchFields.put(StringUtils.trim(fieldName), searchToken);
                } else {
                    this.searchFields.put(StringUtils.trim(fieldName), StringUtils.trim(value));
                }
            } else {
                this.searchFields.put(StringUtils.trim(fieldName), StringUtils.trim(value));
            }
        }
        return this;
    }

    public SearchCriteria addSortField(String fieldName, String value) {
        this.sortFields.put(StringUtils.trim(fieldName), StringUtils.trim(value));
        return this;
    }

    // setters

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

    // getters

    public Integer getStartPosition() {
        return startPosition;
    }

    public Integer getMaxResult() {
        return maxResult;
    }

    public Map<String, String> getSearchFields() {
        return searchFields;
    }

    public boolean hasSearchField(final String fieldName) {
        if (getSearchFields().containsKey(fieldName)) {
            return true;
        }
        return false;
    }

    public String getSearchField(final String fieldName) {
        return getSearchFields().get(fieldName);
    }

    public Map<String, MultiValueSearchToken> getMultiValueSearchFields() {
        return multiValueSearchFields;
    }

    public Map<String, String> getSortFields() {
        return sortFields;
    }

    public boolean hasSortField(final String fieldName) {
        if (getSortFields().containsKey(fieldName)) {
            return true;
        }
        return false;
    }

    public String getSortField(final String fieldName) {
        return getSortFields().get(fieldName);
    }

    public boolean needToSort() {
        return getSortFields().isEmpty() == false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getSearchScopeMedicine() {
        return searchScopeMedicine;
    }

    public void setSearchScopeMedicine(String searchScopeMedicine) {
        this.searchScopeMedicine = searchScopeMedicine;
    }

    public Boolean getIsSingleMedicine() {
        return singleMedicine;
    }

    public void setIsSingleMedicine(Boolean isSingleMedicine) {
        this.singleMedicine = isSingleMedicine;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public boolean isPreCondition() {
        return preCondition;
    }

    public void setPreCondition(Boolean preCondition) {
        this.preCondition = preCondition;
    }

    public String getFronDate() {
        return fromDate;
    }

    public void setFronDate(String fronDate) {
        this.fromDate = fronDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(String orderTable) {
        this.orderTable = orderTable;
    }

    public String getSettlementTable() {
        return settlementTable;
    }

    public void setSettlementTable(String settlementTable) {
        this.settlementTable = settlementTable;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public void updateTableNameIfExit() {
        if (MapUtils.isNotEmpty(this.searchFields)) {
            if (this.searchFields.containsKey("paymentGateway")) {
                updateTablesNameBySearchKey("paymentGateway");
            } else if (this.searchFields.containsKey("gateway")) {
                updateTablesNameBySearchKey("gateway");
            } else if (this.searchFields.containsKey("payment_gateway")) {
                updateTablesNameBySearchKey("payment_gateway");
            } else if (this.searchFields.containsKey("PAYMENT_GATEWAY")) {
                updateTablesNameBySearchKey("PAYMENT_GATEWAY");
            } else if (this.searchFields.containsKey("paymentgateway")) {
                updateTablesNameBySearchKey("paymentgateway");
            }
        }
    }

    private void updateTablesNameBySearchKey(String key) {
        String gateway = this.searchFields.get(key);
        switch (gateway) {
            case "CASHFREE":
                this.orderTable = "cashfree_order";
                this.settlementTable = "cashfree_settlement";
                break;

            case "RAZORPAY":
                this.orderTable = "razorpay_order";
                this.settlementTable = "razorpay_settlement";
                break;

            case "TIMESOFMONEY":
                this.orderTable = "timesofmoney_order";
                this.settlementTable = "timesofmoney_settlement";
                break;

            case "TECHPROCESS":
                this.orderTable = "techprocess_order";
                this.settlementTable = "techprocess_settlement";
                break;

            case "PHONEPE":
                this.orderTable = "phonepe_order";
                this.settlementTable = "phonepe_settlement";

                break;

            default:
                this.orderTable = "paytmwallet_order";
                this.settlementTable = "paytmwallet_settlement";
                break;
        }
    }

}

