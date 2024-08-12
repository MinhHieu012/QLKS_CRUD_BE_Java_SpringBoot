package vn.eledevo.vksbe.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import vn.eledevo.vksbe.entity.User;

import java.util.UUID;

public class SecurityUtils {
    public static UUID getUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        User user = (User) securityContext.getAuthentication().getPrincipal();
        return user.getId();
    }
}
