package ubi_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 26.05.17.
 */
public class DB2ConnetionsInfoUbi extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public DB2ConnetionsInfoUbi() {
        super("jdbc:db2://127.0.0.1:50000/SAMPLE", "db2inst1", "db2inst1",
               "com.ibm.db2.jcc.DB2Driver", "UBIMSSQL", DatabaseType.DB2);
    }


}
