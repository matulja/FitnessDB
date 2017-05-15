package GuttenBase_Examples._copyDatabeses;

import GuttenBase_Examples.connInfo.DB2ConnetionsInfo;
import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;


/**
 * Created by mfehler on 21.03.17.
 */
public class TestH2DB {


    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        connectorRepository.addTargetDatabaseConfiguration(DatabaseType.H2DB, new GenericTargetDatabaseConfiguration(connectorRepository));

        connectorRepository.addConnectionInfo(TARGET, new DB2ConnetionsInfo());

        de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData target = connectorRepository.getDatabaseMetaData(TARGET);


       // System.out.println("H2 Version: " + target.getMinorVersion());
      //  System.out.println("H2 Version Major: " + target.getMajorVersion());
        System.out.println("H2 Schema: " + target.getSchema());
        System.out.println("Tables : " + target.getTableMetaData());




    }

}


