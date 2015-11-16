package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceIdentifier;
import com.hp.autonomy.hod.sso.HodAuthentication;
import com.hp.autonomy.hod.sso.HodAuthenticationPrincipal;
import org.junit.Test;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HodApplicationCacheResolverTest {

    @Test
    public void testGetCacheNames() {
        final HodAuthenticationPrincipal principal = mock(HodAuthenticationPrincipal.class);
        when(principal.getApplication()).thenReturn(new ResourceIdentifier("DOMAIN", "APPLICATION"));

        final HodAuthentication hodAuthentication = mock(HodAuthentication.class);
        when(hodAuthentication.getPrincipal()).thenReturn(principal);

        final SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(hodAuthentication);

        SecurityContextHolder.setContext(securityContext);

        final Set<String> cacheNames = new HashSet<>(Arrays.asList(
            "cacheName",
            "fooCache",
            "barCache"
        ));

        final BasicOperation basicOperation = mock(BasicOperation.class);
        when(basicOperation.getCacheNames()).thenReturn(cacheNames);

        final CacheOperationInvocationContext<?> cacheOperationInvocationContext = mock(CacheOperationInvocationContext.class);
        when(cacheOperationInvocationContext.getOperation()).thenReturn(basicOperation);

        final HodApplicationCacheResolver hodApplicationCacheResolver = new HodApplicationCacheResolver();

        final Collection<String> resolvedNames = hodApplicationCacheResolver.getCacheNames(cacheOperationInvocationContext);

        assertThat(resolvedNames, hasSize(3));

        assertThat(resolvedNames, hasItems(
            "DOMAIN:APPLICATION:cacheName",
            "DOMAIN:APPLICATION:fooCache",
            "DOMAIN:APPLICATION:barCache"
        ));
    }

    @Test
    public void testGetOriginalName() {
        final String resolvedName = "DOMAIN:APPLICATION:cacheName";

        assertThat(HodApplicationCacheResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void testGetOriginalNameWithDomainColons() {
        final String resolvedName = "DOM\\:AIN:APPLICATION:cacheName";

        assertThat(HodApplicationCacheResolver.getOriginalName(resolvedName), is("cacheName"));
    }

    @Test
    public void testResolveAndGetOriginalName() {
        final ResourceIdentifier hodApplication = new ResourceIdentifier("my:domain", "applications\\1");
        final String originalName = "myCache";
        assertThat(HodApplicationCacheResolver.getOriginalName(HodApplicationCacheResolver.resolveName(originalName, hodApplication)), is(originalName));
    }

}