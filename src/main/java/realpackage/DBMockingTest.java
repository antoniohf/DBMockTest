package realpackage;

import static java.lang.System.out;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.tools.jdbc.MockConnection;

public class DBMockingTest {
    public static void main(String[] args) throws Exception {
        
        // FileDatabase provider
//        InputStream resource = DBMockingTest.class.getResourceAsStream("mocking.txt");
//        System.out.println(resource);
//        MockDataProvider db = new MockFileDatabase(resource);
        
        MyProvider db = new MyProvider();
        
        try(Connection c = new MockConnection(db); Statement s = c.createStatement();)
        {
            out.println("Actors:");
            out.println("-------");
            out.println();
            out.println("Actors and their films:");
            out.println("-----------------------");
            try(ResultSet rs = s.executeQuery("select first_name, last_name from actor");)
            { 
                    while (rs.next())
                        out.println(rs.getString("first_name") + " " + rs.getString(2));
            }
            try(ResultSet rs = s.executeQuery("select first_name, last_name, count(*) from actor join film_actor using (actor_id) group by actor_id, first_name, last_name order by count(*) desc");)
            { 
                while (rs.next())
                    out.println(rs.getString(1) + " " + rs.getString(2) + " ("
                            + rs.getInt(3) + ")");
            }
            try
            { 
                s.executeQuery("insert into table values(1,2,3)");
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
            try(ResultSet rs = s.executeQuery("select first_name, last_name, count(*) from actor join film_actor using (actor_id) group by actor_id, first_name, last_name order by count(*) desc");)
            { 
                while (rs.next())
                    out.println(rs.getString(1) + " " + rs.getString(2) + " ("
                            + rs.getInt(3) + ")");
            }
        }
    }
    
}
