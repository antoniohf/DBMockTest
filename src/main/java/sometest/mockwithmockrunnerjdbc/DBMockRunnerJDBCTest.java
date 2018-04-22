package sometest.mockwithmockrunnerjdbc;

import java.sql.SQLException;

import org.junit.Test;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.ParameterSets;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

public class DBMockRunnerJDBCTest extends BasicJDBCTestCaseAdapter {

    public void prepareMocks()
    {
        MockConnection connection = getJDBCMockObjectFactory().getMockConnection();
        
        PreparedStatementResultSetHandler statementHandler = connection.getPreparedStatementResultSetHandler();
        statementHandler.setExactMatch(true);
        
        MockResultSet resultset = statementHandler.createResultSet();
        resultset.addColumn("id");
        resultset.addColumn("balance");
        resultset.addColumn("notes");
        resultset.addRow(new Object[] {1,200,"I need more money!"});
        statementHandler.prepareResultSet(TestQueries.QUERY_PREPARED_STATEMENT, resultset, new Object[] { 1 });
        
        resultset = statementHandler.createResultSet();
        resultset.addColumn("id");
        resultset.addColumn("balance");
        resultset.addColumn("notes");
        resultset.addRow(new Object[] {2,1000000,"I'm a millionaire"});
        statementHandler.prepareResultSet(TestQueries.QUERY_PREPARED_STATEMENT, resultset, new Object[] { 2 });
        
        statementHandler.prepareThrowsSQLException(TestQueries.QUERY_INSERT, new SQLException("1023", "HY000" ), new Object[] { 1, 1000000, "" } );
        
        statementHandler.prepareUpdateCount(TestQueries.QUERY_INSERT, 1,  new Object[] { 3, 1000000, "Yet another millionaire!" });
    }
    
    @Test
    public void runTest() throws SQLException
    {
        prepareMocks();
        TestQueries tq = new TestQueries();
        tq.connect();
        tq.executeQueries();
        tq.disconnect();
        
        System.out.println("------------------------");
        System.out.println("All executed queries: ");
        for( String sql : getExecutedSQLStatements() )
        {
            System.out.println("-->"+sql+"<--");
            StringBuilder sb = new StringBuilder("\twith params: ");
            ParameterSets params = getExecutedSQLStatementParameterSets(sql);
            for(int i=0;i<params.getNumberParameterSets();i++)
            {
                sb.append(params.getParameterSet(i));
                sb.append(",");
            }
            System.out.println(sb.toString());
        }
        System.out.println("------------------------");
        
        verifySQLStatementExecuted(TestQueries.QUERY_PREPARED_STATEMENT);
        verifySQLStatementExecuted(TestQueries.QUERY_INSERT);
        verifySQLStatementNotExecuted("select * from dummytable");
    }
    
}
