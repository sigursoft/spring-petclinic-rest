package org.springframework.samples.petclinic.configuration;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public InfinispanCacheConfigurer infinispanCacheConfigurer() {
        return manager -> {
            final org.infinispan.configuration.cache.Configuration config = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.LOCAL)
                .memory().maxCount(1000L)
                .build();
            var cacheName = "vets";
            manager.defineConfiguration(cacheName, config);
        };
    }
}
