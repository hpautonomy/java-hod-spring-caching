/*
 * Copyright 2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.hod.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings({"WeakerAccess", "SpringJavaAutowiringInspection"})
@ConditionalOnBean(CacheManager.class)
@Configuration
public class CachingConfiguration extends CachingConfigurerSupport {
    public static final String SIMPLE_CACHE_RESOLVER_NAME = "simpleCacheResolver";

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Bean
    public CacheResolver cacheResolver() {
        final HodApplicationCacheResolver hodApplicationCacheResolver = new HodApplicationCacheResolver();
        hodApplicationCacheResolver.setCacheManager(cacheManager);

        return hodApplicationCacheResolver;
    }

    // Resolver for caches which are not application-specific
    @Bean(name = SIMPLE_CACHE_RESOLVER_NAME)
    public CacheResolver simpleCacheResolver() {
        final SimpleCacheResolver resolver = new SimpleCacheResolver();
        resolver.setCacheManager(cacheManager);
        return resolver;
    }
}
