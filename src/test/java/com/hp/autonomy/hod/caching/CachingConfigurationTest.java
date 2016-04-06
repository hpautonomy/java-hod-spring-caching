package com.hp.autonomy.hod.caching;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CachingConfigurationTest.MockConfiguration.class, CachingConfiguration.class})
public class CachingConfigurationTest {
    @Autowired
    private CacheResolver cacheResolver;

    @Autowired
    private CacheResolver simpleCacheResolver;

    @Test
    public void wiring() {
        assertNotNull(cacheResolver);
        assertNotNull(simpleCacheResolver);
        assertNotEquals(cacheResolver, simpleCacheResolver);
    }

    public static class MockConfiguration {
        @Bean
        public CacheManager cacheManager() {
            return mock(CacheManager.class);
        }
    }
}
