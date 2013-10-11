package org.ops4j.pax.clapper.runtime.osgi.launcher.internal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.nat.internal.NativeTestContainer;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.spi.PaxExamRuntime;
import org.osgi.framework.Bundle;

public class Launcher
{
    private final List<String> deploymentPackages;
    private final List<String> platformBundles;
    private final List<String> serviceBundles;
    private TestContainer container;

    public Launcher( List<String> deploymentPackages, List<String> platformBundles, List<String> serviceBundles )
    {
        this.deploymentPackages = deploymentPackages;
        this.platformBundles = platformBundles;
        this.serviceBundles = serviceBundles;
    }

    public void start()
        throws IOException
    {

        DefaultCompositeOption options = new DefaultCompositeOption();
        options.add( vmOptions() );
        options.add( frameworkOptions() );
        options.add( platformBundles() );
//        options.add( serviceBundles() );

        ExamSystem system = PaxExamRuntime.createServerSystem( options );
        container = PaxExamRuntime.createContainer( system );
        container.start();
        registerCommandLineBridge();
    }

    public void stop()
    {
        container.stop();
    }

    private Option serviceBundles()
    {
        return createBundleOptions( serviceBundles, 5 );
    }

    private Option platformBundles()
    {
        PlatformBundleSpecification specification = new PlatformBundleSpecification();
        DefaultCompositeOption options = new DefaultCompositeOption(  );
        URLClassLoader ucl = (URLClassLoader) getClass().getClassLoader();
        URL[] urls = ucl.getURLs();
        for( URL url : urls )
        {
            if( specification.isSatisfiedBy( url ) )
                options.add( CoreOptions.bundle( url.toExternalForm() ).startLevel( 1 ));
        }
        return options.add( createBundleOptions( platformBundles, 3 ) );
    }

    private Option createBundleOptions( List<String> bundles, int startLevel )
    {
        DefaultCompositeOption options = new DefaultCompositeOption();
        for( String bundle : bundles )
        {
            options.add( CoreOptions.bundle( bundle ).startLevel( startLevel ) );
        }
        return options;
    }

    private Option frameworkOptions()
    {
        return CoreOptions.frameworkStartLevel( 5 );
    }

    private Option vmOptions()
    {
        return CoreOptions.vmOptions(
            "-Xmx1000MB"
        );
    }

    private void registerCommandLineBridge()
    {
        Bundle frameworkBundle = getFrameworkBundle();
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put("service.id","command.line");

        Map<String,List<String>> commandline = new HashMap<String,List<String>>();
        commandline.put("deployment.packages", deploymentPackages );

        frameworkBundle.getBundleContext().registerService( Map.class, commandline, properties );
    }

    private Bundle getFrameworkBundle()
    {
        try
        {
            Field field = NativeTestContainer.class.getDeclaredField( "framework" );
            field.setAccessible( true );
            return (Bundle) field.get( container );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            throw new InternalError( "Pax Exam have changed. This version of Pax Clapper is not compatible." );
        }
    }
}
