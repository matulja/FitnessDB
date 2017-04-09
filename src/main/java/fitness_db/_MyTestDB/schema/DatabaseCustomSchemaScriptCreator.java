package fitness_db._MyTestDB.schema;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.*;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.TableOrderTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.SchemaColumnTypeMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
        _sourceConnectorId = sourceConnectorId;
        assert sourceConnectorId != null : "sourceConnectorId != null";
        assert targetSchema != null : "schema != null";

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

        //Filter

        return createTableStatements (tables);
    }

    //Columns

    /*public List<String> createColumns(List<ColumnMetaData> orderedSourceColumns) throws SQLException {

        final TableMetaData tableMetaData= (TableMetaData) _connectorRepository.getDatabaseMetaData(_sourceConnectorId);
        orderedSourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, _sourceConnectorId, tableMetaData);
        System.out.println("Columns " + createColumns(orderedSourceColumns));
        return createColumns(orderedSourceColumns);
    }*/



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

        String builder = "CREATE TABLE " + ("".equals(_targetSchema) ? "" : _targetSchema + ".")
                + tableNameMapper.mapTableName(tableMetaData)
                + "\n(\n" + "\n);";
        return builder;


    }

    private String createPrimaryKeyStatement(final TableMetaData tableMetaData, final List<ColumnMetaData> primaryKeyColumns,
                                             final int counter) throws SQLException {

        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnNameMapper columnNameMapper =  _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();

        //final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();

        final String tableName = tableNameMapper.mapTableName(tableMetaData);
        final String pkName = "PK_" + tableName + "_" + counter;
        final StringBuilder builder = new StringBuilder("ALTER TABLE " + ("".equals(_targetSchema) ? "" : _targetSchema + ".")
                + tableName
                + " ADD CONSTRAINT " +
                pkName
                + " PRIMARY KEY (");

        for (final ColumnMetaData columnMetaData : primaryKeyColumns)
        {
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
        final ColumnNameMapper columnNameMapper= _connectorRepository.getConnectorHint(_sourceConnectorId,ColumnNameMapper.class).getValue();

        final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
        final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";
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

            String column=columnMetaData.getColumnName();
           System.out.println("ColumnsName " +  columnNameMapper.mapColumnName(columnMetaData));

       //Columns Mapping

           /* final List<TableMetaData> tableSourceMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, _sourceConnectorId);
            for (final TableMetaData sourceTableMetaData : tableSourceMetaDatas)

            {
                final List<ColumnMetaData> sourceColumns = sourceTableMetaData.getColumnMetaData();
                final Map<String, String> map = new HashMap<>();

                for (final ColumnMetaData sourceColumnMetaData : sourceColumns)
                {
                    final int columnCount =sourceTableMetaData.getColumnCount();
                    final String columnName=sourceColumnMetaData.getColumnName();
                    for (int i = 1; i <= columnCount; i++)
                    {
                        map.put(columnName,"");
                        map.put("PASSWORD", "286400");
                        //System.out.println(" Column Name map" + map)
                        System.out.println("Column Name " + sourceColumnMetaData.getColumnName());


                    }
                    Iterator<String> keySetIterator = map.keySet().iterator();
                    while(keySetIterator.hasNext()){
                        String key = keySetIterator.next();
                        System.out.println("Key: " + key + " Value: " + map.get(key));
                    }



                }


            }*/




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
        final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();
        final ColumnNameMapper columnNameMapper= _connectorRepository.getConnectorHint(_sourceConnectorId,ColumnNameMapper.class).getValue();

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
        final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();
        final ColumnNameMapper columnNameMapper= _connectorRepository.getConnectorHint(_sourceConnectorId,ColumnNameMapper.class).getValue();

        final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
        final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
        final String tablename = tableNameMapper.mapTableName(tableMetaData);
        final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";

        String builder = "ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT " + fkName +
                " FOREIGN KEY (" + columnNameMapper.mapColumnName(referencingColumn) + ") REFERENCES " + schemaPrefix +
                tableNameMapper.mapTableName(referencedColumn.getTableMetaData()) + "(" + columnNameMapper.mapColumnName(referencedColumn) + ");";
        return builder;
    }

    public String createForeignKey(final ForeignKeyMetaData foreignKeyMetaData) throws SQLException {
        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();
        final ColumnNameMapper columnNameMapper= _connectorRepository.getConnectorHint(_sourceConnectorId,ColumnNameMapper.class).getValue();


        final ColumnMetaData referencingColumn = foreignKeyMetaData.getReferencingColumn();
        final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
        final ColumnMetaData referencedColumn = foreignKeyMetaData.getReferencedColumn();
        final String tablename = tableNameMapper.mapTableName(tableMetaData);
        final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";

        final StringBuilder builder = new StringBuilder("ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT ");
        builder.append(foreignKeyMetaData.getForeignKeyName());
        StringBuilder append = builder.append(" FOREIGN KEY (").append(columnNameMapper.mapColumnName(referencingColumn)).append(") REFERENCES ")
                .append(schemaPrefix).append(tableNameMapper.mapTableName(referencedColumn.getTableMetaData())).append("(").append(columnNameMapper.mapColumnName(referencedColumn)).append(");");

        return builder.toString();
    }

    private String createColumn(final ColumnMetaData columnMetaData) throws SQLException {

        //Mapping
        final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableNameMapper.class).getValue();
        final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();


        final StringBuilder builder = new StringBuilder();
        final ColumnNameMapper columnNameMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnNameMapper.class).getValue();
        final SchemaColumnTypeMapper columnTypeMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, SchemaColumnTypeMapper.class).getValue();

        builder.append(columnNameMapper.mapColumnName(columnMetaData)).append(" ").append(columnTypeMapper.getColumnType(columnMetaData));

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

    public static void main(final String[] args) throws SQLException {

    }

}
