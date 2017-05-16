package GuttenBase_Examples;

import GuttenBase_Examples.connInfo.PostgreConnetionsInfo;
import GuttenBase_Examples.connInfo.SqlConnectionsInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;

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
        connectorRepository.addConnectionInfo(SOURCE, new SqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new PostgreConnetionsInfo());

        //from postgres to sql
        //connectorRepository.addConnectionInfo(SOURCE, new PostgreConnetionsInfo());
         //connectorRepository.addConnectionInfo(TARGET, new SqlConnectionsInfo());

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);

        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new ColumnTypeMapperHint() {
                    @Override
                    public ColumnTypeMapper getValue() {
                         return new DefaultColumnTypeMapper();
                    }
                });

        //copy Schema
        List<String> script = new CopySchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {System.out.println(s);}

        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("Schema Done");

        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("Issues: "+ issues);
        if(!issues.isSevere()) {

            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        }

    }


}


