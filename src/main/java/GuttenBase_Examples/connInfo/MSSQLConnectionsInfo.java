package GuttenBase_Examples.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 16.05.17.
 */
public class MSSQLConnectionsInfo extends URLConnectorInfoImpl {

        private static final long serialVersionUID = 1L;

        public MSSQLConnectionsInfo() {
            super("jdbc:sqlserver://localhost:1433;DataBaseName=Shopdb2", "SA", "Fm123UA!",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver", "shop", DatabaseType.MSSQL);
        }

    }

