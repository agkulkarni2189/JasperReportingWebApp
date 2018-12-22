package UtilityLibrary;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionUtility {
	private Connection DBConnection=null;
	public DBConnectionUtility() {
		// TODO Auto-generated constructor stub
	}
	
	public DBConnectionUtility(String DriverName, String ConnectionURL, String UserName, String Password) throws ClassNotFoundException, SQLException
	{
		Class.forName(DriverName);
		DBConnection = DriverManager.getConnection(ConnectionURL, UserName, Password);
	}
	
	public ResultSet getRecordsFromDB(String SqlQuery) throws SQLException
	{
		ResultSet QueryResult = null;
		
		if(DBConnection != null)
		{
			Statement QueryStatement = DBConnection.createStatement();
			QueryResult = QueryStatement.executeQuery(SqlQuery);
		}
		
		return QueryResult;
	}
}
