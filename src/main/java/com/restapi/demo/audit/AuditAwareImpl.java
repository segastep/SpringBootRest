package com.restapi.demo.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author G.Nikolov on 14/10/18
 * @project rest-service-basic
 */
public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        //Returning optional of hardcoded value since no security is implemented if security
        // is added we can return:
        // Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
        return Optional.of("DEMO_USER");

    }
}
