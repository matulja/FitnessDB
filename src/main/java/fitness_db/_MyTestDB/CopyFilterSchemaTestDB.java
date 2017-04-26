package fitness_db._MyTestDB;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
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

import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyFilterSchemaTestDB {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfoTest());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfoTest());

        //from postgres to sql
        //connectorRepository.addConnectionInfo(SOURCE, new MyPostgreConnetionsInfoTest());
        //connectorRepository.addConnectionInfo(TARGET, new MySqlConnectionsInfoTest());


        //add Mapping TableFilter
        connectorRepository.addConnectorHint(SOURCE,new CustomTableNameFilter());
        connectorRepository.addConnectorHint(TARGET,new CustomTableNameFilter());

        //add Mapping ColumnFilter
        connectorRepository.addConnectorHint(SOURCE,new CustomColumnNameFilter());
        connectorRepository.addConnectorHint(TARGET,new CustomColumnNameFilter());

        //add MappingColumn  --> rename columns
        connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return new CustomColumnRenameName()
                        .addReplacement("officecode", "id_officecode")
                        .addReplacement("ordernumber", "id_ordernumber")
                        .addReplacement("phone", "id_phone")
                        .addReplacement("city", "id_city");
            }
        });
        connectorRepository.addConnectorHint(TARGET, new ColumnNameMapperHint() {
            @Override
            public ColumnNameMapper getValue() {
                return new CustomColumnRenameName()
                        .addReplacement("officecode", "id_officecode")
                        .addReplacement("ordernumber", "id_ordernumber")
                        .addReplacement("phone", "id_phone")
                        .addReplacement("city", "id_city");
            }
        });

        //add MappingTable  --> rename tables
        connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return new CustomTableRenameName()
                        .addReplacement("offices", "tab_offices")
                        .addReplacement("orders", "tab_orders")
                        .addReplacement("orderdetails", "tab_ordersdetails");
            }
        });
        connectorRepository.addConnectorHint(TARGET, new TableNameMapperHint() {
            @Override
            public TableNameMapper getValue() {
                return new CustomTableRenameName()
                        .addReplacement("offices", "tab_offices")
                        .addReplacement("orders", "tab_orders")
                        .addReplacement("orderdetails", "tab_ordersdetails");
            }
        });



        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new CustomColumnTypeMapperHint() {
                    @Override
                    public CustomColumnTypeMapper getValue() {
                         return new CustomDefaultColumnTypeMapper(DatabaseType.MYSQL, DatabaseType.POSTGRESQL);
                    }
                });


        List<String> script = new CreateCustomSchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {System.out.println(s);}

        new CreateCustomSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("Schema Done");

        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("Issues: "+ issues);
        if(!issues.isSevere()) {

            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        }


    }


}


