package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;
import java.util.UUID;


/**
 *
 * convert columns id to UUID
 *
 *
 * Created by mfehler on 27.03.17.
 */
public class MapingColumnsIdtoUUID implements ColumnDataMapper {
    @Override
    public boolean isApplicable(ColumnMetaData sourceColumnMetaData,
                                ColumnMetaData targetColumnMetaData) throws SQLException {
        return false;
    }

    @Override
    public Object map(ColumnMetaData sourceColumnMetaData, ColumnMetaData targetColumnMetaData, Object value) throws SQLException {
        final Number number = (Number) value;

        if (number != null) {
            final long id = number.longValue();
            final ColumnMetaData referencedColumn = sourceColumnMetaData.getReferencedColumn();
            String uuid;

            if (referencedColumn != null) {
                uuid = columnKey(referencedColumn, id);
            } else {
                uuid = columnKey(sourceColumnMetaData, id);
            }

            return uuid;
        } else {
            return null;
        }
    }


    private String columnKey(final ColumnMetaData columnMetaData, final long id) {

        final String key = columnMetaData.getTableMetaData().getTableName() + ":" + columnMetaData.getColumnName();
        final UUID uuid = new UUID(key.hashCode(), id);
        return uuid.toString();

    }


}