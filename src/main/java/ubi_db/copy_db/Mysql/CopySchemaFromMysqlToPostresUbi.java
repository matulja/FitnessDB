package ubi_db.copy_db.Mysql;


import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DropTablesTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import ubi_db.connInfo.MysqlConnetionsInfoUbi;
import ubi_db.connInfo.PostgresConnetionsInfoUbi;

import java.util.List;


/**
 * Created by mfehler on 26.05.17.
 */
public class CopySchemaFromMysqlToPostresUbi {


    public static final String SOURCE = "source";
    public static final String TARGET = "target";


    public static void main(final String[] args) throws Exception {

        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();

        connectorRepository.addConnectionInfo(SOURCE, new MysqlConnetionsInfoUbi());
        connectorRepository.addConnectionInfo(TARGET, new PostgresConnetionsInfoUbi());

        DropTablesTool dropTablesTool = new DropTablesTool(connectorRepository);
        dropTablesTool.dropIndexes(TARGET);
        dropTablesTool.dropForeignKeys(TARGET);
        dropTablesTool.dropTables(TARGET);


        connectorRepository.addConnectorHint(SOURCE, new ColumnTypeMapperHint() {
                    @Override
                    public ColumnTypeMapper getValue() {
                         return new DefaultColumnTypeMapper();
                    }
                });


        List<String> script = new CopySchemaTool(connectorRepository).createDDLScript(SOURCE, TARGET);
        for (String s : script) {System.out.println(s);}

        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        System.out.println("------------------------------------ SCHEMA DONE ------------------------------------ ");

        new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE, TARGET);
        System.out.println("------------------------------------ COPY DATA ------------------------------------ ");
    }
}




