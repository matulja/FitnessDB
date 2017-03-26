package fitness_db;

import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateSchemaTool;

import java.sql.SQLException;


/**
 * Created by mfehler on 21.03.17.
 */
public class CopyFitnessDBFomSqlToPostgre {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void main(final String[] args) throws SQLException {

        //Repository initialisieren
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        //connectorRepository.addTargetDatabaseConfiguration(DatabaseType.POSTGRESQL, new GenericTargetDatabaseConfiguration(connectorRepository));

        //add Source & Target ConnectionInfo
        connectorRepository.addConnectionInfo(SOURCE, new MySqlConnectionsInfo());
        connectorRepository.addConnectionInfo(TARGET, new MyPostgreConnetionsInfo());

        //get Source Name, get Matadata
        DatabaseMetaData source = connectorRepository.getDatabaseMetaData(SOURCE);
        DatabaseMetaData target = connectorRepository.getDatabaseMetaData(TARGET);

        System.out.println(source.getDatabaseName());
        System.out.println(target.getDatabaseName());


        //delete the tables

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        //getMetadata for source

        System.out.println("Schema" +source.getSchema());
        System.out.println("Table Name" + source.getTableMetaData().toString());

        //Metadata und ColumnData deklarieren

        TableMetaData sourceData= (TableMetaData) connectorRepository.getDatabaseMetaData(SOURCE);
        sourceData.getRowCount();

        ColumnMetaData sourceColumn= (ColumnMetaData) connectorRepository.getDatabaseMetaData(SOURCE);
        sourceColumn.getColumnType();

        System.out.println("Name Column"+ sourceColumn.getColumnName() );
        System.out.println("Type Column"+ sourceColumn.getColumnType() );
        System.out.println("Anzahl Zeilen" +  sourceData.getRowCount());


        final int numberOfRowsPerBatch = 1;
        final int sourceRowCount = sourceData.getRowCount();
        final int numberOfBatches = sourceRowCount / numberOfRowsPerBatch;

        System.out.println("numberOfBatches" + numberOfBatches);
        System.out.println("IndexMetaData" + sourceData.getIndexesForColumn(sourceColumn) );
        System.out.println("PrimaryKeyColumn" + sourceData.getPrimaryKeyColumns());

        // getConnector Hint

        System.out.println("Connectors Ids" +
                connectorRepository.getConnectorIds());


        final TableNameMapper tableNameMapper = connectorRepository.getConnectorHint(TARGET, TableNameMapper.class).getValue();
        System.out.println("Connector Hint for Target tableNameMapper " + tableNameMapper);



        //copy only Schema
        new CreateSchemaTool(connectorRepository).copySchema(SOURCE, TARGET);

        //copy Tables
        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,TARGET);

        //add Connection Hint für Umbenennung

        /*connectorRepository.addConnectorHint(TARGET, new MappingColumnData());*/







    }
}
