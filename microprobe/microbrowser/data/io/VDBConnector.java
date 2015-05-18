package microbrowser.data.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import microbrowser.VisualDBConfig;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class VDBConnector {

    // logger
    private static final Logger logger = Logger.getLogger(VDBConnector.class.getName());
    
	public static Connection getConnection() throws SQLException {
//
//	    Connection connection = null;
//		String driver 		= "org.apache.derby.jdbc.ClientDriver",
//				url 		= "jdbc:derby://localhost:1527/VISUALDB;create=false",
//				//url = "jdbc:derby:/Users/faz23/projects/websphere/usr/servers/microbrowser.0.1/VISUALDB;create=true",
///*				driver = "com.mysql.jdbc.Driver",
//				url = "jdbc:mysql://mysql.cs.pitt.edu:3306/ezegarraDB",
//				user	= "ezegarra",
//				password = "peru1995";
//				*/
//				user 		= "user",
//				password 	= "password";
//
//		try {
//	        //ctx = new javax.naming.InitialContext();
//			//Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
//			Class.forName(driver).newInstance();
//			
//	        //ds = (javax.sql.DataSource) ctx.lookup("jdbc/VISUALDB");
//	        //connection = ds.getConnection();
//			//connection = DriverManager.getConnection("jdbc:derby:/Users/faz23/projects/WEBSPHERE/usr/servers/microbrowser.0.1/VISUALDB;create=true");
//			//connection = DriverManager.getConnection("jdbc:derby:C:/WebSphere/Liberty85/usr/servers/microbrowser.0.1/VISUALDB;create=true");
//			connection = DriverManager.getConnection(url, user, password);
//			
//	    }  catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}       

	    return getDerbyConnection();
	}
	
	/**
	 * Get a connection the the MySQL database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getMySqlConnection() throws SQLException {
		return getConnection(VisualDBConfig.DATABASE_DRIVER_MYSQL, VisualDBConfig.DATABASE_URL_MYSQL, VisualDBConfig.DATABASE_USER_MYSQL, VisualDBConfig.DATABASE_PASSWORD_MYSQL);
	}
	
	/**
	 * Get a connection to the derby database
	 * 
	 */
	public static Connection getDerbyConnection() throws SQLException {
	    return getConnection(VisualDBConfig.DATABASE_DRIVER_DERBY, VisualDBConfig.DATABASE_URL_DERBY, VisualDBConfig.DATABASE_USER_DERBY, VisualDBConfig.DATABASE_PASSWORD_DERBY);
	}
	
	public static Connection getConnection(String driver, String url, String user, String password) throws SQLException {
	    Connection connection = null;
		//String driver 		= "org.apache.derby.jdbc.ClientDriver",
		//		url 		= "jdbc:derby://localhost:1527/VISUALDB;create=false",
				//url = "jdbc:derby:/Users/faz23/projects/websphere/usr/servers/microbrowser.0.1/VISUALDB;create=true",
/*				driver = "com.mysql.jdbc.Driver",
				url = "jdbc:mysql://mysql.cs.pitt.edu:3306/ezegarraDB",
				user	= "ezegarra",
				password = "peru1995";
				*/
		//		user 		= "user",
		//		password 	= "password";

		try {
	        //ctx = new javax.naming.InitialContext();
			//Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			Class.forName(driver).newInstance();
			
	        //ds = (javax.sql.DataSource) ctx.lookup("jdbc/VISUALDB");
	        //connection = ds.getConnection();
			//connection = DriverManager.getConnection("jdbc:derby:/Users/faz23/projects/WEBSPHERE/usr/servers/microbrowser.0.1/VISUALDB;create=true");
			//connection = DriverManager.getConnection("jdbc:derby:C:/WebSphere/Liberty85/usr/servers/microbrowser.0.1/VISUALDB;create=true");
			connection = DriverManager.getConnection(url, user, password);
			
	    }  catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}       

	    return connection;
	}
	
	public static DatabaseDataSource getDataSourceFromDerbyNetworkConnection() {
		String driver 		= "org.apache.derby.jdbc.ClientDriver",
				url 		= "jdbc:derby://localhost:1527/MICROBROWSERDB;create=false",
				//url = "jdbc:derby:/Users/faz23/projects/websphere/usr/servers/microbrowser.0.1/VISUALDB;create=true",
				user 		= "user",
				password 	= "password";
		
		DatabaseDataSource databaseDataSource = null;
		
		try {
			databaseDataSource = ConnectionFactory.getDatabaseConnection(driver, url, user, password);
			//logger.info("connection = " + databaseDataSource);
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return databaseDataSource;
	}
	
	public static void createPattern(int id, String name, String description, String solution) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO VISUAL.PATTERNS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Timestamp _date = new Timestamp(System.currentTimeMillis());
		try {
			conn = getConnection();

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, description);
			pstmt.setString(4, solution);
			pstmt.setTimestamp(5, _date);
			pstmt.setInt(6, 2);
			pstmt.setInt(7, 2);
			pstmt.setTimestamp(8, _date);
			pstmt.setString(9, "");
			System.out.println("Created " + pstmt.executeUpdate() + " pattern.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
