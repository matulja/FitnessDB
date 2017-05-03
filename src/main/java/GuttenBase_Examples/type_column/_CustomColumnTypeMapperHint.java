package GuttenBase_Examples.type_column;

import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;


/**
 * Created by mfehler on 10.04.17.
 */
public abstract class _CustomColumnTypeMapperHint implements ConnectorHint<_CustomColumnTypeMapper> {
    @Override
    public Class<_CustomColumnTypeMapper> getConnectorHintType() {
        return _CustomColumnTypeMapper.class;
    }
}
