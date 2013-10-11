package org.ops4j.pax.clapper.runtime.osgi.launcher;

import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.ops4j.pax.clapper.runtime.osgi.launcher.internal.Launcher;

public class Main
{
    private static Launcher launcher;

    public static void main( String[] args )
        throws Exception
    {
        launcher = new Launcher(getDeploymentPackages(), getPlatformBundles(), getServiceBundles() );
        launcher.start();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                if( launcher != null )
                {
                    try
                    {
                        launcher.stop();
                    } catch( Throwable e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        } ) );
    }

    private static List<String> getServiceBundles()
        throws IOException
    {
        return loadResourceURLs( "META-INF/pax/clapper/service.bundles" );
    }

    private static List<String> getPlatformBundles()
        throws IOException
    {
        return loadResourceURLs( "META-INF/pax/clapper/platform.bundles" );
    }

    private static List<String> getDeploymentPackages()
        throws IOException
    {
        return loadResourceURLs( "META-INF/clapper/links/deployment.packages" );
    }

    private static List<String> loadResourceURLs( String resourceName )
        throws IOException
    {
        List<String> result = new ArrayList<String>();
        Enumeration<URL> bundles = locateResources( resourceName );
        while( bundles.hasMoreElements())
        {
            List<String> urls = loadResourceURL( bundles.nextElement() );
            result.addAll( urls );
        }
        return result;
    }

    private static List<String> loadResourceURL( URL bundles )
        throws IOException
    {
        Closer closer = Closer.create();
        try
        {
            InputStreamReader in = closer.register( new InputStreamReader( bundles.openStream() ) );
            return CharStreams.readLines( in, new StringAggregator() );
        } finally
        {
            closer.close();
        }
    }

    private static Enumeration<URL> locateResources( String linkName )
        throws IOException
    {
        ClassLoader classLoader = Main.class.getClassLoader();
        return classLoader.getResources( linkName );
    }
}
