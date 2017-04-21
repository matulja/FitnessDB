package fitness_db._MyTestDB.schema.type_column;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnTypeMapper;
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

    private DatabaseType sourceDB;
    private DatabaseType targetDB;

    private final Map<DatabaseType, Map<DatabaseType, Map<String, String>>> _mappings = new HashMap<>();

    public CustomDefaultColumnTypeMapper(DatabaseType sourceDatabase, DatabaseType targetDatabase) {

        sourceDB=sourceDatabase;
        targetDB=targetDatabase;
        createPostgresToMysqlMapping();
        createMysqlToPostresMapping();
        createH2ToDerbyMapping();

    }

    @Override
    public String mapColumnType(final ColumnMetaData columnMetaData, DatabaseType sourceDatabase, DatabaseType targetDatabase) {

        final String columnType = columnMetaData.getColumnTypeName().toUpperCase();

        String targetColumnType = getMapping(sourceDB, targetDB, columnType);

        if (targetColumnType != null)
            return targetColumnType;
        else
            return new DefaultColumnTypeMapper().getColumnType(columnMetaData, sourceDB, targetDB);
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

        Map<DatabaseType, Map<String, String>> databaseMatrix = _mappings.get(sourceDB);



        if (databaseMatrix == null) {
            databaseMatrix = new HashMap<>();
            _mappings.put(sourceDB, databaseMatrix);
        }

        Map<String, String> mapping = databaseMatrix.get(targetDB);

        if (mapping == null) {
            mapping = new HashMap<>();
            databaseMatrix.put(targetDB, mapping);
        }

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

        System.out.println("Mapping from Postgres to Mysql");

    }


    private void createH2ToDerbyMapping() {

        addMapping(DatabaseType.H2DB, DatabaseType.DERBY, "LONGTEXT", "CLOB");
        addMapping(DatabaseType.H2DB, DatabaseType.DERBY, "LONGBLOB", "BLOB");
        System.out.println("Mapping from H2 to Derby");



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

        System.out.println("Mapping from Mysql to Postgres");



    }



}
