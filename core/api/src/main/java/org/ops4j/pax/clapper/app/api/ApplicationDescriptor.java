package org.ops4j.pax.clapper.app.api;


public interface ApplicationDescriptor
{
    String getName();

    DeploymentPackageDescriptor getDeploymentPackage();

}
