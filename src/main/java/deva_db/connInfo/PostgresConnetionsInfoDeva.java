package deva_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 23.05.17.
 */
public class PostgresConnetionsInfoDeva extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public PostgresConnetionsInfoDeva () {
        super("jdbc:postgresql://localhost/deva", "user", "pass",
                "org.postgresql.Driver", "devamssql", DatabaseType.POSTGRESQL);
    }


}