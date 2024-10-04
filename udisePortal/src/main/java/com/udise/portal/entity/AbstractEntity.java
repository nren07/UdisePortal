package com.udise.portal.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -9199765418025422605L;

    public static final String ACTIVE = "active";

    public static final String TENANT_ID = "tenantId";

    public static final String FIELD_ID = "id";

    public static final String VERSION_2 = "";
    public static final String DASHBOARD_PRIVILEGE_NAME = "Dashboard";
    public static final String REPORT_PRIVILEGE_NAME = "Reports";
    public static final String TRANSACTION_PRIVILEGE_NAME = "Transaction";

    private Long id;

    private boolean active;

    private String tenantId;

    public AbstractEntity() {

    }

    public AbstractEntity(final Long id) {
        this.id = id;
    }

    public AbstractEntity(boolean active2) {
        this.active = active2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasId() {
        return id != null && id > 0;
    }

    public boolean hasSameId(AbstractEntity other) {
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Column(name = "tenant_id")//, nullable = false
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


}
