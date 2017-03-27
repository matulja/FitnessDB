package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;

/**
 *
 * add some "_tab" to table name
 *
 * Created by mfehler on 27.03.17.
 */
public class MappingTableName implements TableNameMapper, TableMapper{
    @Override
    public String mapTableName(final TableMetaData sourceTableMetaData) throws SQLException {

        final String newTableName= "_tab" + sourceTableMetaData.getTableName();
        return newTableName;
    }

    @Override
    public TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

        final String mappedTableName= mapTableName(source);
        return targetDatabaseMetaData.getTableMetaData(mappedTableName);
    }

}


