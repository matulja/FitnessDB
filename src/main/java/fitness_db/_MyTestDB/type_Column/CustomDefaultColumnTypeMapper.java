package fitness_db._MyTestDB.type_Column;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static de.akquinet.jbosscc.guttenbase.connector.DatabaseType.MYSQL;
import static de.akquinet.jbosscc.guttenbase.connector.DatabaseType.POSTGRESQL;

/**
 * Default uses same data type as source
 * 
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class CustomDefaultColumnTypeMapper implements CustomColumnTypeMapper {


  @Override
  public String getColumnType(final ColumnMetaData columnMetaData, final DatabaseType sourceDatabase, final DatabaseType targetDatabase) {
    final String precision = createPrecisionClause(columnMetaData);
    final String columnType = columnMetaData.getColumnTypeName();

    if (sourceDatabase == MYSQL && targetDatabase == POSTGRESQL) {

      switch (columnType.toUpperCase()) {
        case "MEDIUMTEXT":
          return "VARCHAR(1024)";
        case "MEDIUMBLOB":
          return "BYTEA";
        case "TINYINT":
          return "INT";
        case "LONGTEXT":
          return "TEXT";
        case "VARBINARY":
          return "BYTEA";
        default:
          return columnType;
      }

    } else if (sourceDatabase == POSTGRESQL && targetDatabase == MYSQL) {


    } else {
      System.out.println("No DATABASE mathces");
    }

    return columnType + precision;


  }

  private String createPrecisionClause(final ColumnMetaData columnMetaData)
  {
    switch (columnMetaData.getColumnType())
    {
    case Types.CHAR:
    case Types.VARCHAR:
    case Types.VARBINARY:

      return "(" + columnMetaData.getPrecision() + ")";

    default:
      return "";
    }
  }


}
