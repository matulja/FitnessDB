package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * look at columnName starts with some value and replace them
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class CustomTableRenameName implements TableNameMapper {


  private final Map<String, String> replacementsTables = new HashMap<>();
  private final CaseConversionMode _caseConversionMode;


  public CustomTableRenameName(final CaseConversionMode caseConversionMode) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;

  }

  public CustomTableRenameName() {
    this(CaseConversionMode.NONE);
  }


  @Override
  public String mapTableName(TableMetaData tableMetaData) throws SQLException {

    final String defaultTableName = tableMetaData.getTableName();
    final String tableName = replacementsTables.get(defaultTableName);

    if (tableName==null)
    {
      return defaultTableName;
    }

    else
    {
      return tableName;
    }

  }

  public CustomTableRenameName addReplacement(final String sourceTable, final String targetTable){
    replacementsTables.put(sourceTable, targetTable);
    replacementsTables.put("orders", "tab_orders");
    replacementsTables.put("orderdetails", "tab_ordersdetails");
    return this;
  }
}
