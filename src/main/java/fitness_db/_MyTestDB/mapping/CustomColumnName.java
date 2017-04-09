package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;


/**
 * look at columnName starts with "abo" and add to columnName "_id"
 *
 * Created by Marynasuprun on 26.03.2017.
 *
 */

@SuppressWarnings("Duplicates")
public class CustomColumnName implements ColumnNameMapper{


    @Override
    public String mapColumnName(ColumnMetaData columnMetaData) throws SQLException {

        final String defaultColumnName = columnMetaData.getColumnName();
        String columnName;

        switch (defaultColumnName)
        {
            case "ID":
                columnName = defaultColumnName.replace("ID", "NO_ID");
                break;
            case "NAME":
                columnName = defaultColumnName.replace("NAME", "NO_NAME");
                break;
            default:
                columnName = defaultColumnName;
                break;
        }

        return columnName;


    }

    /*public List<String> createColumns()
    {
        final List<TableMetaData> tableSourceMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, _sourceConnectorId);

        for (final TableMetaData sourceTableMetaData : tableSourceMetaDatas)

        {
            final List<ColumnMetaData> sourceColumns = sourceTableMetaData.getColumnMetaData();
            final Map<String, String> map = new HashMap<>();

            for (final ColumnMetaData sourceColumnMetaData : sourceColumns)
            {
                final int columnCount =sourceTableMetaData.getColumnCount();
                // System.out.println("Column Name " + sourceColumnMetaData.getColumnName());

                for (int i = 1; i <= columnCount; i++) {
                    final String columnName = columnMetaData.getColumnName();
                    map.put(columnName, "New Name");
                }
            }

        }
        return tableSourceMetaDatas;

    }*/
}


