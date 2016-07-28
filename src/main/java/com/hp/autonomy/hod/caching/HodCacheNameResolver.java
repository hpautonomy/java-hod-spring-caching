package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceIdentifier;

@SuppressWarnings("WeakerAccess")
public interface HodCacheNameResolver {
    /**
     * Resolve the cache for the given name, qualified by the HoD application.
     *
     * @param cacheName      The original cache name
     * @param hodApplication The identifier for the application
     * @return The cache name qualified with the application
     */
    String resolvePerApplicationCacheName(final String cacheName, final ResourceIdentifier hodApplication);

    /**
     * Resolve the cache for the given name, qualified by the HoD application and the HoD user uuid.
     *
     * @param cacheName      The original cache name
     * @param hodApplication The identifier for the application
     * @param user           The user uuid
     * @return The cache name qualified with the application
     */
    String resolvePerUserCacheName(final String cacheName, final ResourceIdentifier hodApplication, final String user);

    /**
     * Given a resolved name as returned by this CacheResolver, returns the original cache name
     *
     * @param resolvedName The resolved name of the cache
     * @return The original name of the cache
     */
    String getOriginalName(final String resolvedName);
}
