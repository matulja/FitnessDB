package GuttenBase_Examples;

import GuttenBase_Examples.mapping.CustomColumnNameFilter;
import GuttenBase_Examples.mapping.CustomTableRenameName;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.CustomColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.CustomColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.CustomDefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateCustomSchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import GuttenBase_Examples.connInfo.MyPostgreConnetionsInfoTest;
import GuttenBase_Examples.connInfo.MySqlConnectionsInfoTest;
import GuttenBase_Examples.mapping.CustomColumnRenameName;
import GuttenBase_Examples.mapping.CustomTableNameFilter;

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

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);

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


