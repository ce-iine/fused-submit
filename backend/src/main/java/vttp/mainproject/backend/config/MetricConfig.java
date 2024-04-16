package vttp.mainproject.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@Configuration
public class MetricConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean    
    public MeterRegistry getMeterRegistry() {
        CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();        
        return meterRegistry;    
    }

    

}
