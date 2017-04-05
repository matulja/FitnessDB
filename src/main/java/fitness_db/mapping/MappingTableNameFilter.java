package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * look at table name starts with "k"
 *
 * Created by mfehler on 27.03.17.
 */
public class MappingTableNameFilter extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return table -> {

            final String kuCase=table.getTableName();
            return kuCase.startsWith("k");
        };
    }
}
