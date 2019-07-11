import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlMasterSlaveDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hux
 * @datetime 2019-07-11 19:54
 * @description ��ȡ��д��������ݿ�Connection��
 */
class DbKit {

    /***
     * ��ȡ��д�����Connection��ͨ��yaml�ļ�
     * @return Connection
     */
    static Connection getConnectionByYaml() {
        Connection connection = null;
        try {
            //sharding-jdbc yaml�ļ�����
            final File file = new File((System.getProperty("user.dir") + "\\src\\main\\resources\\sharding-jdbc.yml"));
            //��������Դ
            final DataSource dataSource = YamlMasterSlaveDataSourceFactory.createDataSource(file);
            //������Դ�л�ȡConnection
            connection = dataSource.getConnection();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * ��ȡ��д�����Connection��Ӳ���뷽ʽ�����Ƽ�ʹ�ã�
     *
     * @return Connection
     */
    static Connection getConnectionByHardCode() {
        //�������Դ��Map
        Map<String, DataSource> dataSourceMap = new HashMap<>(3);
        //������Դ��д��
        HikariConfig hikariMaster = new HikariConfig();
        hikariMaster.setJdbcUrl("jdbc:mysql://127.0.0.1/t_master?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        hikariMaster.setUsername("root");
        hikariMaster.setPassword("tiger123");
        hikariMaster.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //������Դ������
        HikariConfig hikariSlave = new HikariConfig();
        hikariMaster.setJdbcUrl("jdbc:mysql://127.0.0.1/t_master?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        hikariSlave.setUsername("root");
        hikariSlave.setPassword("tiger123");
        hikariSlave.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //����д������Դ����ӵ�map��
        dataSourceMap.put("ds_slave", new HikariDataSource(hikariSlave));
        dataSourceMap.put("ds_master", new HikariDataSource(hikariMaster));
        //���ö�д�������
        MasterSlaveRuleConfiguration msrc = new MasterSlaveRuleConfiguration("ds_read_write", "ds_master", Collections.singletonList("ds_slave"));
        Connection connection = null;
        try {
            //��������Դ
            DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, msrc, new Properties());
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("��������Դʧ��");
        }
        return connection;
    }

}
