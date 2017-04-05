package fitness_db._MyTestDB;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import fitness_db._MyTestDB.connInfo.MyPostgreConnetionsInfoTest;
import fitness_db._MyTestDB.connInfo.MySqlConnectionsInfoTest;
import fitness_db._MyTestDB.mapping.CustomTableNameMapper;
import fitness_db._MyTestDB.mapping.CustomTableRenameNameMapper;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyTestDBFromSqlToPostgre {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfoTest());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfoTest());


        //delete the tables
        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        //Mapping Table for tableName
        connectorRepository.addConnectorHint(SOURCE, new TableNameMapperHint() {
            @Override
            public TableNameMapper getValue() {
                return new CustomTableNameMapper();
            }
        });


        //Mapping Column



        //copy only Schema
        new CreateCustomSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);


        //Script
        List < String > script = new CreateCustomSchemaTool(connectorRepository).createDDLScript(SOURCE, "test");
         for (String s : script) {

             System.out.println(s);
             }
          System.out.println("End");


        }


    }


