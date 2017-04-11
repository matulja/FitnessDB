package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeResolverListHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolverList;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.repository.impl.ClassNameColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.repository.impl.HeuristicColumnTypeResolver;

import java.sql.Types;
import java.util.Arrays;

/**
 * Created by mfehler on 10.04.17.
 */
public class CustomColumnType implements ColumnTypeMapper {

    @Override
    public String getColumnType(ColumnMetaData columnMetaData) {
        switch (columnMetaData.getColumnTypeName().toUpperCase()) {
            case "MEDIUMTEXT":
                return "VARCHAR(1024)";
            case "MEDIUMBLOB":
                return "BYTEA";
            default:
                return columnMetaData.getColumnTypeName();
        }
    }

}

