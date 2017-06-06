package _GuttenBase_Examples._copyDatabeses._Mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Created by mfehler on 10.05.17.
 */
public class CustomTableNameFilterShop extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return table -> {
            final String tableCase = table.getTableName().toLowerCase();
            return tableCase.startsWith("tab")||(tableCase.startsWith("p"))||(tableCase.startsWith("o"))||(tableCase.startsWith("c"))
                    ||(tableCase.startsWith("foo"))||(tableCase.startsWith("e"));
        };
    }
}