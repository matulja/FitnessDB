package _GuttenBase_Examples.type_column;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Sometimes data types of columns are not compatible: Allow user to set mapping.
 */
public interface _CustomColumnTypeMapper
{
  String mapColumnType(final ColumnMetaData columnMetaData, final DatabaseType sourceDatabase, final DatabaseType targetDatabase) throws Exception;
}
