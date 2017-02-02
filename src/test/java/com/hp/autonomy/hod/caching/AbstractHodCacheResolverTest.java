package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceName;
import com.hp.autonomy.hod.sso.HodAuthentication;
import com.hp.autonomy.hod.sso.HodAuthenticationPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractHodCacheResolverTest {
    @Mock
    private SecurityContext securityContext;

    @Mock
    private HodAuthentication authentication;

    @Mock
    private HodAuthenticationPrincipal principal;

    final UUID userUuid = UUID.randomUUID();

    private AbstractHodCacheResolver cacheResolver;

    @Before
    public void setUp() {
        when(principal.getApplication()).thenReturn(new ResourceName("DOMAIN", "APPLICATION"));
        when(principal.getUserUuid()).thenReturn(userUuid);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        cacheResolver = constructCacheResolver();
    }

    protected abstract AbstractHodCacheResolver constructCacheResolver();

    protected abstract void validateCacheNames(final Collection<String> resolvedNames);

    @Test
    public void getCacheNames() {
        final Set<String> cacheNames = new HashSet<>(Arrays.asList(
                "cacheName",
                "fooCache",
                "barCache"
        ));

        final CacheOperationInvocationContext<?> cacheOperationInvocationContext = mockCacheNames(cacheNames);

        final Collection<String> resolvedNames = cacheResolver.getCacheNames(cacheOperationInvocationContext);

        assertThat(resolvedNames, hasSize(3));

        validateCacheNames(resolvedNames);
    }

    @Test(expected = IllegalStateException.class)
    public void badContextInformation() {
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);

        final CacheOperationInvocationContext<?> cacheOperationInvocationContext = mockCacheNames(Collections.singleton("anything"));
        cacheResolver.getCacheNames(cacheOperationInvocationContext);
    }

    private CacheOperationInvocationContext<?> mockCacheNames(final Set<String> cacheNames) {
        final BasicOperation basicOperation = mock(BasicOperation.class);
        when(basicOperation.getCacheNames()).thenReturn(cacheNames);

        @SuppressWarnings("unchecked")
        final CacheOperationInvocationContext<BasicOperation> cacheOperationInvocationContext = mock(CacheOperationInvocationContext.class);

        when(cacheOperationInvocationContext.getOperation()).thenReturn(basicOperation);
        return cacheOperationInvocationContext;
    }

}