import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author hux
 * @datetime 2019-07-11 18:08
 * @description ������
 */
public class MainTest {

    /**
     * ����д������
     */
    private static void writeTest(Connection connection) throws SQLException {
        String writeSql = "insert into test values(100);";
        PreparedStatement ps = connection.prepareStatement(writeSql);
        //ִ��sql
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    /**
     * ���Զ�ȡ����
     */
    private static void readTest(Connection connection) throws SQLException {
        String readSql = "select * from test;";
        PreparedStatement ps = connection.prepareStatement(readSql);
        //��ѯ���ؽ����
        ResultSet resultSet = ps.executeQuery();
        //���ݲ�ѯ��������
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt(1));
        }
        ps.close();
        resultSet.close();
        connection.close();
    }

    public static void main(String[] args) throws SQLException {
        /*
        ����2�ַ�ʽ�����ȡ��Connection��һ�㶼���ö�ȡYaml�ļ��ķ�ʽ
         */
        //��ȡYaml�ļ���ʽ��ȡ���ݿ�Connection�����ã�
        final Connection yamlConnection = DbKit.getConnectionByYaml();
        //Ӳ���뷽ʽ��ȡ���ݿ�Connection�������ã�
        final Connection hardCodeConnection = DbKit.getConnectionByHardCode();
        //���Զ����������еĶ����������slave���ж�ȡ�����ö��slave
        readTest(yamlConnection);
        //����д���������е�д��������д��master���ݿ�
        writeTest(yamlConnection);
    }
}
