package fitness_db._MyTestDB.schema.type_column;

import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;

/**
 * Created by mfehler on 10.04.17.
 */
public abstract class CustomColumnTypeMapperHint implements ConnectorHint<CustomColumnTypeMapper> {
    @Override
    public Class<CustomColumnTypeMapper> getConnectorHintType() {
        return CustomColumnTypeMapper.class;
    }
}
