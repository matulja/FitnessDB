package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;

/**
 * find the Name and Rename name to ...
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class CustomTableRenameNameMapper implements TableNameMapper {


  private final CaseConversionMode _caseConversionMode;

  public CustomTableRenameNameMapper(final CaseConversionMode caseConversionMode) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;

  }

  public CustomTableRenameNameMapper() {
    this(CaseConversionMode.NONE);
  }


  @Override
  public String mapTableName(TableMetaData tableMetaData) throws SQLException {

    final String defaultTableName = tableMetaData.getTableName();
    String tableName;

    switch (defaultTableName)
    {
      case "CountryData":
        tableName = defaultTableName.replace("CountryData", "MyCountryData");
        break;
      case "MeineAdressen":
        tableName = defaultTableName.replace("MeineAdressen", "MyAdresses");
        break;
      default:
        tableName = defaultTableName;
        break;
    }

    return tableName;


  }
}
