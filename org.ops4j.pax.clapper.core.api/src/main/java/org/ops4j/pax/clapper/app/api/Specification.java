package org.ops4j.pax.clapper.app.api;

public interface Specification<T>
{
    boolean isSatisfiedBy( T item );

    public final class All<T>
        implements Specification<T>
    {

        @Override
        public boolean isSatisfiedBy( T item )
        {
            return true;
        }
    }

    public final class Single<T>
        implements Specification<T>
    {

        private final T specified;

        public Single(T specified)
        {
            this.specified = specified;
        }

        @Override
        public boolean isSatisfiedBy( T item )
        {
            return true;
        }
    }

    public final class AnyOf<T>
        implements Specification<T>
    {

        private final Iterable<T> specified;

        public AnyOf(Iterable<T> specified)
        {
            this.specified = specified;
        }

        @Override
        public boolean isSatisfiedBy( T item )
        {
            for( T any : specified )
            {
                if( item.equals( any ))
                    return true;
            }
            return false;
        }
    }
}
