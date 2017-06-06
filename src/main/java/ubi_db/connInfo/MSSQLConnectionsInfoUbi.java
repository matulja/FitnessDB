package ubi_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 16.05.17.
 */
public class MSSQLConnectionsInfoUbi extends URLConnectorInfoImpl {

        private static final long serialVersionUID = 1L;

        public MSSQLConnectionsInfoUbi() {
            super("jdbc:sqlserver://localhost:1433;DataBaseName=UBI", "SA", "Fm123UA!",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver", "ubi", DatabaseType.MSSQL);
        }

    }

