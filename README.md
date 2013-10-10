Pax Clapper - Managed Deployment system for Cloud providers.
==============

Pax Clapper is a Deployment Management system, with ambitions to be for OSGi applications what Google AppEngine
is for web applications.

## Abstraction
Pax Clapper operates with a few abstractions, and concrete implementations of these abstractions must be developed,
either for public cloud providers or for in-house cloud, virtualization or other deployment architectures.

    * Container - Abstraction of the _clean_ host.
    * Runtime - The process(es) that is started in the Container.
    * Client - User Interfaces to the running Clapper plant.
    * Cell - A logical partition of a data center, containing hosts and networks.
    * Stage - A step in a progress of Application Lifecycle Management. dev -> qa -> uat -> prod
    * InstancePool - A set of resources spanning all Stages and containing one Application.

## Containers
The Container abstraction tries to encompass the vital parts of what is needed to run an application. The Container
abstraction is simplified from the usage point of view, to ensure that a wide range of Container types can be supported.

The initial *Bare* container is simply a naked Linux host, without virtualization capabilities at all. It effectively
works as a permanent "image" that is always available, where start/stop of the image has no effect.

An EC2 container is the natural next step to implement, possibly followed with the Google Compute and other offereings.

## Runtimes
Runtimes represents the process or processes that are started inside the Container.

Initially, we would like to support both a _scripted_ and a _OSGi_ runtime, with the latter being capable of providing
custom bundles for application-level services.

### Services Runtime
The Services Runtime will start an OSGi framework and load configured _Service Bundles_, which extend the basic
Clapper platform with environment/firm specific extensions available to the applications.

Applications are packaged as Deployment Packages, defined by the OSGi Deployment Admin Specification 114.

### Scripted Runtime
The Scripted Runtime simply deploys a script on to the Container. The script must work with all resources only being
reachable be network, not on the local file system.

## Server
The Clapper server is a Clapper application managing itself.

## Clients

### CLI - Command Line Interface

### Web

