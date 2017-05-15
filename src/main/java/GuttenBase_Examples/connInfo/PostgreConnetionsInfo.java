package GuttenBase_Examples.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 21.03.17.
 */
public class PostgreConnetionsInfo extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public PostgreConnetionsInfo() {
        super("jdbc:postgresql://localhost/", "user", "pass",
                "org.postgresql.Driver", "shopdb2", DatabaseType.POSTGRESQL);
    }


}
