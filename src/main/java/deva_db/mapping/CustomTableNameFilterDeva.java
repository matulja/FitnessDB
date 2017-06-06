package deva_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * look at table name starts with "k"
 * <p>
 * Created by mfehler on 27.03.17.
 */
public class CustomTableNameFilterDeva extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return table -> {
            final String tableCase = table.getTableName().toLowerCase();
            return tableCase.startsWith("deva")||(tableCase.startsWith("jbpm"))||(tableCase.startsWith("abc"));
        };
    }
}
