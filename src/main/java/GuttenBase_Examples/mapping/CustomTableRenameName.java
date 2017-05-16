package GuttenBase_Examples.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * look at columnName starts with some value and replace them
 * <p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class CustomTableRenameName implements TableMapper {


  private final Map<String, String> replacementsTables = new HashMap<>();
  private final CaseConversionMode _caseConversionMode;
  private final boolean _addSchema;

  public CustomTableRenameName(final CaseConversionMode caseConversionMode, final boolean addSchema) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
    _addSchema = addSchema;

    //addReplacement("offices", "tab_offices");
    //addReplacement("orders", "tab_orders");
    // addReplacement("orderdetails", "tab_ordersdetails");
  }

  public CustomTableRenameName() {
    this(CaseConversionMode.NONE, true);
  }

  @Override
  public TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {

    final String defaultTableName = _caseConversionMode.convert(source.getTableName());
    final String tableName = replacementsTables.containsKey(defaultTableName) ?
            replacementsTables.get(defaultTableName) : defaultTableName;

    return targetDatabaseMetaData.getTableMetaData(tableName);
  }

  @Override
  public String mapTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {
    final String result = _caseConversionMode.convert(source.getTableName());
    final String tableName = replacementsTables.get(result);

    if (tableName != null) {
      return _caseConversionMode.convert(tableName);
    } else {
      return result;
    }
  }

  public CustomTableRenameName addReplacement(final String sourceTable, final String targetTable) {
    replacementsTables.put(sourceTable, targetTable);

    return this;
  }

  @Override
  public String fullyQualifiedTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException {
    final String schema = targetDatabaseMetaData.getSchema();
    final String tableName = mapTableName(source, targetDatabaseMetaData);

    if ("".equals(schema.trim()) || !_addSchema) {
      return tableName;
    } else {
      return schema + "." + tableName;
    }
  }
}
