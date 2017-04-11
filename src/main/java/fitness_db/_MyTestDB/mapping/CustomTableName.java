package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
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
  public static final String Nr_PREFIX = "_";


  public CustomTableName(final CaseConversionMode caseConversionMode, final boolean addSchema)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;

  }

  public CustomTableName()
  {
    this(CaseConversionMode.NONE, true);
  }

  @Override
  public String mapTableName(final TableMetaData tableMetaData) {

    String tableName = _caseConversionMode.convert(tableMetaData.getTableName());
    return Nr_PREFIX + tableName;

  }

}
