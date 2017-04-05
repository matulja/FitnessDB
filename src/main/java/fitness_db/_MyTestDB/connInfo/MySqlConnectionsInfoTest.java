package fitness_db._MyTestDB.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 21.03.17.
 */
public class MySqlConnectionsInfoTest extends URLConnectorInfoImpl  {

    private static final long serialVersionUID = 1L;

        public MySqlConnectionsInfoTest() {
          super("jdbc:mysql://localhost:3306/", "mary", "password",
                  "com.mysql.jdbc.Driver", "mytestDB", DatabaseType.MYSQL);
        }

    }
