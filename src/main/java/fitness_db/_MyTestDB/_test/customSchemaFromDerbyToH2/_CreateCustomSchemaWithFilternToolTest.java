package fitness_db._MyTestDB._test.customSchemaFromDerbyToH2;


import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;
import fitness_db._MyTestDB._test.configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test._hints.CustomColumnNameFilterTest;
import fitness_db._MyTestDB._test._hints.CustomTableNameFilterTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class _CreateCustomSchemaWithFilternToolTest extends AbstractGuttenBaseTest
{
  private static final String SOURCE_CONNECTOR_ID = "derby";

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID, new TestDerbyConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID , "/ddl/derby/script-allshop-derby-mod.sql");

  }

  @Test
  public void testScript() throws Exception
  {

    assertEquals("Before", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
    assertEquals("Before", 13, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
    assertEquals("Before", 8, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());

    _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomTableNameFilterTest());
    _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomColumnNameFilterTest());
    _connectorRepository.refreshDatabaseMetaData(SOURCE_CONNECTOR_ID);

    assertEquals("After", 9, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
    assertEquals("After", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
    assertEquals("Before", 5, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());






  }
}
