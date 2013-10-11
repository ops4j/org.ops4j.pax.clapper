package org.ops4j.pax.clapper.app.api;

/** This interface represents an description of a Deployment Package in the Deployment Admin Specification in OSGi.
 * It is a descriptor of how to assemble a DeploymentPackage.
 */
public interface DeploymentPackageDescriptor
{
    /**
     *
     * @return the bundles that will be part of the DeploymentPackage.
     */
    Iterable<BundleDescriptor> getBundles();

    /**
     *
     * @return the resources that will be part of the DeploymentPackage.
     */
    Iterable<ResourceDescriptor> getResources();

    /**
     *
     * @return the manifest for the DeploymentPackage.
     */
    ManifestDescriptor getManifest();

    /**
     *
     * @return the signatures that will be part of the DeploymentPackage.
     */
    Iterable<SignatureDescriptor> getSignatures();
}
