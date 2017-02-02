package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceName;
import com.hp.autonomy.hod.sso.HodAuthenticationPrincipal;

/**
 * Cache resolver for methods which should be cached per tenant
 */
class HodPerApplicationCacheResolver extends AbstractHodCacheResolver {
    private final HodCacheNameResolver hodCacheNameResolver;

    HodPerApplicationCacheResolver(final HodCacheNameResolver hodCacheNameResolver) {
        this.hodCacheNameResolver = hodCacheNameResolver;
    }

    /**
     * Resolve the cache for the given name, qualified by the HOD application.
     *
     * @param cacheName The original cache name
     * @param principal HoD authentication principal retrieved from context
     * @return The cache name qualified with the application
     */
    @Override
    protected String resolveName(final String cacheName, final HodAuthenticationPrincipal principal) {
        final ResourceName hodApplication = principal.getApplication();
        return hodCacheNameResolver.resolvePerApplicationCacheName(cacheName, hodApplication);
    }
}
