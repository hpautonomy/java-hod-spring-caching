package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HodCacheNameResolverTest {
    private HodCacheNameResolver cacheNameResolver;

    @Before
    public void setUp() {
        cacheNameResolver = new HodCacheNameResolverImpl();
    }

    @Test
    public void getOriginalPerApplicationName() {
        final String resolvedName = "DOMAIN:APPLICATION:cacheName";
        assertThat(cacheNameResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void getOriginalPerApplicationNameWithDomainColons() {
        final String resolvedName = "DOM\\:AIN:APPLICATION:cacheName";
        assertThat(cacheNameResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void resolveAndGetOriginalPerApplicationName() {
        final ResourceIdentifier hodApplication = new ResourceIdentifier("my:domain", "applications\\1");
        final String originalName = "myCache";
        assertThat(cacheNameResolver.getOriginalName(cacheNameResolver.resolvePerApplicationCacheName(originalName, hodApplication)), is(originalName));
    }

    @Test
    public void getOriginalPerUserName() {
        final String resolvedName = "DOMAIN:APPLICATION:userId:cacheName";
        assertThat(cacheNameResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void getOriginalPerUserNameWithUserIdColons() {
        final String resolvedName = "DOM\\:AIN:APPLICATION:user\\:\\\\Id:cacheName";
        assertThat(cacheNameResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void resolveAndGetOriginalPerUserName() {
        final ResourceIdentifier hodApplication = new ResourceIdentifier("my:domain", "applications\\1");
        final String userId = UUID.randomUUID().toString();
        final String originalName = "myCache";
        assertThat(cacheNameResolver.getOriginalName(cacheNameResolver.resolvePerUserCacheName(originalName, hodApplication, userId)), is(originalName));
    }
}
