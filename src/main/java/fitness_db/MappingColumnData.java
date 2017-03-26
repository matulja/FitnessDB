package fitness_db;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;

/**
 * Created by Marynasuprun on 26.03.2017.
 */
public class MappingColumnData implements ColumnDataMapper {

    //map for column name? " "

    @Override
    public boolean isApplicable(ColumnMetaData sourceColumnMetaData,
                                ColumnMetaData targetColumnMetaData) throws SQLException {

        String columnName=sourceColumnMetaData.getColumnName();
        return columnName.equalsIgnoreCase("");
    }


    @Override
    public Object map(ColumnMetaData sourceColumnMetaData, ColumnMetaData targetColumnMetaData,
                      Object value) throws SQLException {

        String resultMap = mapStrings((String) value);

        System.out.println("map: " + value + "==" + resultMap);
        return resultMap;
    }

    private String mapStrings(String mapValue) {

        if (mapValue == null) {
            return "";
        }

        String mapString = mapValue;
        mapString = mapString.toUpperCase() + "map";

        return mapString;
    }

}
