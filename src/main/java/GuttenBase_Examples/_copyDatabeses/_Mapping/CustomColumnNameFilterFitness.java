package GuttenBase_Examples._copyDatabeses._Mapping;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * Created by mfehler on 10.05.17.
 */
public class CustomColumnNameFilterFitness extends RepositoryColumnFilterHint {


    @Override
    public RepositoryColumnFilter getValue() {
        return column -> {

            final String columnCase = column.getColumnName().toLowerCase();
            return ((columnCase.startsWith("k"))||(columnCase.startsWith("id"))||(columnCase.startsWith("h"))||(columnCase.startsWith("st"))
                    ||(columnCase.startsWith("v")) |(columnCase.startsWith("n")||(columnCase.startsWith("d"))
                    ||(columnCase.startsWith("o"))));
        };
    }
}
