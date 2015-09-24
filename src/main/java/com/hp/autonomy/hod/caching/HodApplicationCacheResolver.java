/*
 * Copyright 2014-2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceIdentifier;
import com.hp.autonomy.hod.sso.HodAuthentication;
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
 *
 * The domain and application will be joined with colons, so cache name must not contain the ":" character if
 * {@link #getOriginalName(String)} is to be used
 */
public class HodApplicationCacheResolver extends AbstractCacheResolver {
    private static final String SEPARATOR = ":";

    @Override
    protected Collection<String> getCacheNames(final CacheOperationInvocationContext<?> context) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof HodAuthentication)) {
            throw new IllegalStateException("There is no HOD authentication token in the security context holder");
        }

        final HodAuthentication hodAuthentication = (HodAuthentication) authentication;
        final String applicationId = new ResourceIdentifier(hodAuthentication.getDomain(), hodAuthentication.getApplication()).toString();

        final Set<String> contextCacheNames = context.getOperation().getCacheNames();
        final Set<String> resolvedCacheNames = new HashSet<>();

        for (final String cacheName : contextCacheNames) {
            resolvedCacheNames.add(applicationId + SEPARATOR + cacheName);
        }

        return resolvedCacheNames;
    }

    /**
     * Given a resolved name as returned by this CacheResolver, returns the original cache name
     * @param resolvedName The resolved name of the cache as returned by {@link #getCacheNames(CacheOperationInvocationContext)}
     * @return The original name of the cache
     */
    public static String getOriginalName(final String resolvedName) {
        final String[] cacheNameComponents = resolvedName.split(SEPARATOR);
        return cacheNameComponents[cacheNameComponents.length - 1];
    }
}
