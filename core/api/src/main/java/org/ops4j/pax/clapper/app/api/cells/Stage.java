package org.ops4j.pax.clapper.app.api.cells;

import org.ops4j.pax.clapper.app.api.ApplicationDescriptor;

public interface Stage
{
    void startApplication( ApplicationDescriptor descriptor );

    void stopApplication( ApplicationDescriptor descriptor );

}
