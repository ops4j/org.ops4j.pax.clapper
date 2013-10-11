package org.ops4j.pax.clapper.runtime.osgi.bundle;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class Activator
    implements BundleActivator
{
    private DeploymentAdminTracker tracker;

    @Override
    public void start( BundleContext context )
        throws Exception
    {
        Collection<ServiceReference<Map>> cmdline = context.getServiceReferences( Map.class, createFilter() );
        @SuppressWarnings( "unchecked" )
        Map<String, List<String>> commandLine = context.getService( cmdline.iterator().next() );
        List<String> deploymentPackages = commandLine.get( "deployment.packages" );
        tracker = new DeploymentAdminTracker( context, deploymentPackages );
        tracker.open();
    }

    @Override
    public void stop( BundleContext context )
        throws Exception
    {
        tracker.close();
    }

    private String createFilter()
    {
        return "(&(objectClass=java.util.Map)(service.id=command.line))";
    }
}
