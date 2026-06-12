package com.example.assignmentai.common.security;

import com.example.assignmentai.common.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new UnauthorizedException("User is not authenticated");

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedUser authenticatedUser))
            throw new UnauthorizedException("Invalid authenticated user");

        return authenticatedUser.id();
    }
}
