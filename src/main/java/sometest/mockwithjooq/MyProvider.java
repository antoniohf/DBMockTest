package sometest.mockwithjooq;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

public class MyProvider implements MockDataProvider {

    private static final String QUERY_1 = "select first_name, last_name from actor";
    private static final String QUERY_2 = "select first_name, last_name, count(*) from actor join film_actor using (actor_id) group by actor_id, first_name, last_name order by count(*) desc";
    
    private final Map<String, List<MockResult>> queryToMockResults = new HashMap<>();
    
    public MyProvider()
    {
        DSLContext create = DSL.using(SQLDialect.DEFAULT);
        
        List<MockResult> mockResults = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        Result<Record> result;
        Record rec;
        
        // QUERY_1
        fields.add(field(name("first_name"), String.class));
        fields.add(field(name("last_name"), String.class));
        result = create.newResult( fields.toArray( new Field[fields.size()]) );
        rec = create.newRecord(fields.toArray( new Field[fields.size()]));
        rec.setValue(fields.get(0), "GINA");
        rec.setValue(fields.get(1), "Orwell");
        result.add(rec);
        mockResults.add(new MockResult(1, result));
        queryToMockResults.put(QUERY_1, mockResults);
        
        // QUERY_2
        mockResults = new ArrayList<>();
        fields = new ArrayList<>();
        fields.add(field(name("first_name"), String.class));
        fields.add(field(name("last_name"), String.class));
        fields.add(field(name("count"), Integer.class));
        result = create.newResult( fields.toArray( new Field[fields.size()]) );
        rec = create.newRecord(fields.toArray( new Field[fields.size()]));
        rec.setValue(fields.get(0), "GINA");
        rec.setValue(fields.get(1), "Orwell");
        rec.setValue(fields.get(2), 3);
        result.add(rec);
        mockResults.add(new MockResult(1, result));
        
        result = create.newResult( fields.toArray( new Field[fields.size()]) );
        rec = create.newRecord(fields.toArray( new Field[fields.size()]));
        rec.setValue(fields.get(0), "FIRSTNAME");
        rec.setValue(fields.get(1), "LASTNAME");
        rec.setValue(fields.get(2), 3);
        result.add(rec);
        mockResults.add(new MockResult(1, result));
        queryToMockResults.put(QUERY_2, mockResults);
        
    }
    
    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {

        DSLContext create = DSL.using(SQLDialect.DEFAULT);
        MockResult[] mock = new MockResult[1];
        String sql = ctx.sql();
        System.out.println("----> Executing query: " + sql);
        // First check for mocked queries and return their resultset
        if (queryToMockResults.containsKey(sql))
        {
            List<MockResult> mockResults = queryToMockResults.get(sql);
            MockResult res = mockResults.get(0);
            if(mockResults.size() > 1)
            {
                mockResults.remove(0);
            }
            return new MockResult[]{res};
        }
        else if(sql.startsWith("insert into"))
        {
            // inserts will throw an SQLException - can define the error code to test duplicatekey, for example
            throw new SQLException("REASON", "23");
        }
        else
        {
            // for any non mocked query, result an empty resultset
            List<Field<String>> fields = new ArrayList<>();
            fields.add(field(name("DUMMY"), String.class));
            Result<Record> result = create.newResult( fields.toArray( new Field[fields.size()]) );
            mock[0] = new MockResult(0, result);
            return mock;
        }
    }

}
