package fitness_db._MyTestDB.schema;


import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.DatabaseSchemaScriptCreator;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create DDL from existing schema
 *
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
@SuppressWarnings("SameParameterValue")
public class CreateCustomSchemaTool

{
    private final ConnectorRepository _connectorRepository;
    private final int _maxIdLength;


    public CreateCustomSchemaTool(final ConnectorRepository connectorRepository, final int maxIdLength)
    {
        assert connectorRepository != null : "connectorRepository != null";

        _connectorRepository = connectorRepository;
        _maxIdLength = maxIdLength;
    }

    public CreateCustomSchemaTool(final ConnectorRepository connectorRepository)
    {
        this(connectorRepository, DatabaseSchemaScriptCreator.MAX_ID_LENGTH);
    }

    public List<String> createDDLScript(final String connectorId, final String targetSchema) throws SQLException
    {
        final List<String> result = new ArrayList<>();
        final DatabaseCustomSchemaScriptCreator databaseCustomSchemaScriptCreator = new DatabaseCustomSchemaScriptCreator(_connectorRepository, connectorId,
                targetSchema, _maxIdLength);

        result.addAll( databaseCustomSchemaScriptCreator.createTableStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createPrimaryKeyStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createForeignKeyStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createIndexStatements());

        return result;
    }

    public void copySchema(final String sourceConnectorId, final String targetConnectorId) throws SQLException
    {
        final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

        final List<String> ddlScript = createDDLScript(sourceConnectorId, databaseMetaData.getSchema());

        new ScriptExecutorTool(_connectorRepository).executeScript(targetConnectorId, ddlScript);
    }
}

