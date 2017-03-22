package fitness_db;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 21.03.17.
 */
class MySqlConnectionsInfo extends URLConnectorInfoImpl  {

    private static final long serialVersionUID = 1L;

        public MySqlConnectionsInfo() {
          super("jdbc:mysql://localhost:3306/fitness", "mary", "password",
                  "com.mysql.jdbc.Driver", "fitness", DatabaseType.MYSQL);
        }

    }
