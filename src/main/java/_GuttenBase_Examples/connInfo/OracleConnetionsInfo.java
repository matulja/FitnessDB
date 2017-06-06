package _GuttenBase_Examples.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 03.05.17.
 */
public class OracleConnetionsInfo extends URLConnectorInfoImpl {

    private static final long serialVersionUID = 1L;

    public OracleConnetionsInfo() {
        super("jdbc:oracle:thin://@localhost:1521/XE", "mary", "pass",
               "oracle.jdbc.OracleDriver", "MARY", DatabaseType.ORACLE);
    }


}
