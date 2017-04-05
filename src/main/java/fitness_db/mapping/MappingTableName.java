package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;

/**
 *
 * look at table starts with "tab_"
 *
 * Created by mfehler on 27.03.17.
 */
public class MappingTableName implements TableNameMapper, TableMapper{
    @Override
    public String mapTableName(final TableMetaData sourceTableMetaData) throws SQLException {

        return "tab_" + sourceTableMetaData.getTableName();
    }

    @Override
    public TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

        final String mappedTableName= mapTableName(source);
        return targetDatabaseMetaData.getTableMetaData(mappedTableName);
    }

}


