package _GuttenBase_Examples._copyDatabeses._Mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Created by mfehler on 10.05.17.
 */
public class CustomTableNameFilterFitness extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return table -> {
            final String tableCase = table.getTableName().toLowerCase();
            return tableCase.startsWith("tab")||(tableCase.startsWith("f"))||(tableCase.startsWith("h"))||(tableCase.startsWith("k"))
                    ||(tableCase.startsWith("s"));
        };
    }
}