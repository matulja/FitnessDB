package ubi_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 26.05.17.
 */
public class MysqlConnetionsInfoUbi extends URLConnectorInfoImpl  {

    private static final long serialVersionUID = 1L;

    public MysqlConnetionsInfoUbi() {
        super("jdbc:mysql://localhost:3306/", "mary", "password",
                "com.mysql.jdbc.Driver", "ubimssql", DatabaseType.MYSQL);
    }

}