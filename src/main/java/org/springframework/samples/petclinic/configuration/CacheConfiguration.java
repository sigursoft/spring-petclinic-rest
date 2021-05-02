package org.springframework.samples.petclinic.configuration;

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
            var cacheName = "vets";
            var config = new ConfigurationBuilder()
                .clustering()
                .simpleCache(true)
                .memory().maxCount(1000L)
                .build();
            manager.defineConfiguration(cacheName, config);
        };
    }
}
