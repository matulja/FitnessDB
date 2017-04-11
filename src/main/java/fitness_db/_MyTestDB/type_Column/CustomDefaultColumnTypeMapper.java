package fitness_db._MyTestDB.type_Column;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static de.akquinet.jbosscc.guttenbase.connector.DatabaseType.MYSQL;
import static de.akquinet.jbosscc.guttenbase.connector.DatabaseType.ORACLE;
import static de.akquinet.jbosscc.guttenbase.connector.DatabaseType.POSTGRESQL;

/**
 * Default uses same data type as source
 * 
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class CustomDefaultColumnTypeMapper implements CustomColumnTypeMapper {



  @Override
  public String mapColumnType(final ColumnMetaData columnMetaData, final DatabaseType sourceDatabase, final DatabaseType targetDatabase) {

    final String precision = createPrecisionClause(columnMetaData);
    String columnType = columnMetaData.getColumnTypeName();

    if (sourceDatabase == MYSQL && targetDatabase == POSTGRESQL) {

      columnType = createPostgreMapping(columnMetaData);
      System.out.println("Postgres Database");

    } else if (sourceDatabase == MYSQL && targetDatabase == ORACLE) {

      System.out.println("Oracle Database");

      columnType = createOracleMapping(columnMetaData);

    } else {

      System.out.println("No DATABASE matches");

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

  private String createPostgreMapping (final ColumnMetaData columnMetaData)
  {
    final String columnType = columnMetaData.getColumnTypeName();
    switch (columnType.toUpperCase()) {

      case "BIGINT":
        return "BIGINT";
      case "BIGINT AUTO_INCREMENT":
        return "BIGSERIAL";
      case "BIGINT UNSIGNED":
        return "NUMERIC(20)";
      case "BIT":
        return "BIT";
      case "BINARY":
        return "BYTEA";
      case "BLOB":
        return "BYTEA";
      case "CHAR":
        return "CHAR";
      case "DATE":
        return "DATE";
      case "DATETIME":
        return "TIMESTAMP";
      case "DECIMAL":
        return "DECIMAL";
      case "DOUBLE":
        return "DOUBLE PRECISION";
      case "FLOAT":
        return "REAL";
      case "INT UNSIGNED":
        return "BIGINT";
      case "INTEGER AUTO_INCREMENT":
        return "SERIAL";
      case "LONGBLOB":
        return "BYTEA";
      case "LONGTEXT":
        return "TEXT";
      case "MEDIUMINT":
        return "INTEGER";
      case "MEDIUMINT UNSIGNED":
        return "INTEGER";
      case "MEDIUMBLOB":
        return "BYTEA";
      case "MEDIUMTEXT":
        return "TEXT";
      case "NUMERIC":
        return "NUMERIC";
      case "SMALLINT AUTO_INCREMENT":
        return "SMALLSERIAL";
      case "SMALLINT UNSIGNED":
        return "INTEGER";
      case "TEXT":
        return "TEXT";
      case "TIME":
        return "TIME";
      case "TIMESTAMP DEFAULT NOW()":
        return "TIMESTAMP DEFAULT NOW()";
      case "TINYBLOB":
        return "BYTEA";
      case "TINYINT":
        return "SMALLINT";
      case "TINYINT AUTO_INCREMENT":
        return "SMALLSERIAL";
      case "TINYINT UNSIGNED":
        return "SMALLINT";
      case "TINYTEXT":
        return "TEXT";
      case "VARBINARY":
        return "BYTEA";
      case "VARCHAR":
        return "VARCHAR";

      default:
        return columnType;
    }


  }

  private String createOracleMapping (final ColumnMetaData columnMetaData)
  {
    final String columnType = columnMetaData.getColumnTypeName();
    switch (columnType.toUpperCase()) {

      case "BIGINT":
        return "NUMBER(19,0)";
      case "BIT":
        return "RAW";
      case "BLOB":
        return "BLOB";  //RAW
      case "CHAR":
        return "CHAR";
      case "DATE":
        return "DATE";
      case "DATETIME":
        return "DATE";
      case "DECIMAL":
        return "FLOAT(24)";
      case "DOUBLE":
        return "FLOAT(24)";
      case "DOUBLE PRECISION":
        return "FLOAT(24)";
      case "ENUM":
        return "VARCHAR2";
      case "FLOAT":
        return "FLOAT";
      case "INT":
        return "NUMBER(10,0)";
      case "INTEGER":
        return "NUMBER(10,0)";
      case "LONGBLOB":
        return "BLOB";   //RAW
      case "LONGTEXT":
        return "CLOB";   //RAW
      case "MEDIUMBLOB":
        return "BLOB";  //RAW
      case "MEDIUMINT":
        return "NUMBER(7,0)";
      case "MEDIUMTEXT":
        return "CLOB";  //RAW
      case "NUMERIC":
        return "NUMBER";
      case "REAL":
        return "FLOAT(24)";
      case "SET":
        return "VARCHAR2";
      case "SMALLINT":
        return "NUMBER(5,0)";
      case "TEXT":
        return "VARCHAR2";  //CLOB
      case "TIME":
        return "DATE";
      case "TIMESTAMP":
        return "DATE";
      case "TINYBLOB":
        return "RAW";  //BLOB
      case "TINYINT":
        return "NUMBER(3,0)";
      case "TINYTEXT":
        return "VARCHAR2";
      case "VARCHAR":
        return "VARCHAR2";
      case "YEAR":
        return "NUMBER";
      case "VARBINARY":
        return "BYTEA";

      default:
        return columnType;
    }


  }



}
