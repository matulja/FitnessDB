package fitness_db.mapping;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;


/**
 * look at columnName starts with "abo" and add to columnName "value + _id"
 *
 * Created by Marynasuprun on 26.03.2017.
 *
 */


public class MappingColumnName implements ColumnDataMapper {


    @Override
    public boolean isApplicable(ColumnMetaData sourceColumnMetaData,
                                ColumnMetaData targetColumnMetaData) throws SQLException {

        String columnName=sourceColumnMetaData.getColumnName();
        return columnName.startsWith("abo");
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
        mapString = mapString.toUpperCase() + " " +"_id";

        return mapString;
    }

    public static void main(final String[] args) throws SQLException {


        MappingColumnName map = new MappingColumnName();
        map.mapStrings("");
        System.out.println("map: " + map.mapStrings(""));

    }

}
