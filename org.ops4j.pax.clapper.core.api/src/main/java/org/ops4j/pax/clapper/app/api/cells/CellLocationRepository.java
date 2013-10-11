package org.ops4j.pax.clapper.app.api.cells;

import org.ops4j.pax.clapper.app.api.Specification;

public interface CellLocationRepository
{
    Iterable<Cell> findCells( Specification<CellLocation> filter );

}
