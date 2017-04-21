package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * look at columnName starts with some value and replace them
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 *
 *
 */
public class CustomTableName implements TableNameMapper
{
  private final CaseConversionMode _caseConversionMode;
  private final boolean _addSchema;
  private String tab= "_Nr";


  public CustomTableName(final CaseConversionMode caseConversionMode, final boolean addSchema)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
      _addSchema = addSchema;

  }

  public CustomTableName()
  {
    this(CaseConversionMode.NONE, true);
  }

  @Override
  public String mapTableName(final TableMetaData sourceTableMetaData, final DatabaseMetaData targetDatabaseMetaData) {

      final String schema = targetDatabaseMetaData.getSchema();
      final String table = _caseConversionMode.convert(sourceTableMetaData.getTableName());

      if ("".equals(schema.trim()) || !_addSchema)
      {
          return table + tab;
      }
      else
      {
          return schema + "." + table + tab;
      }
  }
}

