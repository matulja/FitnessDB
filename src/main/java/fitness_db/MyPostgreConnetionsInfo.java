package fitness_db;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 21.03.17.
 */
public class MyPostgreConnetionsInfo extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public MyPostgreConnetionsInfo() {
        super("jdbc:postgresql://localhost/", "user", "pass",
                "org.postgresql.Driver", "fitness", DatabaseType.POSTGRESQL);
    }


}
