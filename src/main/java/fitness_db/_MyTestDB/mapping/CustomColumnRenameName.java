package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * look at columnName starts with some value and replace them
 *
 * Created by Marynasuprun on 26.03.2017.
 *
 */

public class CustomColumnRenameName implements ColumnNameMapper{

    private final Map<String, String> replacementsColumns = new HashMap<>();

    @Override
    public String mapColumnName(ColumnMetaData columnMetaData) throws SQLException {

        final String defaultColumnName = columnMetaData.getColumnName();
        final String columnName = replacementsColumns.get(defaultColumnName);

        if(columnName == null)
            return defaultColumnName;
        else
            return columnName;
    }

    public CustomColumnRenameName addReplacement(final String sourceComn, final String targetColumn){
        replacementsColumns.put(sourceComn, targetColumn);

        replacementsColumns.put("officecode", "id_officecode");
        replacementsColumns.put("city", "id_city");
        return  this;
    }
}


