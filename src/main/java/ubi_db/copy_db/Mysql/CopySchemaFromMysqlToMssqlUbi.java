package ubi_db.copy_db.Mysql;

import _GuttenBase_Examples.mapping.CustomColumnRenameName;
import _GuttenBase_Examples.mapping.CustomTableRenameName;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerBatch;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import ubi_db.connInfo.MSSQLConnectionsInfoUbi;
import ubi_db.connInfo.MysqlConnetionsInfoUbi;
import ubi_db.mapping.CustomTableNameFilterUbi;

import java.sql.SQLException;


/**
 * Created by mfehler on 26.05.17.
 */
public class CopySchemaFromMysqlToMssqlUbi {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        connectorRepository.addConnectionInfo(SOURCE, new MysqlConnetionsInfoUbi());
        connectorRepository.addConnectionInfo(TARGET, new MSSQLConnectionsInfoUbi());

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


        //add _Mapping TableFilter
         connectorRepository.addConnectorHint(SOURCE, new CustomTableNameFilterUbi());
         connectorRepository.addConnectorHint(TARGET, new CustomTableNameFilterUbi());


        //add MappingTable  --> rename tables
        connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return new CustomTableRenameName()
                        .addReplacement("baulichermangel","tab_baulichtermangel")
                        .addReplacement("bauwerk", "tab_bauwerk")
                        .addReplacement("bauwerkhinweis", "tab_bauwerk_hinweis");
            }
        });

        //add MappingColumn  --> rename columns
        connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return new CustomColumnRenameName()
                        .addReplacement("MANGEL", "COL_MANGEL")
                        .addReplacement("FESTSTELLUNG", "COL_FESTSTELLUNG")
                        .addReplacement("BEZIRK", "COL_BEZIRK")
                        .addReplacement("LAGE", "COL_LAGE");
            }
        });



        connectorRepository.addConnectorHint(SOURCE, new ColumnTypeMapperHint() {
                    @Override
                    public ColumnTypeMapper getValue() {
                         return new DefaultColumnTypeMapper();
                    }
                });


        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("------------------------------------ SCHEMA DONE ------------------------------------ ");

        SchemaCompatibilityIssues issues = new SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        System.out.println("------------------------------------ COPY DATA ------------------------------------ ");

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

                }

            }

        }

        System.out.println("CopyData Done !!!");
    }
}







