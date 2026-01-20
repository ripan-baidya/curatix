package com.curatix.config.audit;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {

    @Bean(name = "auditAware")
    public AuditorAware<@NonNull String> auditorAware() {
        return new ApplicationAuditorAware();
    }
}
