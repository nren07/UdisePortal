package com.udise.portal.common;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class WebUtils {
    private static final char CHAR_QUESTION_MARK = '?';

    private static final String X_ASCENT_AUTHTOKEN = "X-ASCENT-AUTHTOKEN";

    private static final String X_ASCENT_SSO_AUTHTOKEN = "X-ASCENT-SSO-AUTHTOKEN";

    private static final String TENANT_ID = "tenantId";

    public static final String SEARCH_CRITERIA = "searchCriteria";

    public static String getHeaderValue(HttpServletRequest request, String headerName) {
        final String value = request.getHeader(headerName);
        return value;
    }

//    public static String getUserId(HttpServletRequest httpServletRequest) {
//        final String jsonWebToken = getAuthToken(httpServletRequest);
//        if (StringUtils.isBlank(jsonWebToken)) {
//            return null;
//        }
//        Long userId = (Long) CoreUtil.getClaim(JWTConstants.CLAIM_USER_ID, jsonWebToken);
//        return userId.toString();
//    }

//    public static Long getParsedUserId(HttpServletRequest request) {
//        return Long.valueOf(getUserId(request));
//    }

    public static String getAuthToken(HttpServletRequest httpServletRequest) {
        String authToken = getHeaderValue(httpServletRequest, X_ASCENT_AUTHTOKEN);
        return authToken;
    }

    public static String getTenant(HttpServletRequest httpServletRequest) {
        String tenantId = getHeaderValue(httpServletRequest, TENANT_ID);
        return tenantId;
    }

    public static String getSsoAuthToken(HttpServletRequest httpServletRequest) {
        String authToken = getHeaderValue(httpServletRequest, X_ASCENT_SSO_AUTHTOKEN);
        return authToken;
    }

    public static Integer getRequestParamAsInt(HttpServletRequest request, String paramaName) {
        final String paramValueStr = request.getParameter(paramaName);
        final Integer paramValue = StringUtils.isNotBlank(paramValueStr) ? Integer.valueOf(paramValueStr) : null;
        return paramValue;
    }

    public static boolean getRequestParamAsBoolean(HttpServletRequest request, String paramaName) {
        String paramValueStr = request.getParameter(paramaName);
        boolean paramValue = StringUtils.isNotBlank(paramValueStr) ? Boolean.parseBoolean(paramValueStr) : false;
        return paramValue;
    }

    public static String getRequestParam(HttpServletRequest request, String paramaName) {
        String paramValue = request.getParameter(paramaName);
        return paramValue;
    }

    public static SearchCriteria getSearchCriteria(HttpServletRequest request) {
        SearchCriteria searchRequest = (SearchCriteria) request.getAttribute(SEARCH_CRITERIA);
        return searchRequest;
    }

    public static String extractUrl(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        StringBuilder requestURL = new StringBuilder(httpRequest.getRequestURL().toString());
        String queryString = httpRequest.getQueryString();
        if (CoreUtil.isNotNull(queryString)) {
            return requestURL.toString();
        } else {
            return requestURL.append(CHAR_QUESTION_MARK).append(queryString).toString();
        }
    }

    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
