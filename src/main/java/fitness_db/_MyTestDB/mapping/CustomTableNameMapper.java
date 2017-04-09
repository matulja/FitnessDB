package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * add id to TableName
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class CustomTableNameMapper implements TableNameMapper
{
  private final CaseConversionMode _caseConversionMode;
  public static Integer counter=1;
  public static final String Nr_PREFIX = "_Nr";


  public CustomTableNameMapper(final CaseConversionMode caseConversionMode, final boolean addSchema)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;

  }

  public CustomTableNameMapper()
  {
    this(CaseConversionMode.NONE, true);
  }

  @Override
  public String mapTableName(final TableMetaData tableMetaData) {

    String tableName = _caseConversionMode.convert(tableMetaData.getTableName());
    return tableName + Nr_PREFIX  + counter ++;
    //return tableName + Nr_PREFIX;

  }


}
