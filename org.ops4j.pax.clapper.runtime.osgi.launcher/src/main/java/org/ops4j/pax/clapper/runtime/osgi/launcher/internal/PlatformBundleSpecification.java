package org.ops4j.pax.clapper.runtime.osgi.launcher.internal;

import java.net.URL;

public class PlatformBundleSpecification
    implements Specification<URL>
{

    @Override
    public boolean isSatisfiedBy( URL item )
    {
        String path = item.getPath();
        for( String pattern : PATTERNS )
        {
            if( path.contains(pattern))
                return true;
        }
        return false;
    }

    private static final String[] PATTERNS = {
        "pax-url", "ops4j-base", "swissbox", "pax-logging"
    };
}
