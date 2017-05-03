package GuttenBase_Examples.type_column;

/**
 * Created by mfehler on 18.04.17.
 */
public class _CustomColumnTypeMapping {
    private final String _sourceColumnType;
    private final String _targetColumnType;

    public _CustomColumnTypeMapping(final String sourceColumnType, final String targetColumnType) {
        assert sourceColumnType != null;
        assert targetColumnType != null;

        _sourceColumnType = sourceColumnType;
        _targetColumnType = targetColumnType;
    }

    public String getSourceColumnType() {
        return _sourceColumnType;
    }

    public String getTargetColumnType() {
        return _targetColumnType;
    }
}
