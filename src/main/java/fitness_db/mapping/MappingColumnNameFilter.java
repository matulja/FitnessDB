package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * look at table columns starts with "k" or "h"
 *
 * Created by mfehler on 27.03.17.
 */

public final class MappingColumnNameFilter extends RepositoryColumnFilterHint{


    @Override
    public RepositoryColumnFilter getValue() {
        return column -> {

            final String columnCase = column.getColumnName();
            return (columnCase.startsWith("k")||(columnCase.startsWith("h")));
        };
    }
}
