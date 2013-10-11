package org.ops4j.pax.clapper.runtime.osgi.bundle;

import com.google.common.io.Closer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.deploymentadmin.DeploymentAdmin;
import org.osgi.service.deploymentadmin.DeploymentException;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeploymentAdminTracker extends ServiceTracker<DeploymentAdmin, DeploymentAdmin>
{
    private static final Logger LOG = LoggerFactory.getLogger( DeploymentAdminTracker.class );

    private final List<String> deploymentPackages;

    public DeploymentAdminTracker( BundleContext context, List<String> deploymentPackages )
    {
        super( context, DeploymentAdmin.class, null );
        this.deploymentPackages = deploymentPackages;
    }

    @Override
    public DeploymentAdmin addingService( ServiceReference<DeploymentAdmin> reference )
    {
        DeploymentAdmin admin = super.addingService( reference );
        for( String url : deploymentPackages )
        {
            try
            {
                deployPackage( url, admin );
            }
            catch( Exception e )
            {
                LOG.error( "Unable to deploy: " + url, e );
            }
        }
        return admin;
    }

    private void deployPackage( String deploymentPackage, DeploymentAdmin admin )
        throws IOException, DeploymentException
    {
        Closer closer = Closer.create();
        try
        {
            URL url = new URL(deploymentPackage);
            InputStream in = closer.register( url.openStream() );
            admin.installDeploymentPackage( in );
        }
        finally
        {
            closer.close();
        }
    }
}
