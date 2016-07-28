package com.hp.autonomy.hod.caching;

import java.util.Collection;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class HodPerUserCacheResolverTest extends AbstractHodCacheResolverTest {
    @Override
    protected AbstractHodCacheResolver constructCacheResolver() {
        return new HodPerUserCacheResolver(new HodCacheNameResolverImpl());
    }

    @Override
    protected void validateCacheNames(final Collection<String> resolvedNames) {
        final String userId = userUuid.toString();
        assertThat(resolvedNames, hasItems(
                "DOMAIN:APPLICATION:" + userId + ":cacheName",
                "DOMAIN:APPLICATION:" + userId + ":fooCache",
                "DOMAIN:APPLICATION:" + userId + ":barCache"
        ));
    }
}
