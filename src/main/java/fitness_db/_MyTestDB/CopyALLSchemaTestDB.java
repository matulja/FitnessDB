package fitness_db._MyTestDB;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import fitness_db._MyTestDB.connInfo.MyPostgreConnetionsInfoTest;
import fitness_db._MyTestDB.connInfo.MySqlConnectionsInfoTest;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;

import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyALLSchemaTestDB {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";


    public static void main(final String[] args) throws Exception {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo
       connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfoTest());
       connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfoTest());



        //from postgres to sql
        //connectorRepository.addConnectionInfo(SOURCE, new MyPostgreConnetionsInfoTest());
        // connectorRepository.addConnectionInfo(TARGET, new MySqlConnectionsInfoTest());


        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new CustomColumnTypeMapperHint() {
                    @Override
                    public CustomColumnTypeMapper getValue() {
                         return new CustomDefaultColumnTypeMapper(DatabaseType.MYSQL, DatabaseType.POSTGRESQL);
                    }
                });

        //copy Schema
        List<String> script = new CreateCustomSchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {System.out.println(s);}

        new CreateCustomSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("Schema Done");

        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("Issues: "+ issues);
        if(!issues.isSevere()) {

            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        }

    }


}


