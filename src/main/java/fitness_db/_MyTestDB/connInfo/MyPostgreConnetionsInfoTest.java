package fitness_db._MyTestDB.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 21.03.17.
 */
public class MyPostgreConnetionsInfoTest extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public MyPostgreConnetionsInfoTest() {
        super("jdbc:postgresql://localhost/", "user", "pass",
                "org.postgresql.Driver", "allshop", DatabaseType.POSTGRESQL);
    }


}
