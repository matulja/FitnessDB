package fitness_db._MyTestDB._test.copyTables;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;

public class _DefaultCustomWithRenameTableCopyToolTest extends _AbstractCustomWithRenameTableCopyToolTest {
        @Override
        protected AbstractTableCopyTool getCopyTool() {
          return new DefaultTableCopyTool(_connectorRepository);
        }
}