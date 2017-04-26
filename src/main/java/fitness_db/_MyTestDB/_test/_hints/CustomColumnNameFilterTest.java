package fitness_db._MyTestDB._test._hints;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * Created by mfehler on 26.04.17.
 */
public class CustomColumnNameFilterTest extends RepositoryColumnFilterHint {


    @Override
    public RepositoryColumnFilter getValue() {
        return column -> {

            final String columnCase = column.getColumnName();
            return ((columnCase.startsWith("E"))||(columnCase.startsWith("C"))||(columnCase.startsWith("O"))||(columnCase.startsWith("SA"))
                    ||(columnCase.startsWith("R")) |(columnCase.startsWith("H")||(columnCase.startsWith("I"))
                    ||(columnCase.startsWith("P"))));
        };
    }
}