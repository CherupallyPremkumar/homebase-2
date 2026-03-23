package com.homebase.ecom.build.configuration;

import org.chenile.stm.STMSecurityStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Bridges Spring Security (Keycloak JWT) into the Chenile STM authorization model.
 *
 * The STM calls isAllowed(acls) before allowing any state transition.
 * ACLs come from meta-acls in the states XML (e.g. meta-acls="CUSTOMER,SYSTEM").
 *
 * Keycloak realm roles → SecurityConfig maps to SCOPE_<role> authorities.
 * This strategy checks if the current principal holds any of the required SCOPE_ authorities.
 *
 * SYSTEM transitions are allowed for authenticated service-to-service calls
 * that carry the SYSTEM role, OR for unauthenticated internal calls (no JWT present).
 */
@Component("springSecuritySTMStrategy")
public class SpringSecuritySTMStrategy implements STMSecurityStrategy {

    @Override
    public boolean isAllowed(String... acls) {
        if (acls == null || acls.length == 0) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // No authentication = internal/system call (e.g. scheduler, event consumer)
        // Allow only if SYSTEM is an accepted role for this transition
        if (auth == null || !auth.isAuthenticated()) {
            for (String acl : acls) {
                if ("SYSTEM".equalsIgnoreCase(acl.trim())) {
                    return true;
                }
            }
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        for (String acl : acls) {
            String required = "SCOPE_" + acl.trim();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(required)) {
                    return true;
                }
            }
        }

        return false;
    }
}
