package fitness_db._MyTestDB._test._configuration;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;
import fitness_db._MyTestDB._test.AbstractGuttenBaseTest;


public class TestH2ConnectionInfo extends URLConnectorInfoImpl {
	private static final long serialVersionUID = 1L;
	private static int count = 1;

	public TestH2ConnectionInfo() {
		super("jdbc:h2:" + AbstractGuttenBaseTest.DB_DIRECTORY + "/h2" + count++,
				"sa", "sa", "org.h2.Driver", "", DatabaseType.H2DB);
	}
}
