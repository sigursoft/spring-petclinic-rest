package org.springframework.samples.petclinic.configuration;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.spring.starter.embedded.InfinispanGlobalConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CacheTestConfiguration {

    @Bean
    public InfinispanGlobalConfigurer infinispanGlobalConfigurer() {
        return () -> new GlobalConfigurationBuilder().jmx().disable().build();
    }
}
