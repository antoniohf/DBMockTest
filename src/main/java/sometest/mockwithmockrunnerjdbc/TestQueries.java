package sometest.mockwithmockrunnerjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestQueries {

    protected static final String QUERY_PREPARED_STATEMENT = "select balance from account where id = ?";
    protected static final String QUERY_INSERT = "insert into account (?,?,?)";
    
    private Connection connection;
    
    public void connect() throws SQLException
    {
        disconnect();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
        connection.setAutoCommit(false);
    }
    
    public void disconnect() throws SQLException
    {
        if(null != connection)
        {
            connection.close();
            connection = null;
        }        
    }
    
    public void executeQueries()
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_PREPARED_STATEMENT);)
        {
            System.out.println("Executing query: " + QUERY_PREPARED_STATEMENT + " , with parameter: 1");
            preparedStatement.setInt(1, 1);
            try(ResultSet rs = preparedStatement.executeQuery();)
            {
                System.out.println("Results found: ");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "\t" + rs.getInt("balance") + "\t" + rs.getString("notes"));
                }
            }
            System.out.println();
            System.out.println("Executing query: " + QUERY_PREPARED_STATEMENT + " , with parameter: 2");
            preparedStatement.setInt(1, 2);
            try(ResultSet rs = preparedStatement.executeQuery();)
            {
                System.out.println("Results found: ");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "\t" + rs.getInt("balance") + "\t" + rs.getString("notes"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try(PreparedStatement ps = connection.prepareStatement(QUERY_INSERT))
        {
            System.out.println();
            System.out.println("Inserting row with an already existing id");
            ps.setInt(1, 1);
            ps.setInt(2, 1000000);
            ps.setString(3, "");
            ps.executeUpdate();
        } catch (SQLException e) {
            if( "HY000".equals(e.getSQLState()) )
            {
                System.out.println("Duplicatekey error! This id already exists!");
            }
            System.out.println("Exception: " + e);
        }

        try(PreparedStatement ps = connection.prepareStatement(QUERY_INSERT))
        {
            System.out.println();
            System.out.println("Inserting new row");
            ps.setInt(1, 3);
            ps.setInt(2, 1000000);
            ps.setString(3, "Yet another millionaire!");
            int numRowsInserted = ps.executeUpdate();
            System.out.println(String.format("Inserted: %d row(s)", numRowsInserted));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
