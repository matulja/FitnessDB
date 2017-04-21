package fitness_db._MyTestDB._test.customSchema;


import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;
import fitness_db._MyTestDB._test._configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test._configuration.TestH2ConnectionInfo;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class _CreateCustomSchemaAllShopToolTest extends AbstractGuttenBaseTest
{
  private static final String SOURCE_CONNECTOR_ID = "hsqldb";
  private static final String TARGET_CONNECTOR_ID = "derby";

  private final CreateCustomSchemaTool _objectUnderTest = new CreateCustomSchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID, new TestH2ConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET_CONNECTOR_ID, new TestDerbyConnectionInfo());

    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID, "/ddl/script_allshop_h2.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(TARGET_CONNECTOR_ID, "/ddl/script_allshop_derby.sql");

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
    final List<String> script = _objectUnderTest.createDDLScript(SOURCE_CONNECTOR_ID, TARGET_CONNECTOR_ID);
    final List<String> parsedScript = new SQLLexer(script).parse();

    assertTrue(parsedScript.
            contains("CREATE TABLE PRODUCTLINES ( PRODUCTLINE VARCHAR(50) NOT NULL,  TEXTDESCRIPTION VARCHAR(4000),  HTMLDESCRIPTION CLOB,  IMAGE BLOB )"));
    assertTrue(parsedScript.
            contains("ALTER TABLE ORDERDETAILS ADD CONSTRAINT FK_ORDERDETAILS_ORDERNUMBER_ORDERNUMBER_1 FOREIGN KEY (ORDERNUMBER) REFERENCES ORDERS(ORDERNUMBER)"));
    assertTrue(parsedScript.
            contains("ALTER TABLE PRODUCTS ADD CONSTRAINT FK_PRODUCTS_PRODUCTLINE_PRODUCTLINE_1 FOREIGN KEY (PRODUCTLINE) REFERENCES PRODUCTLINES(PRODUCTLINE)"));



  }
}
