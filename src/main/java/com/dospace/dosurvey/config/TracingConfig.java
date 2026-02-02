package com.dospace.dosurvey.config;

import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class TracingConfig {

    @Bean
    ObservationPredicate noActuatorServerObservations() {
        return (name, context) -> {
            if ("http.server.requests".equals(name) && context instanceof ServerRequestObservationContext serverContext) {
                if (serverContext.getCarrier() instanceof HttpServletRequest request) {
                    String uri = request.getRequestURI();
                    return !uri.startsWith("/actuator") && !uri.startsWith("/eureka");
                }
            }
            return true;
        };
    }
}

