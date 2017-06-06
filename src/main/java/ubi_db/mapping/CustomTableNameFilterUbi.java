package ubi_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 *
 * <p>
 * Created by mfehler on 26.05.17.
 */
public class CustomTableNameFilterUbi extends RepositoryTableFilterHint {


    @Override
    public RepositoryTableFilter getValue() {
        return table -> {
            final String tableCase = table.getTableName().toLowerCase();
            return (tableCase.startsWith("b")||(tableCase.startsWith("d"))||
                    tableCase.startsWith("e")||(tableCase.startsWith("f"))||(tableCase.startsWith("g"))||
                    tableCase.startsWith("h")||(tableCase.startsWith("k"))||(tableCase.startsWith("l")) ||
                    tableCase.startsWith("m")||(tableCase.startsWith("n"))||(tableCase.startsWith("p"))||
                    tableCase.startsWith("s")||(tableCase.startsWith("t"))||(tableCase.startsWith("a")||
                    tableCase.startsWith("r")|| tableCase.startsWith("w")));
        };
    }
}
