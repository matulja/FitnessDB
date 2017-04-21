package fitness_db._MyTestDB;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import fitness_db._MyTestDB.connInfo.MyPostgreConnetionsInfoTest;
import fitness_db._MyTestDB.connInfo.MySqlConnectionsInfoTest;
import fitness_db._MyTestDB.mapping.CustomColumnNameFilter;
import fitness_db._MyTestDB.mapping.CustomColumnRenameName;
import fitness_db._MyTestDB.mapping.CustomTableNameFilter;
import fitness_db._MyTestDB.mapping.CustomTableRenameName;
import fitness_db._MyTestDB.schema.CreateCustomSchemaTool;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapper;
import fitness_db._MyTestDB.schema.type_column.CustomColumnTypeMapperHint;
import fitness_db._MyTestDB.schema.type_column.CustomDefaultColumnTypeMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyFilterSchemaTestDB {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //add Source & Target ConnectionInfo
       // connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfoTest());
        //connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfoTest());

        //from postgres to sql
        connectorRepository.addConnectionInfo(SOURCE, new MyPostgreConnetionsInfoTest());
        connectorRepository.addConnectionInfo(TARGET, new MySqlConnectionsInfoTest());


        //delete the tables
        //DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        //dropTablesTool.dropIndexes(TARGET);
       // dropTablesTool.dropForeignKeys(TARGET);
        //dropTablesTool.dropTables(TARGET);


        //add Mapping TableFilter -->  copy only tables starts with " "
        connectorRepository.addConnectorHint(SOURCE,new CustomTableNameFilter());
        connectorRepository.addConnectorHint(TARGET,new CustomTableNameFilter());

        //add Mapping ColumnFilter --> copy tables starts with some value

       connectorRepository.addConnectorHint(SOURCE,new CustomColumnNameFilter());
       connectorRepository.addConnectorHint(TARGET,new CustomColumnNameFilter());


        //add Mapping ColumnName --> rename Columns Name
        connectorRepository.addConnectorHint(SOURCE, new ColumnNameMapperHint() {
            @Override
            public ColumnNameMapper getValue() {
                return new CustomColumnRenameName().addReplacement("", "");
            }
        });
        connectorRepository.addConnectorHint(TARGET, new ColumnNameMapperHint() {
            @Override
            public ColumnNameMapper getValue() {
                return new CustomColumnRenameName().addReplacement("", "");
            }
        });

        //add MappingTable  --> rename tables

        connectorRepository.addConnectorHint(TARGET, new TableNameMapperHint() {
            @Override
            public TableNameMapper getValue() {
                return new CustomTableRenameName().addReplacement("", "");
            }
        });




        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new CustomColumnTypeMapperHint() {
                    @Override
                    public CustomColumnTypeMapper getValue() {
                         return new CustomDefaultColumnTypeMapper(DatabaseType.POSTGRESQL, DatabaseType.MYSQL);
                    }
                });




        //copy Schema
        List<String> script = new CreateCustomSchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {

            System.out.println(s);
        }
        //new CreateCustomSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("Schema Done");


        //TODO copy tables
        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("Issues: "+ issues);
        if(!issues.isSevere()) {

            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        }



    }


}


