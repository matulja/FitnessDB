package _GuttenBase_Examples.type_column;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Default uses same data type as source
 *
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class _CustomDefaultColumnTypeMapper implements _CustomColumnTypeMapper {

    private DatabaseType sourceDB;
    private DatabaseType targetDB;

    private final Map<DatabaseType, Map<DatabaseType, Map<String, String>>> _mappings = new HashMap<>();

    public _CustomDefaultColumnTypeMapper(DatabaseType sourceDatabase, DatabaseType targetDatabase) {

        sourceDB = sourceDatabase;
        targetDB = targetDatabase;

        createPostgresToMysqlMapping();
        createMysqlToPostresMapping();
        createMysqlToOracle();
        createOracleToMysql();
        createMSSQLToOracle();
        createH2ToDerbyMapping();
        createDerbyToH2Mapping();
    }

    @Override
    public String mapColumnType(final ColumnMetaData columnMetaData, DatabaseType sourceDatabase, DatabaseType targetDatabase) {
        final String columnType = columnMetaData.getColumnTypeName().toUpperCase();

        String targetColumnType = getMapping(sourceDB, targetDB, columnType);

        if (targetColumnType != null)
            return targetColumnType;
        else
            return getColumnType(columnMetaData);
    }

    private String getColumnType(final ColumnMetaData columnMetaData) {
        final String precision = createPrecisionClause(columnMetaData);
        final String defaultColumnType = columnMetaData.getColumnTypeName();

        return defaultColumnType + precision;
    }

    private String createPrecisionClause(final ColumnMetaData columnMetaData) {
        switch (columnMetaData.getColumnType()) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.VARBINARY:
                return "(" + columnMetaData.getPrecision() + ")";

            default:
                return "";
        }
    }


    public String getMapping(DatabaseType sourceDB, DatabaseType targetDB, String columnType) {

        final Map<DatabaseType, Map<String, String>> databaseMatrix = _mappings.get(sourceDB);

        if (databaseMatrix != null) {
            final Map<String, String> databaseMapping = databaseMatrix.get(targetDB);

            if (databaseMapping != null) {
                return databaseMapping.get(columnType);
            }
        }

        return null;
    }

    public final void addMapping(DatabaseType sourceDB, DatabaseType targetDB, String sourceTypeName, String targetTypeName) {

        Map<DatabaseType, Map<String, String>> databaseMatrix = _mappings.computeIfAbsent(sourceDB, k -> new HashMap<>());


        Map<String, String> mapping = databaseMatrix.computeIfAbsent(targetDB, k -> new HashMap<>());

        mapping.put(sourceTypeName, targetTypeName);
    }


    private void createPostgresToMysqlMapping() {

        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "ARRAY", "LONGTEXT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "BIGSERIAL", "BIGINT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "BOOLEAN", "TINYINT(1)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "BOX", "POLYGON");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "BYTEA", "LONGBLOB");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "CIDR", "VARCHAR(43)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "CIRCLE", "POLYGON");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "DOUBLE PRECISION", "DOUBLE");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "INET", "VARCHAR(43)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "INTERVAL", "TIME");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "JSON", "LONGTEXT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "LINE", "LINESTRING");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "LSEG", "LINESTRING");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "MACADDR", "VARCHAR(17)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "MONEY", "DECIMAL(19,2)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "NATIONAL CHARACTER VARYING", "VARCHAR");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "NATIONAL CHARACTER", "CHAR");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "NUMERIC", "DECIMAL");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "PATH", "LINESTRING");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "REAL", "FLOAT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "SERIAL", "INT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "SMALLSERIAL", "SMALLINT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "TEXT", "LONGTEXT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "TIMESTAMP", "DATETIME");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "TSQUERY", "LONGTEXT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "TSVECTOR", "LONGTEXT");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "TXID_SNAPSHOT", "VARCHART");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "UUID", "VARCHAR(36)");
        addMapping(DatabaseType.POSTGRESQL, DatabaseType.MYSQL, "XML", "LONGTEXT");

    }


    private void createH2ToDerbyMapping() {

        addMapping(DatabaseType.H2DB, DatabaseType.DERBY, "LONGTEXT", "CLOB");
        addMapping(DatabaseType.H2DB, DatabaseType.DERBY, "LONGBLOB", "BLOB");
    }

    private void createDerbyToH2Mapping() {

        addMapping(DatabaseType.DERBY, DatabaseType.H2DB, "SMALLINT", "INT");
        addMapping(DatabaseType.DERBY, DatabaseType.H2DB, "LONGTEXT", "CLOB");
        addMapping(DatabaseType.DERBY, DatabaseType.H2DB, "LONGBLOB", "BLOB");

    }

    private void createMysqlToPostresMapping() {

        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "BIGINT AUTO_INCREMENT", "BIGSERIAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "BIGINT UNSIGNED", "NUMERIC(20)");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "BINARY", "BYTEA");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "BLOB", "BYTEA");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "DATETIME", "TIMESTAMP");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "DOUBLE", "DOUBLE PRECISION");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "FLOAT", "REAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "INT UNSIGNED", "BIGINT");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "INTEGER AUTO_INCREMENT", "SERIAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "LONGBLOB", "BYTEA");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "LONGTEXT", "TEXT");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "MEDIUMINT", "INTEGER");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "MEDIUMINT UNSIGNED", "INTEGER");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "MEDIUMBLOB", "BYTEA");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "MEDIUMTEXT", "TEXT");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "SMALLINT AUTO_INCREMENT", "SMALLSERIAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "SMALLINT UNSIGNED", "INTEGER");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "TINYBLOB", "BYTEA");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "TINYINT", "SMALLINT");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "TINYINT AUTO_INCREMENT", "SMALLSERIAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "TINYINT UNSIGNED", "SMALLSERIAL");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "TINYTEXT", "TEXT");
        addMapping(DatabaseType.MYSQL, DatabaseType.POSTGRESQL, "VARBINARY", "BYTEA");

    }

    private void createMysqlToOracle() {

        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "BIT", "RAW");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "BIGINT", "NUMBER(19, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "DATETIME", "DATE");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "DECIMAL", "FLOAT (24)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "DOUBLE", "FLOAT (24)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "DOUBLE PRECISION", "FLOAT (24)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "ENUM", "VARCHAR2");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "INT", "NUMBER(10, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "INTEGER", "NUMBER(10, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "LONGBLOB", "BLOB");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "LONGTEXT", "CLOB");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "MEDIUMBLOB", "BLOB");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "MEDIUMINT", "NUMBER(7, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "MEDIUMTEXT", "CLOB");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "NUMERIC", "NUMBER");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "REAL", "FLOAT (24)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "SET", "VARCHAR2");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "SMALLINT", "NUMBER(5, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TEXT", "VARCHAR2");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TIME", "DATE");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TIMESTAMP", "DATE");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TINYBLOB", "RAW");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TINYINT", "NUMBER(3, 0)");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "TINYTEXT", "VARCHAR2");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "YEAR", "NUMBER");
        addMapping(DatabaseType.MYSQL, DatabaseType.ORACLE, "VARBINARY", "BYTEA");

    }

    private void createOracleToMysql() {

        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "RAW", "BIT");                  //TINYBLOB
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "NUMBER(19, 0)", "BIGINT");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "DATE", "DATETIME");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "FLOAT (24)", "DECIMAL");       // DOUBLE, DOUBLE PRECISION, REAL
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "VARCHAR2", "ENUM");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "NUMBER(10, 0)", "INT");        // INTEGER
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "BLOB", "LONGBLOB");            // MEDIUMBLOB
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "CLOB", "MEDIUMTEXT");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "NUMBER(7, 0)", "MEDIUMINT");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "NUMBER", "NUMERIC");           //YEAR
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "VARCHAR2", "SET");             //TEXT, TINYTEXT
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "NUMBER(5, 0)", "SMALLINT");
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "DATE", "TIME");                //TIMESTAMP
        addMapping(DatabaseType.ORACLE, DatabaseType.MYSQL, "BYTEA", "VARBINARY");

    }

    private void createMSSQLToOracle() {

        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "BIGINT", "NUMBER(19)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "BINARY", "RAW");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "BIT", "NUMBER(3)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "DATETIME", "DATE");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "DECIMAL", "NUMBER(p[,s])");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "FLOAT", "FLOAT(49)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "IMAGE", "LONG RAW");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "INTEGER", "NUMBER(10");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "MONEY", "NUMBER(19,4)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "NTEXT", "LONG");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "NVARCHAR", "NCHAR");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "NUMERIC", "NUMBER(p[,s])");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "REAL", "FLOAT(23)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "SMALL DATETIME", "DATE");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "SMALL MONEY", "NUMBER(10,4)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "SMALLINT", "NUMBER(5)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "TEXT", "LONG");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "TIMESTAMP", "RAW");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "TINYINT", "NUMBER(3)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "UNIQUEIDENTIFIER", "CHAR(36)");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "VARBINARY", "RAW");
        addMapping(DatabaseType.MSSQL, DatabaseType.ORACLE, "VARCHAR", "VARCHAR2");

    }

}
