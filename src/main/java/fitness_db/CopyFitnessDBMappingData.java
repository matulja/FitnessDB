package fitness_db;


import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateSchemaTool;

import java.sql.SQLException;

/**
 * Copy and Mapping Data
 *
 * Created by mfehler on 27.03.17.
 */
public class CopyFitnessDBMappingData {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";


    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo

        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //delete the tables
        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);



        //add ConnectorHints and Mapping Data

        // 1 --- add Mapping MappingTableNameFilter --> copy only Tables with name starts with "k" ---

       //connectorRepository.addConnectorHint(SOURCE, new MappingTableNameFilter());
       //connectorRepository.addConnectorHint(TARGET, new MappingTableNameFilter());


        // 2 --- add MappingTableName --> Hinweis geben, in Tabellen starts with "tab_" kopieren ---

        /*connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return new MappingTableName();
            }
        });*/

        // 3 --- add MappingColumnNameFilter -->  copy only columns starts with "k" or ends with "h" --?

        //connectorRepository.addConnectorHint(SOURCE,new MappingColumnNameFilter());
        //connectorRepository.addConnectorHint(TARGET,new MappingColumnNameFilter());

        // 4 --- add MappingColumnName --> Hinweis geben in columns starts with "id_abo" kopieren
        //final MappingColumnName columnNameMapper = new MappingColumnName();


        // 5 --- add MappingColumnOrder to order (sort) Columns by ColumnName

        //connectorRepository.addConnectorHint(SOURCE, new MappingColumnOrder() );


        // 6 --- add MappingColumnsIDtoUUID, id --> UUID


        //copy only Schema
        new CreateSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);

        //copy Tables
        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);

        /*SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);

        System.out.println(issues);
        if(!issues.isSevere()) {
            //copy Tables
            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        }*/

    }
}


