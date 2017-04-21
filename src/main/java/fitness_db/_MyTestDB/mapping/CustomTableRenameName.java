package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
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
  private final boolean _addSchema;


  public CustomTableRenameName(final CaseConversionMode caseConversionMode, final boolean addSchema) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
    _addSchema = addSchema;

  }

  public CustomTableRenameName() {
    this(CaseConversionMode.NONE, true);
  }


  @Override
  public String mapTableName(final TableMetaData sourceTableMetaData, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {

    final String schema = targetDatabaseMetaData.getSchema();
    final String defaultTableName = _caseConversionMode.convert(sourceTableMetaData.getTableName());
    final String tableName = replacementsTables.get(defaultTableName);

    if ("".equals(schema.trim()) || !_addSchema && (tableName==null)) {
      return defaultTableName;
    }

    else if ("".equals(schema.trim()) || !_addSchema ) {
      return tableName;
    }

    else if (tableName==null) {
      return schema + "." + defaultTableName;
    }
    else {
      return schema + "." + tableName;
    }

  }

  public CustomTableRenameName addReplacement(final String sourceTable, final String targetTable){
    replacementsTables.put(sourceTable, targetTable);
    replacementsTables.put("offices", "tab_offices");
    replacementsTables.put("orders", "tab_orders");
    replacementsTables.put("orderdetails", "tab_ordersdetails");
    return this;
  }
}
