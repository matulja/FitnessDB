package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * comparate Columns by ColumnsName
 *
 *
 * Created by mfehler on 27.03.17.
 */
public class MappingColumnOrder implements ConnectorHint<ColumnOrderComparatorFactory> {

    @Override
    public Class<ColumnOrderComparatorFactory> getConnectorHintType() {
        return ColumnOrderComparatorFactory.class;
    }

    @Override
    public ColumnOrderComparatorFactory getValue() {
        return null;
    }

    public static List<ColumnMetaData> getSortedColumns(final ConnectorRepository connectorRepository, final String connectorId,
                                                        final TableMetaData tableMetaData) {

        final Comparator<ColumnMetaData> sourceColumnComparator = connectorRepository

        .getConnectorHint(connectorId, ColumnOrderComparatorFactory.class).getValue().createComparator().
                        thenComparing(ColumnMetaData::getColumnName);

        final List<ColumnMetaData> columns = new ArrayList<>(tableMetaData.getColumnMetaData());
        columns.sort(sourceColumnComparator);
        return columns;
    }


}
