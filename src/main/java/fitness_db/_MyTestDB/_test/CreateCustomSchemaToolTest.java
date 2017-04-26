package fitness_db._MyTestDB._test;


import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import fitness_db._MyTestDB._test.configuration.TestDerbyConnectionInfo;
import fitness_db._MyTestDB._test.configuration.TestH2ConnectionInfo;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CreateCustomSchemaToolTest extends AbstractGuttenBaseTest
{
  private static final String source_CONNECTOR_ID = "hsqldb";
  private static final String target_CONNECTOR_ID = "derby";

  private final CreateCustomSchemaTool _objectUnderTest = new CreateCustomSchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(source_CONNECTOR_ID, new TestH2ConnectionInfo());
    _connectorRepository.addConnectionInfo(target_CONNECTOR_ID, new TestDerbyConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(source_CONNECTOR_ID, "/ddl/tables-h2.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(target_CONNECTOR_ID, "/ddl/tables-derby.sql");

    _connectorRepository.addConnectorHint(source_CONNECTOR_ID, new CustomColumnTypeMapperHint() {
      @Override
      public CustomColumnTypeMapper getValue() {
        return new CustomDefaultColumnTypeMapper(DatabaseType.H2DB, DatabaseType.DERBY);
      }
    });


  }

  @Test
  public void testScript() throws Exception
  {
    final List<String> script = _objectUnderTest.createDDLScript(source_CONNECTOR_ID, target_CONNECTOR_ID);
    final List<String> parsedScript = new SQLLexer(script).parse();

    assertTrue(parsedScript.contains("CREATE TABLE FOO_COMPANY ( ID BIGINT NOT NULL,  SUPPLIER CHAR(1),  NAME VARCHAR(100) )"));
    assertTrue(parsedScript.contains("ALTER TABLE FOO_COMPANY ADD CONSTRAINT PK_FOO_COMPANY_1 PRIMARY KEY (ID)"));
    assertTrue(parsedScript.contains("ALTER TABLE FOO_USER_ROLES ADD CONSTRAINT FK_FOO_USER_ROLES_USER_ID_ID_1 FOREIGN KEY (USER_ID) REFERENCES FOO_USER(ID)"));


  }
}
