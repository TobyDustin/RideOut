package org.io.rideout.authentication;

import org.io.rideout.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class UserSecurityContext implements SecurityContext {

    private User user;
    private String authScheme;
    private boolean secure;

    public UserSecurityContext(User user, String authScheme, boolean secure) {
        this.user = user;
        this.authScheme = authScheme;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> user.getId().toHexString();
    }

    @Override
    public boolean isUserInRole(String s) {
        return user.getRole().equals(s);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return authScheme;
    }

    public User getUser() {
        return user;
    }
}
