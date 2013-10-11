package org.ops4j.pax.clapper.runtime.osgi.launcher.internal;

public interface Specification<T>
{
    boolean isSatisfiedBy( T item );
}
