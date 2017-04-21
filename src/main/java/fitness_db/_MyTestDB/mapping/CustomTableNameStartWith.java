package fitness_db._MyTestDB.mapping;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Created by mfehler on 10.04.17.
 */
public class CustomTableNameStartWith implements TableNameMapper
{
    private final CaseConversionMode _caseConversionMode;
    public static final String PREFIX = "_";


    public CustomTableNameStartWith(final CaseConversionMode caseConversionMode, final boolean addSchema)
    {
        assert caseConversionMode != null : "caseConversionMode != null";
        _caseConversionMode = caseConversionMode;

    }

    public CustomTableNameStartWith()
    {
        this(CaseConversionMode.NONE, true);
    }

    @Override
    public String mapTableName(final TableMetaData tableMetaData, final DatabaseMetaData databaseMetaData) {

        final String defaultTableName = tableMetaData.getTableName();
        String tableName;

        if (defaultTableName.startsWith("o"))
        {
            tableName= PREFIX + defaultTableName;

        }

        else {

            tableName=defaultTableName;
        }

        return tableName;


    }
}

