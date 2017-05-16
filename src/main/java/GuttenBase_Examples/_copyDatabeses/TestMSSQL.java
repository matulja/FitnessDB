package GuttenBase_Examples._copyDatabeses;

import GuttenBase_Examples.connInfo.DB2ConnetionsInfo;
import GuttenBase_Examples.connInfo.MSSQLConnectionsInfo;
import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;


/**
 * Created by mfehler on 21.03.17.
 */
public class TestMSSQL {


    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        connectorRepository.addTargetDatabaseConfiguration(DatabaseType.MSSQL, new GenericTargetDatabaseConfiguration(connectorRepository));

        connectorRepository.addConnectionInfo(TARGET, new MSSQLConnectionsInfo());
        de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData target = connectorRepository.getDatabaseMetaData(TARGET);


        System.out.println("Mssql Schema: " + target.getSchema());
        System.out.println("Tables : " + target.getTableMetaData());




    }

}


