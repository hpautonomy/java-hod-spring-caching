/*
 * Copyright 2014-2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.sso.HodAuthentication;
import com.hp.autonomy.hod.sso.HodAuthenticationPrincipal;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * CacheResolver that prefixes cache names with the HP Haven OnDemand domain and the HP Haven OnDemand application in
 * the current security context. This allows caching to be used across multiple domains without interference.
 * <p>
 * The domain and application will be joined with colons, so cache name must not contain the ":" character if
 * {@link HodCacheNameResolver#getOriginalName(String)} is to be used
 */
abstract class AbstractHodCacheResolver extends AbstractCacheResolver {
    @Override
    protected Collection<String> getCacheNames(final CacheOperationInvocationContext<?> context) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof HodAuthentication)) {
            throw new IllegalStateException("There is no HOD authentication token in the security context holder");
        }

        final HodAuthenticationPrincipal principal = ((HodAuthentication) authentication).getPrincipal();

        final Set<String> contextCacheNames = context.getOperation().getCacheNames();
        final Collection<String> resolvedCacheNames = new HashSet<>();

        for (final String cacheName : contextCacheNames) {
            resolvedCacheNames.add(resolveName(cacheName, principal));
        }

        return resolvedCacheNames;
    }

    /**
     * Resolve the cache for the given name, qualified by the HOD application.
     *
     * @param cacheName The original cache name
     * @param principal HoD authentication principal retrieved from context
     * @return The cache name qualified with the application
     */
    protected abstract String resolveName(final String cacheName, final HodAuthenticationPrincipal principal);
}
