package GuttenBase_Examples._copyDatabeses._Mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * Created by mfehler on 10.05.17.
 */
public class CustomColumnNameFilterShop extends RepositoryColumnFilterHint {


    @Override
    public RepositoryColumnFilter getValue() {
        return column -> {

            final String columnCase = column.getColumnName().toLowerCase();
            return ((columnCase.startsWith("e"))||(columnCase.startsWith("c"))||(columnCase.startsWith("o"))||(columnCase.startsWith("sa"))
                    ||(columnCase.startsWith("r")) |(columnCase.startsWith("h")||(columnCase.startsWith("i"))
                    ||(columnCase.startsWith("p"))));
        };
    }
}
