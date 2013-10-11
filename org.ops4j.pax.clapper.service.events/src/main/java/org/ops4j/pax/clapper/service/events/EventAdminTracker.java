package org.ops4j.pax.clapper.service.events;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class EventAdminTracker extends ServiceTracker<EventAdmin, EventAdmin>
    implements EventAdmin
{
    private volatile EventAdmin eventBus;
    private ConcurrentLinkedQueue<Event> buffer;

    public EventAdminTracker( BundleContext context )
    {
        super( context, EventAdmin.class, null );
        buffer = new ConcurrentLinkedQueue<Event>();
    }

    @Override
    public EventAdmin addingService( ServiceReference<EventAdmin> reference )
    {
        eventBus = super.addingService( reference );
        return eventBus;
    }

    @Override
    public void removedService( ServiceReference<EventAdmin> reference, EventAdmin service )
    {
        if( service.equals( eventBus ) )
        {
            eventBus = null;
        }
        super.removedService( reference, service );
    }

    @Override
    public void postEvent( Event event )
    {
        buffer.offer( event );
        emptyBuffer();
    }

    @Override
    public void sendEvent( Event event )
    {
        buffer.offer( event );
        emptyBuffer();
    }

    private void emptyBuffer()
    {
        while( eventBus != null && buffer.size() > 0 )
        {
            Event event = buffer.poll();
            if( event == null )
            {
                break;
            }
            EventAdmin delegate = eventBus;
            if( delegate == null )
            {
                break;
            }
            delegate.postEvent( event );
        }
    }
}
