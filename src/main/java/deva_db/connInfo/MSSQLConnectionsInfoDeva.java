package deva_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 16.05.17.
 */
public class MSSQLConnectionsInfoDeva extends URLConnectorInfoImpl {

        private static final long serialVersionUID = 1L;

        public MSSQLConnectionsInfoDeva() {
            super("jdbc:sqlserver://localhost:1433;DataBaseName=DEVA", "SA", "Fm123UA!",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver", "deva", DatabaseType.MSSQL);
        }

    }

