package fitness_db._MyTestDB._test.customSchema;


import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;
import fitness_db._MyTestDB._test._configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test._configuration.TestH2ConnectionInfo;
import fitness_db._MyTestDB.mapping.CustomColumnNameFilter;
import fitness_db._MyTestDB.mapping.CustomColumnRenameName;
import fitness_db._MyTestDB.mapping.CustomTableNameFilter;
import fitness_db._MyTestDB.mapping.CustomTableRenameName;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class _CreateCustomSchemaWithRenameShopToolTest extends AbstractGuttenBaseTest
{
  private static final String SOURCE_CONNECTOR_ID = "hsqldb";
  private static final String TARGET_CONNECTOR_ID = "derby";

  private final CreateCustomSchemaTool _objectUnderTest = new CreateCustomSchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID , new TestH2ConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET_CONNECTOR_ID, new TestDerbyConnectionInfo());

    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID , "/ddl/script_allshop_h2.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(TARGET_CONNECTOR_ID, "/ddl/shop_rename_derby.sql");

    //ConnectionHint for Mapping ColumnName

    _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new ColumnNameMapperHint() {
      @Override
      public ColumnNameMapper getValue() {
        return new CustomColumnRenameName().addReplacement("", "");
      }
    });

    //ConnectionHint for Mapping TableName

    _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new TableNameMapperHint() {
      @Override
      public TableNameMapper getValue() {
        return new CustomTableRenameName().addReplacement("", "");
      }
    });

    //ConnectionHint for Mapping ColumnType
    _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID , new CustomColumnTypeMapperHint() {
      @Override
      public CustomColumnTypeMapper getValue() {
        return new CustomDefaultColumnTypeMapper(DatabaseType.H2DB, DatabaseType.DERBY);
      }
    });

  }

  @Test
  public void testScript() throws Exception
  {

   assertEquals("Before", "OFFICES", _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).
           getTableMetaData("OFFICES").getTableName());
   assertEquals("After", "TAB_OFFICES", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
           getTableMetaData("TAB_OFFICES").getTableName());


   assertEquals("Before", "ORDERS", _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).
           getTableMetaData("ORDERS").getTableName());
   assertEquals("After", "TAB_ORDERS", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
           getTableMetaData("TAB_ORDERS").getTableName());


    assertEquals("Before", "CITY", _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).
            getTableMetaData("CUSTOMERS").getColumnMetaData("CITY").getColumnName());

    assertEquals("After", "ID_CITY", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
           getTableMetaData("CUSTOMERS").getColumnMetaData("ID_CITY").getColumnName());

  }
}
