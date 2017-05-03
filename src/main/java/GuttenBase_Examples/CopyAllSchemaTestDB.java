package GuttenBase_Examples;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.CustomColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.CustomColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.CustomDefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateCustomSchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import GuttenBase_Examples.connInfo.MyPostgreConnetionsInfoTest;
import GuttenBase_Examples.connInfo.MySqlConnectionsInfoTest;

import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyAllSchemaTestDB {


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

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);

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


