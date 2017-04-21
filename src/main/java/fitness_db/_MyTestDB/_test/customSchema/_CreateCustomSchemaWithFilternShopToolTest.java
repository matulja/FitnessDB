package fitness_db._MyTestDB._test.customSchema;


import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;
import fitness_db._MyTestDB._test._configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test._configuration.TestH2ConnectionInfo;
import fitness_db._MyTestDB.mapping.CustomColumnNameFilter;
import fitness_db._MyTestDB.mapping.CustomTableNameFilter;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class _CreateCustomSchemaWithFilternShopToolTest extends AbstractGuttenBaseTest
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
    new ScriptExecutorTool(_connectorRepository).executeFileScript(TARGET_CONNECTOR_ID, "/ddl/shop_withfiltern_derby.sql");

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

    assertEquals("Before", 9, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
    assertEquals("Before", 13, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("customers").getColumnCount());
 //   assertEquals("Before", 8, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("employees").getColumnCount());

    assertEquals("After", 8, _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).getTableMetaData().size());
    assertEquals("After", 10, _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).getTableMetaData("customers").getColumnCount());
  //  assertEquals("After", 5, _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).getTableMetaData("employees").getColumnCount());

    _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomTableNameFilter() );
    _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new CustomTableNameFilter() );
    _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomColumnNameFilter() );
    _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new CustomColumnNameFilter() );





  }
}
