package com.udise.portal.utils.impl;

import com.udise.portal.common.CoreUtil;
import com.udise.portal.common.JWTConstants;
import com.udise.portal.utils.UserContextResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class UserContextResolverImpl implements UserContextResolver {
//    @Override
//    public RoleName resolveLoggedInUserRole() {
//        String authToken = getAuthToken();
//        Object roleObj = CoreUtil.getClaim(JWTConstants.CLAIM_ROLE, authToken);
//        String role = roleObj != null ? (String) roleObj : null;
//        return RoleName.valueOf(role);
//    }

//    @Override
//    public Long resolveLoggedInUserId() {
//        String authToken = getAuthToken();
//        Long userId = authToken != null ? (Long) CoreUtil.getClaim(JWTConstants.CLAIM_USER_ID, authToken) : null;
//        return userId;
//    }
//
//	public String getAuthToken() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String authToken = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
//		return authToken;
//	}

    public String getAuthToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authToken = null;
        if (authentication != null && authentication.getCredentials() != null) {
            authToken = authentication.getCredentials().toString();
        }
        return authToken;

    }
//	public String getAuthToken() {
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    String authToken = null;
//	    if (authentication != null && authentication.getCredentials() != null) {
//	        String s = authentication.getCredentials().toString();
//	        if (s != null) {
//	            int index = s.indexOf("String");
//	            authToken = index >= 0 ? s.substring(index) : null;
//	        }
//	    }
//	    return authToken;
//	}

    @Override
    public String resolveIpAddress() {
        Object authDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        String ipAddress = null;
        if (authDetails instanceof WebAuthenticationDetails) {
            ipAddress = ((WebAuthenticationDetails) authDetails).getRemoteAddress();
        }
        return ipAddress;
    }

}
