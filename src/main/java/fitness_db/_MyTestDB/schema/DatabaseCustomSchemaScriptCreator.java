package fitness_db._MyTestDB.schema;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.*;
import de.akquinet.jbosscc.guttenbase.meta.*;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.TableOrderTool;
import fitness_db._MyTestDB.type_Column.CustomColumnTypeMapper;

import java.sql.SQLException;
import java.util.*;

/**
 * Create DDL script from given database meta data.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */

@SuppressWarnings({"MismatchedQueryAndUpdateOfStringBuilder", "UnusedAssignment", "Duplicates"})
public class DatabaseCustomSchemaScriptCreator {

    private static final Random RANDOM = new Random();
    public static final int MAX_ID_LENGTH = 64;
    private final DatabaseType sourceType=DatabaseType.MYSQL;
    private final DatabaseType targetType=DatabaseType.POSTGRESQL;


    private final DatabaseMetaData _sourceDatabaseMetaData;

    private final String _targetSchema;
    private final int _maxIdLength;
    private final ConnectorRepository _connectorRepository;
    private final String _sourceConnectorId;



    public DatabaseCustomSchemaScriptCreator(
            final ConnectorRepository connectorRepository,
            final String sourceConnectorId,
            final String targetSchema,
            final int maxIdLength) throws SQLException {
        assert sourceConnectorId != null : "sourceConnectorId != null";
        assert targetSchema != null : "schema != null";
        assert !targetSchema.contains("."): "Invalid schema name "+targetSchema;

        _sourceConnectorId = sourceConnectorId;
        _connectorRepository = connectorRepository;
        _sourceDatabaseMetaData = connectorRepository.getDatabaseMetaData(sourceConnectorId);
        _targetSchema = targetSchema;
        _maxIdLength = maxIdLength;
    }


    public DatabaseCustomSchemaScriptCreator(final ConnectorRepository connectorRepository, final String source, final String schema) throws SQLException {
        this(connectorRepository, source, schema, MAX_ID_LENGTH);
    }

    public DatabaseCustomSchemaScriptCreator(final ConnectorRepository connectorRepository, final String source) throws SQLException {
        this(connectorRepository, source, connectorRepository.getDatabaseMetaData(source).getSchema().trim());
    }

    public List<String> createTableStatements() throws SQLException {
        final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(_sourceDatabaseMetaData.getTableMetaData(), true);

        return createTableStatements(tables);
    }

    public List<String> createTableStatements(final List<TableMetaData> tables) throws SQLException {
        final List<String> result = new ArrayList<>();

        for (final TableMetaData tableMetaData : tables) {
            result.add(createTable(tableMetaData));
        }

        return result;
    }

    public List<String> createPrimaryKeyStatements() throws SQLException {
        final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
        return createPrimaryKeyStatements(tables);
    }

    public List<String> createPrimaryKeyStatements(final List<TableMetaData> tables) throws SQLException {

        final List<String> result = new ArrayList<>();
        for (final TableMetaData tableMetaData : tables) {
            int counter = 1;
            final List<ColumnMetaData> primaryKeyColumns = tableMetaData.getPrimaryKeyColumns();

            if (!primaryKeyColumns.isEmpty()) {
                result.add(createPrimaryKeyStatement(tableMetaData, primaryKeyColumns, counter++));
            }
        }

        return result;
    }

    public List<String> createIndexStatements() throws SQLException {
        final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
        return createIndexStatements(tables);
    }

    public List<String> createIndexStatements(final List<TableMetaData> tables) throws SQLException {
        final List<String> result = new ArrayList<>();

        for (final TableMetaData tableMetaData : tables) {
            int counter = 1;

            for (final IndexMetaData indexMetaData : tableMetaData.getIndexes()) {
                result.add(createIndex(indexMetaData, counter++));
            }
        }

        return result;
    }

    public List<String> createForeignKeyStatements() throws SQLException {
        final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
        return createForeignKeyStatements(tables);
    }

    public List<String> createForeignKeyStatements(final List<TableMetaData> tables) throws SQLException {
        final List<String> result = new ArrayList<>();

        for (final TableMetaData tableMetaData : tables) {
            int counter = 1;

            for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
                if (columnMetaData.getReferencedColumn() != null) {
                    result.add(createForeignKey(columnMetaData, counter++));
                }
            }
        }

        return result;
    }


    public String createTable(final TableMetaData tableMetaData) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();

        final StringBuilder builder = new StringBuilder("CREATE TABLE " + getSchemaPrefix()
                + tableNameMapper.mapTableName(tableMetaData)
                + "\n(\n");

        for (final Iterator<ColumnMetaData> iterator = tableMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
            final ColumnMetaData columnMetaData = iterator.next();

            builder.append("  ").append(createColumn(columnMetaData));

            if (iterator.hasNext()) {
                builder.append(", \n");
            }
        }

        builder.append("\n);");
        return builder.toString();
    }

    private String getSchemaPrefix() {
        return "".equals(_targetSchema) ? "" : _targetSchema + ".";
    }

    private String createPrimaryKeyStatement(final TableMetaData tableMetaData, final List<ColumnMetaData> primaryKeyColumns,
                                             final int counter) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        final String tableName = tableNameMapper.mapTableName(tableMetaData);
        final String pkName = "PK_" + tableName + "_" + counter;
        final StringBuilder builder = new StringBuilder("ALTER TABLE " + (getSchemaPrefix())
                + tableName
                + " ADD CONSTRAINT " +
                pkName
                + " PRIMARY KEY (");

        for (final ColumnMetaData columnMetaData : primaryKeyColumns) {
            builder.append(columnNameMapper.mapColumnName(columnMetaData)).append(", ");
        }

        builder.setLength(builder.length() - 2);
        builder.append(");");
        return builder.toString();

    }

    private String createIndex(final IndexMetaData indexMetaData, final int counter) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
        final String indexName = createConstraintName("IDX_", CaseConversionMode.UPPER.convert(indexMetaData.getIndexName())
                        + "_"
                        + tableNameMapper.mapTableName(tableMetaData)
                        + "_",
                counter);
        return createIndex(indexMetaData, indexName);
    }

    public String createIndex(final IndexMetaData indexMetaData) throws SQLException {
        return createIndex(indexMetaData, indexMetaData.getIndexName());
    }

    private String createIndex(final IndexMetaData indexMetaData, final String indexName) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
        final String schemaPrefix = getSchemaPrefix();
        final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

        final StringBuilder builder = new StringBuilder("CREATE" + unique
                + "INDEX "
                + indexName
                + " ON "
                + schemaPrefix
                + tableNameMapper.mapTableName(tableMetaData)
                + "(");


        for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
            final ColumnMetaData columnMetaData = iterator.next();

            builder.append(columnNameMapper.mapColumnName(columnMetaData));

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append(");");
        return builder.toString();
    }

    private String createForeignKey(final ColumnMetaData referencingColumn, final int counter) throws SQLException {
        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
        final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();

        final String tablename = tableNameMapper.mapTableName(tableMetaData);
        final String fkName = createConstraintName("FK_", tablename + "_"
                + columnNameMapper.mapColumnName(referencingColumn)
                + "_"
                + columnNameMapper.mapColumnName(referencedColumn) + "_", counter);

        return createForeignKey(referencingColumn, fkName);
    }

    public String createForeignKey(final ColumnMetaData referencingColumn, final String fkName) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
        final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
        final String tablename = tableNameMapper.mapTableName(tableMetaData);
        final String schemaPrefix = getSchemaPrefix();

        assert !tablename.contains(".") : "Invalid table name " + tablename;

        String builder = "ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT " + fkName +
                " FOREIGN KEY (" + columnNameMapper.mapColumnName(referencingColumn) + ") REFERENCES " + schemaPrefix +
                tableNameMapper.mapTableName(referencedColumn.getTableMetaData()) + "(" + columnNameMapper.mapColumnName(referencedColumn) + ");";
        return builder;
    }

    public String createForeignKey(final ForeignKeyMetaData foreignKeyMetaData) throws SQLException {
        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();


        final ColumnMetaData referencingColumn = foreignKeyMetaData.getReferencingColumn();
        final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
        final ColumnMetaData referencedColumn = foreignKeyMetaData.getReferencedColumn();
        final String tablename = tableNameMapper.mapTableName(tableMetaData);
        final String schemaPrefix = getSchemaPrefix();

        final StringBuilder builder = new StringBuilder("ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT ");
        builder.append(foreignKeyMetaData.getForeignKeyName());
        StringBuilder append = builder.append(" FOREIGN KEY (").append(columnNameMapper.mapColumnName(referencingColumn)).append(") REFERENCES ")
                .append(schemaPrefix).append(tableNameMapper.mapTableName(referencedColumn.getTableMetaData())).append("(").append(columnNameMapper.mapColumnName(referencedColumn)).append(");");

        return builder.toString();
    }

    private String createColumn(final ColumnMetaData columnMetaData) throws SQLException {

        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        //old ColumnType Mapper
        // final ColumnTypeMapper columnTypeMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnTypeMapper.class).getValue();

        final CustomColumnTypeMapper customColumnTypeMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, CustomColumnTypeMapper.class).getValue();
        final StringBuilder builder = new StringBuilder();

        builder.append(columnNameMapper.mapColumnName(columnMetaData)).append(" ").append(customColumnTypeMapper.getColumnType(columnMetaData, sourceType, targetType));

        if (!columnMetaData.isNullable()) {
            builder.append(" NOT NULL");
        }

        return builder.toString();
    }

    public String createConstraintName(final String prefix, final String preferredName, final int uniqueId) {
        final StringBuilder name = new StringBuilder(preferredName);
        final int maxLength = _maxIdLength - prefix.length() - String.valueOf(uniqueId).length();

        while (name.length() > maxLength) {
            final int index = Math.abs(RANDOM.nextInt() % name.length());
            name.deleteCharAt(index);
        }

        return prefix + name + uniqueId;
    }

}
