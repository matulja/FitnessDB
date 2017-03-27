package fitness_db;

import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateSchemaTool;
import fitness_db.mapping.MappingColumnName;
import fitness_db.mapping.MappingColumnNameFilter;
import fitness_db.mapping.MappingColumnOrder;
import fitness_db.mapping.MappingTableNameFilter;

import java.sql.SQLException;

/**
 * Copy and Mapping Data
 *
 * Created by mfehler on 27.03.17.
 */
public class CopyFintnessDBMappingData {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";


        public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //delete the tables
        connectorRepository.addTargetDatabaseConfiguration(DatabaseType.POSTGRESQL, new GenericTargetDatabaseConfiguration(connectorRepository));

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //add ConnectorHints and Mapping Data

            // 1 --- copy only Tables with name starts with "k" ---
       connectorRepository.addConnectorHint( SOURCE, new MappingTableNameFilter());
       connectorRepository.addConnectorHint(TARGET, new MappingTableNameFilter());


           // 2 --- add to tables, starts with "k" _tab --- ???

       /* connectorRepository.addConnectorHint(TARGET, new DefaultTableMapperHint()
            {
                @Override
                public TableMapper getValue()
                {
                    return new MappingTableName();
                }
            });*/



        // 3 --- add Mapping "_id" for Table starts with ("k") & columns name starts with "abo"

       //final MappingColumnName columnNameMapper = new MappingColumnName();


       // 4 --- add Mapping to order Columns by ColumnName
       //connectorRepository.addConnectorHint(SOURCE, new MappingColumnOrder() );


        // 5 --- add Mapping, id --> UUID

        //copy only Schema
        new CreateSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);

        //copy Tables
        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,TARGET);


        //Mapping and add ConnectionHint

        /*final TableNameMapper tableNameMapper = connectorRepository.getConnectorHint(TARGET, TableNameMapper.class).getValue();
        System.out.println("Connector Hint for Target tableNameMapper " + tableNameMapper);
        connectorRepository.addConnectorHint(SOURCE,new MappingColumnData() );*/

    }
}


