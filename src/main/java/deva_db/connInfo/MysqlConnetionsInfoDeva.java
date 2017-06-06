package deva_db.connInfo;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

/**
 * Created by mfehler on 03.05.17.
 */
public class MysqlConnetionsInfoDeva extends URLConnectorInfoImpl  {

    private static final long serialVersionUID = 1L;

    public MysqlConnetionsInfoDeva() {
        super("jdbc:mysql://localhost:3306/", "mary", "password",
                "com.mysql.jdbc.Driver", "devamssql", DatabaseType.MYSQL);
    }

}