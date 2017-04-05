package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;


/**
 * look at columnName starts with "abo" and add to columnName "_id"
 *
 * Created by Marynasuprun on 26.03.2017.
 *
 */

public class MappingColumnName implements ColumnNameMapper{


    @Override
    public String mapColumnName(ColumnMetaData columnMetaData) throws SQLException {
        final String columnName = columnMetaData.getColumnName();
        return columnName + "id";
    }


}


