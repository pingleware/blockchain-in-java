import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static Connection connection = null;
    public static ResultSet rs = null;


    /**
     * 
     * @param sql the database query
     * @return ResultSet make sure you close after getting reuslts via rs.close()
     */
    public static ResultSet query(String sql) {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:blockchain.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            rs = statement.executeQuery(sql);
            return rs;
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void insert(String sql) {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:blockchain.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
    
            statement.executeUpdate(sql);
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
}