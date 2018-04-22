package sometest.mockwithjooq;

import java.io.InputStream;

import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockFileDatabase;

public class DBMockingTest {
    public static void main(String[] args) throws Exception {
        
        TestQueries tq = new TestQueries();
        
        // FileDatabase provider
        System.out.println("----------------------");
        System.out.println("--- Running test with data from file ----");
        System.out.println("----------------------");
        InputStream resource = DBMockingTest.class.getResourceAsStream("mocking.txt");
        System.out.println(resource);
        MockDataProvider db = new MockFileDatabase(resource);
        tq.executeActorsQueries(db);
        System.out.println("----------------------");
        
        // Custom Provider        
        System.out.println("----------------------");
        System.out.println("--- Running test with a Custom Provider ----");
        System.out.println("----------------------");
        db = new MyProvider();
        tq.executeActorsQueries(db);
        System.out.println("----------------------");
        
    }
    
}
