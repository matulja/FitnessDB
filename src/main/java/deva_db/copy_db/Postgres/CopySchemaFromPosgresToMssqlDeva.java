package deva_db.copy_db.Postgres;

import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerBatch;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import deva_db.connInfo.MSSQLConnectionsInfoDeva;
import deva_db.connInfo.PostgresConnetionsInfoDeva;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by mfehler on 23.05.17.
 */
public class CopySchemaFromPosgresToMssqlDeva {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        connectorRepository.addConnectionInfo(SOURCE, new PostgresConnetionsInfoDeva());
        connectorRepository.addConnectionInfo(TARGET, new MSSQLConnectionsInfoDeva());

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        connectorRepository.addConnectorHint(TARGET, new NumberOfRowsPerBatchHint() {
            @Override
            public NumberOfRowsPerBatch getValue() {
                return new NumberOfRowsPerBatch() {
                    @Override
                    public int getNumberOfRowsPerBatch(TableMetaData targetTableMetaData) {
                        return 1000;
                    }

                    @Override
                    public boolean useMultipleValuesClauses(TableMetaData targetTableMetaData) {
                        return false;
                    }
                };
            }
        });


       /* connectorRepository.addConnectorHint(SOURCE, new CustomTableNameFilterDeva());
        connectorRepository.addConnectorHint(TARGET, new CustomTableNameFilterDeva());

        //add MappingTable  --> rename tables
        connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return new CustomTableRenameName()
                        .addReplacement("deva_artikel","abc_deva_artikel")
                        .addReplacement("deva_benutzer", "abc_deva_benutzer")
                        .addReplacement("deva_firma", "abc_deva_firma");
            }
        });

        //add MappingColumn  --> rename columns
        connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return new CustomColumnRenameName()
                        .addReplacement("name", "id_name")
                        .addReplacement("username", "id_username")
                        .addReplacement("ampel_status", "id_ampel_status")
                        .addReplacement("artikelprozess_status", "id_artikelprozess_status");
            }
        });

*/
        //add  ColumnType  --> replace columnType
        connectorRepository.addConnectorHint(SOURCE, new ColumnTypeMapperHint() {
                    @Override
                    public ColumnTypeMapper getValue() {
                         return new DefaultColumnTypeMapper();
                    }
                });


        List<String> script = new CopySchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {System.out.println(s);}

        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("Schema Done");


        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("ISSUES : " + issues);

        try {

            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);


        } catch (SQLException se) {

            if (!issues.isSevere()) {

                int count = 1;
                while (se != null) {
                    System.out.println("SQLException " + count);
                    System.out.println("Code: " + se.getErrorCode());
                    System.out.println("SqlState: " + se.getSQLState());
                    System.out.println("Error Message: " + se.getMessage());
                    se = se.getNextException();
                    count++;

                    //new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);

                }

            }

        }

        System.out.println("CopyData Done !!!");
    }
}




