package fitness_db;

import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateSchemaTool;

import java.sql.SQLException;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyFitnessDBFomSqlToPostgre {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        connectorRepository.addTargetDatabaseConfiguration(DatabaseType.POSTGRESQL, new GenericTargetDatabaseConfiguration(connectorRepository));

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //get Source Name
        DatabaseMetaData source = connectorRepository.getDatabaseMetaData(SOURCE);
        DatabaseMetaData target = connectorRepository.getDatabaseMetaData(TARGET);

        System.out.println(source.getDatabaseName());
        System.out.println(target.getDatabaseName());


        //delete the tables

        /*DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);*/


        //copy only Schema
        new CreateSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);


        //copy Tables
        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,TARGET);
    }
}
