package com.plazoleta.infrastructure.security;

import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.infrastructure.security.userdetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextAdapter implements IUserContextPort {

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No hay un usuario autenticado en el contexto de seguridad");
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }
}