package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

import java.sql.SQLException;

/**
 * Created by mfehler on 27.03.17.
 */

public final class MappingColumnNameFilter extends RepositoryColumnFilterHint{


    @Override
    public RepositoryColumnFilter getValue() {
        return new RepositoryColumnFilter() {
            @Override
            public boolean accept(ColumnMetaData column) throws SQLException {
                final String columnCase = column.getColumnName();
                return columnCase.startsWith("_id") || columnCase.endsWith("_nr");
            }

        };
    }
}
