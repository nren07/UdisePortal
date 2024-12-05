package com.udise.portal.common;

public final class JWTConstants {
    public static final String SECURITY_KEY = "trfgt767hjnm****hghghgghgh65121tktr";

    public static final long SESSION_TIME_OUT = 120 * 60 * 60 * 1000;

    public static final String CLAIM_ROLE = "role";

    public static final String CLAIM_USER_ID = "userId";

    public static final String COMA_SEPERATOR = ",";

    public static final String TENANT_ID = "tenantId";

    private JWTConstants() {
        throw new AssertionError();
    }
}
