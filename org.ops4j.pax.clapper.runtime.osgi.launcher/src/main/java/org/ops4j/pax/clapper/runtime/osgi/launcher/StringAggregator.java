package org.ops4j.pax.clapper.runtime.osgi.launcher;

import com.google.common.io.LineProcessor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class StringAggregator
    implements LineProcessor<List<String>>
{
    List<String> urls = new ArrayList<String>( );

    @Override
    public boolean processLine( String line )
        throws IOException
    {
        line = line.trim();
        if( line.length() == 0 )
            return true;
        if( line.startsWith( "#" ))
            return true;
        if( line.startsWith( "//" ))
            return true;
        urls.add( line );
        return true;
    }

    @Override
    public List<String> getResult()
    {
        return urls;
    }
}
