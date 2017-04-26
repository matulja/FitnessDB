package fitness_db._MyTestDB.localDB;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

import java.io.File;

/**
 * Created by Marynasuprun on 31.03.2017.
 */
public class H2ConnectionInfo extends URLConnectorInfoImpl {
    private static final long serialVersionUID = 1L;
    public static final File DB_DIRECTORY = new File("./target/db");
    private static int count = 1;

    public H2ConnectionInfo() {
        super("jdbc:h2:" + DB_DIRECTORY+ "/h2" + count++, "sa", "sa", "org.h2.Driver", "", DatabaseType.H2DB);
    }
}
