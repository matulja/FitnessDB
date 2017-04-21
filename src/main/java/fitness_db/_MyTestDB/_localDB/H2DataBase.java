package fitness_db._MyTestDB._localDB;

import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import fitness_db._MyTestDB.mapping.CustomColumnRenameName;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;

import java.util.List;

/**
 * Created by Marynasuprun on 31.03.2017.
 */
public class H2DataBase {

    private static final String CONNECTOR_ID = "hsqldb";
    private static final String TARGET_ID = "target";
    private static String table_name= "";


    public static void main(String[] args) throws Exception {

        final ConnectorRepository _connectorRepository = new ConnectorRepositoryImpl();
        _connectorRepository.addConnectionInfo(CONNECTOR_ID, new H2ConnectionInfo());
        _connectorRepository.addConnectionInfo(TARGET_ID, new H2ConnectionInfo());

        // add MappingColumnNameFilter -->  copy only columns starts with "FOO"

       // _connectorRepository.addConnectorHint(CONNECTOR_ID,new CustomTableNameFilter());


        //Mapping Table for tableName
        /*_connectorRepository.addConnectorHint(CONNECTOR_ID, new TableNameMapperHint() {
            @Override
            public TableNameMapper getValue() {
                return new CustomTableNameMapper();
            }
        });*/


        //Mapping ColumnName

        _connectorRepository.addConnectorHint(CONNECTOR_ID, new ColumnNameMapperHint() {
                    @Override
                    public ColumnNameMapper getValue() {
                        return new CustomColumnRenameName();
                    }
                });


                // CreateSchemaTool schemaTool = new CreateSchemaTool(_connectorRepository);
                System.out.println("schema");


      // new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID,true, false, "/ddl/tables.sql");
        System.out.println("ScriptExecutorTool");
        //Script
        List< String > script = new CreateCustomSchemaTool(_connectorRepository).createDDLScript(CONNECTOR_ID, TARGET_ID);
        for (String s : script) {

            System.out.println(s);
        }


        System.out.println("Test_End");





    }

}
