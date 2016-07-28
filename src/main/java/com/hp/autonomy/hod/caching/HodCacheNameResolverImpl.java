package com.hp.autonomy.hod.caching;

import com.hp.autonomy.hod.client.api.resource.ResourceIdentifier;

import java.util.regex.Pattern;

class HodCacheNameResolverImpl implements HodCacheNameResolver {
    private static final String SEPARATOR = ":";
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("([\\\\:])");

    @Override
    public String resolvePerApplicationCacheName(final String cacheName, final ResourceIdentifier hodApplication) {
        final String applicationId = hodApplication.toString();
        return applicationId + SEPARATOR + cacheName;
    }

    @Override
    public String resolvePerUserCacheName(final String cacheName, final ResourceIdentifier hodApplication, final String user) {
        final String applicationId = hodApplication.toString();
        return applicationId + SEPARATOR + escapeComponent(user) + SEPARATOR + cacheName;
    }

    @Override
    public String getOriginalName(final String resolvedName) {
        final String[] cacheNameComponents = resolvedName.split(SEPARATOR);
        return cacheNameComponents[cacheNameComponents.length - 1];
    }

    // HOD resource names must have : escaped to \: and \ escaped to \\
    private static String escapeComponent(final CharSequence input) {
        return ESCAPE_PATTERN.matcher(input).replaceAll("\\\\$1");
    }
}
