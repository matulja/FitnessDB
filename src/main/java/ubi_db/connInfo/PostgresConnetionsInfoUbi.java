package ubi_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 26.05.17.
 */
public class PostgresConnetionsInfoUbi extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public PostgresConnetionsInfoUbi() {
        super("jdbc:postgresql://localhost/user", "user", "pass",
                "org.postgresql.Driver", "ubimssql", DatabaseType.POSTGRESQL);
    }


}