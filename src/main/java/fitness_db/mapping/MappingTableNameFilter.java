package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

import java.sql.SQLException;

/**
 * look at table name starts with "k"
 *
 * Created by mfehler on 27.03.17.
 */
public final class MappingTableNameFilter extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return new RepositoryTableFilter() {
            @Override
            public boolean accept(TableMetaData table) throws SQLException {

                final String kuCase=table.getTableName();
                return kuCase.startsWith("k");
            }
        };
    }
}
