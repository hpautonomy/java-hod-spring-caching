package com.hp.autonomy.hod.caching;

import java.util.Collection;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class HodPerApplicationCacheResolverTest extends AbstractHodCacheResolverTest {
    @Override
    protected AbstractHodCacheResolver constructCacheResolver() {
        return new HodPerApplicationCacheResolver(new HodCacheNameResolverImpl());
    }

    @Override
    protected void validateCacheNames(final Collection<String> resolvedNames) {
        assertThat(resolvedNames, hasItems(
                "DOMAIN:APPLICATION:cacheName",
                "DOMAIN:APPLICATION:fooCache",
                "DOMAIN:APPLICATION:barCache"
        ));
    }
}
