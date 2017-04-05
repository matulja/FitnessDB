package fitness_db;

import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.*;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateSchemaTool;

import java.sql.*;
import java.util.List;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyFitnessDBFromSqlToPostgre {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    private static String columns= "";
    private static String table_name= "";
    private static String columns_name= "";
    private static String foreign_key= "";
    private static String foreign_name= "";


    private static ColumnMetaData columns_refer;
    private static List<ColumnMetaData> columns_primary;
    private static String columns_index;
    private static int columns_type= 0;
    private static int columns_row= 0;
    private static int columns_count= 0;
    private static int getColumns_row= 0;



    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        //to delete Tables
        connectorRepository.addTargetDatabaseConfiguration(DatabaseType.POSTGRESQL, new GenericTargetDatabaseConfiguration(connectorRepository));

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //get Source Name, get Matadata
        DatabaseMetaData source = connectorRepository.getDatabaseMetaData(SOURCE);
        DatabaseMetaData target = connectorRepository.getDatabaseMetaData(TARGET);


        System.out.println("SourceDB"+ source.getDatabaseName());
        System.out.println("TargetDB" + target.getDatabaseName());


        //delete the tables
        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        //getMetadata for source
        System.out.println("Source Schema " +source.getSchema());

        //copy only Schema
        new CreateSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);

        //get the Script Data
        List<String> script = new CreateSchemaTool(connectorRepository).createDDLScript(SOURCE, "foo");

        for (String s : script) {
            System.out.println(s);
        }


        //copy Tables
        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,TARGET);


        //TableMetadata
        List<TableMetaData> sourceTableMetaData= connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData();

        for (int index=0; index>sourceTableMetaData.size(); index++)
        {
            table_name=sourceTableMetaData.get(index).getTableName();
        }

        //columns = sourceTableMetaData.
        //columns_row= sourceTableMetaData.get(1).getRowCount();
        //table_name= sourceTableMetaData.get(1).getTableName();
        //columns_count= sourceTableMetaData.get(1).getColumnCount();


        System.out.println("Table Name for  : " + table_name);
       // System.out.println("Columns Row for : " + columns_row);
       // System.out.println("Columns Count for : " + columns_count);


        //ColumnMetaData

        //ColumnMetaData sourceColumnMetaData= connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData().get(0).getColumnMetaData().get(0);
        //ColumnMetaData targetColumnMetaData= connectorRepository.getDatabaseMetaData(TARGET).getTableMetaData().get(0).getColumnMetaData().get(0);


        columns_name=sourceTableMetaData.get(1).getColumnMetaData().get(0).getColumnName();
        columns_type=sourceTableMetaData.get(1).getColumnMetaData().get(0).getColumnType();
        columns_primary= sourceTableMetaData.get(1).getPrimaryKeyColumns();
        columns_refer= sourceTableMetaData.get(1).getColumnMetaData().get(0).getReferencedColumn();
        columns_index= sourceTableMetaData.get(1).getIndexesForColumn(columns_primary.get(0)).get(0).getIndexName();

        System.out.println("Columns Name for Index 0: " +columns_name);
        System.out.println("Columns Type for: " +columns_type);
        System.out.println("Columns Refer for: " +columns_refer);
        System.out.println("Columns Primary Columns for: " +columns_primary);
        System.out.println("Columns Index for: " +columns_index);


        //get ForeignKeyMeta Data --???

        List<ForeignKeyMetaData> sourceForeignKeyMetaData= connectorRepository.getDatabaseMetaData(SOURCE).
                getTableMetaData().get(1).getExportedForeignKeys();

        foreign_key=sourceForeignKeyMetaData.toString();
        System.out.println("Foreign key for table: " + foreign_key) ;

        //foreign_name=sourceForeignKeyMetaData.get(0).getForeignKeyName();
        //System.out.println("Foreign key for table name: " + foreign_name) ;


        final int numberOfRowsPerBatch = 1;
        final int sourceRowCount = columns_row;
        final int numberOfBatches = sourceRowCount / numberOfRowsPerBatch;
        System.out.println("numberOfBatches: " + numberOfBatches);

        // getConnector Hint
        System.out.println("Connectors Ids: " +
                connectorRepository.getConnectorIds());



    }
}
