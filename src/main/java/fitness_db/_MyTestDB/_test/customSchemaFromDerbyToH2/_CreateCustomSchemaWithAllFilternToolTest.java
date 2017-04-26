package fitness_db._MyTestDB._test.customSchemaFromDerbyToH2;


import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;
import fitness_db._MyTestDB._test._hints.CustomColumnNameFilterTest;
import fitness_db._MyTestDB._test._hints.CustomTableNameFilterTest;
import fitness_db._MyTestDB._test.configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test.configuration.TestH2ConnectionInfo;
import fitness_db._MyTestDB.mapping.CustomColumnRenameName;
import fitness_db._MyTestDB.mapping.CustomTableRenameName;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class _CreateCustomSchemaWithAllFilternToolTest extends AbstractGuttenBaseTest
{
  private static final String SOURCE_CONNECTOR_ID = "derby";
  private static final String TARGET_CONNECTOR_ID = "hsqldb";

  private final CreateCustomSchemaTool _objectUnderTest = new CreateCustomSchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
      _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID, new TestDerbyConnectionInfo());
      _connectorRepository.addConnectionInfo(TARGET_CONNECTOR_ID, new TestH2ConnectionInfo());
      new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID , "/ddl/derby/script-allshop-derby-mod.sql");

      //ConnectionHint for Mapping TableName
      _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new TableNameMapperHint() {
          @Override
          public TableNameMapper getValue() {
              return new CustomTableRenameName()
                      .addReplacement("OFFICES", "TAB_OFFICES")
                      .addReplacement("ORDERS","TAB_ORDERS");}
      });

      _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new TableMapperHint() {
          @Override
          public TableMapper getValue() {
              return new CustomTableRenameName()
                      .addReplacement("OFFICES", "TAB_OFFICES")
                      .addReplacement("ORDERS","TAB_ORDERS");}
      });

      //ConnectionHint for Mapping ColumnName
      _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new ColumnMapperHint() {
          @Override
          public ColumnMapper getValue() {
              return new CustomColumnRenameName()
                      .addReplacement("OFFICECODE", "ID_OFFICECODE")
                      .addReplacement("ORDERNUMBER", "ID_ORDERNUMBER")
                      .addReplacement("PHONE", "ID_PHONE")
                      .addReplacement("CITY", "ID_CITY");
          }
      });
      _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new ColumnNameMapperHint() {
          @Override
          public ColumnNameMapper getValue() {
              return new CustomColumnRenameName()
                      .addReplacement("OFFICECODE", "ID_OFFICECODE")
                      .addReplacement("ORDERNUMBER", "ID_ORDERNUMBER")
                      .addReplacement("PHONE", "ID_PHONE")
                      .addReplacement("CITY", "ID_CITY");
          }
      });

      //ConnectionHint for Mapping ColumnType
      _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomColumnTypeMapperHint() {
          @Override
          public CustomColumnTypeMapper getValue() {
              return new CustomDefaultColumnTypeMapper(DatabaseType.H2DB, DatabaseType.DERBY);
          }
      });


  }


    @Test
  public void testScript() throws Exception
  {

      _objectUnderTest.copySchema(SOURCE_CONNECTOR_ID, TARGET_CONNECTOR_ID);

      assertEquals("After", "TAB_OFFICES", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
              getTableMetaData("TAB_OFFICES").getTableName());

      assertEquals("After", "TAB_ORDERS", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
              getTableMetaData("TAB_ORDERS").getTableName());

      assertEquals("After", "ID_CITY", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID)
              .getTableMetaData("CUSTOMERS").getColumnMetaData("ID_CITY").getColumnName());

      assertEquals("After", "ID_ORDERNUMBER", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID)
              .getTableMetaData("ORDERDETAILS").getColumnMetaData("ID_ORDERNUMBER").getColumnName());


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
