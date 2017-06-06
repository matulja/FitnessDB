package _GuttenBase_Examples.mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;


/**
 * look at table columns starts with some value ""
 * Created by mfehler on 27.03.17.
 */

public final class CustomColumnNameFilter extends RepositoryColumnFilterHint{


    @Override
    public RepositoryColumnFilter getValue() {
        return column -> {

            final String columnCase = column.getColumnName();
            return ((columnCase.startsWith("e"))||(columnCase.startsWith("c"))||(columnCase.startsWith("o"))||(columnCase.startsWith("sa"))
             ||(columnCase.startsWith("r")) |(columnCase.startsWith("h")||(columnCase.startsWith("i"))
             ||(columnCase.startsWith("p"))));
        };
    }
}
