package com.udise.portal.utils;

public interface UserContextResolver {
//    public abstract Long resolveLoggedInUserId();

//    public abstract RoleName resolveLoggedInUserRole();

    public abstract String resolveIpAddress();
}
