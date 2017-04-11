package fitness_db._MyTestDB;

import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;

import fitness_db._MyTestDB.connInfo.MyPostgreConnetionsInfoTest;
import fitness_db._MyTestDB.connInfo.MySqlConnectionsInfoTest;
import fitness_db._MyTestDB.mapping.*;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.type_Column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.type_Column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.type_Column.CustomDefaultColumnTypeMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopySchemaTestDB {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfoTest());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfoTest());


        //delete the tables
        //DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        //dropTablesTool.dropIndexes(TARGET);
       // dropTablesTool.dropForeignKeys(TARGET);
        //dropTablesTool.dropTables(TARGET);


        //add Mapping TableFilter -->  copy only tables starts with " "
        connectorRepository.addConnectorHint(SOURCE,new CustomTableNameFilter());
        connectorRepository.addConnectorHint(TARGET, new CustomTableNameFilter());

        //add Mapping ColumnFilter --> copy tables starts with some value

       connectorRepository.addConnectorHint(SOURCE,new CustomColumnNameFilter());
       connectorRepository.addConnectorHint(TARGET, new CustomColumnNameFilter());


        //add Mapping ColumnName --> rename Columns Name
        connectorRepository.addConnectorHint(SOURCE, new ColumnNameMapperHint() {
            @Override
            public ColumnNameMapper getValue() {
                return new CustomColumnRenameName().addReplacement("", "");
            }
        });

        //add MappingTable  --> rename tables
        connectorRepository.addConnectorHint(SOURCE, new TableNameMapperHint() {
            @Override
            public TableNameMapper getValue() {
                return new CustomTableRenameName().addReplacement("", "");
            }
        });


        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new CustomColumnTypeMapperHint() {
                    @Override
                    public CustomColumnTypeMapper getValue() {
                        //return new CustomColumnType();
                       return new CustomDefaultColumnTypeMapper();
                    }
                });


                //copy only Schema
        new CreateCustomSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("schema");


        //Script
        List<String> script = new CreateCustomSchemaTool(connectorRepository).createDDLScript(SOURCE, "test");
        for (String s : script) {

            System.out.println(s);
        }
        System.out.println("End");


    }


}


