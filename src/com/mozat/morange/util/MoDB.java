package com.mozat.morange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;


/* 
 *Encapsulate database driver and pooling strategy
 */

public class MoDB {
	
	String connectionURL = "";
	
	public MoDB (String driver,String url,int maxActivity) throws Exception
	{
		Class.forName(driver);		
		connectionURL = url;
		
		ObjectPool connectionPool = new GenericObjectPool(null,maxActivity);		
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionURL,null);
		new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
		
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
        PoolingDriver poolingDriver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");        
        poolingDriver.registerPool(connectionURL,connectionPool);
        
	}
	
	public MoDB (String driver,String url) throws Exception
	{
		Class.forName(driver);		
		connectionURL = url;
		
		ObjectPool connectionPool = new GenericObjectPool(null,200);		
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionURL,null);
		new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
		
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
        PoolingDriver poolingDriver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");        
        poolingDriver.registerPool(connectionURL,connectionPool);
        
	}
	
	public Connection getConnection() throws Exception
	{
		DriverManager.setLoginTimeout(30);
		Connection conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:"+connectionURL);		
		return conn;
	}	
	
	
/*usage test: change database.dbURL in systemproperties to 
 *jdbc:sqlserver://192.168.1.12;databaseName=monet;user=sa;password=tankeshi
 */
	public static void main(String[] args) {
	//	String dbURL = "jdbc:sqlserver://192.168.1.12;databaseName=monet;user=sa;password=tankeshi";
	//	String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"                      ;
		
		String dbURL = "jdbc:jtds:sqlserver://192.168.1.12/monet;user=sa;password=tankeshi";
		String dbDriver = "net.sourceforge.jtds.jdbc.Driver";
		
		
		MoDB monetDB = null;
		Connection con = null;
      	Statement stmt = null;
      	ResultSet rs = null;
      	long startTime;
      	long stopTime ;
      	long runTime;
      	
      	//test query
      	startTime = System.currentTimeMillis();
      	try{
      	
      		monetDB = new MoDB(dbDriver,dbURL);		
      	}catch(Exception e){
      	}
      	for (int i=0;i<1000;i++){
      	
			try{
				
				
	      		
	      		con = monetDB.getConnection();
	         
	         	String SQL = "SELECT * FROM dbo.domain_info;";
	         	stmt = con.createStatement();
	         	rs = stmt.executeQuery(SQL);  
	         	   
	         	while (rs.next()) {
	         	//   System.out.println(rs.getString("domain_name") + " : " + rs.getInt("domain_id"));
	         	}
	
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
	         	if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	         	if (con != null) try { con.close(); } catch(Exception e) {}
	      	}
	    }
	    
	    stopTime = System.currentTimeMillis();
		runTime = stopTime - startTime;
		System.out.println("QUERY TIME TO EXECUTE: " + runTime);
	    
      	
      	//test query
      	startTime = System.currentTimeMillis();
      	
      	for (int i=0;i<1000;i++){
      	
	      	PreparedStatement prestmt = null;
			try{			
				      		
	      		con = monetDB.getConnection();
	         
	         	String SQL = "INSERT INTO dbo.domain_info (domain_name, password,domain_id,description) VALUES (?,?,?,?);";
	         	
	         	prestmt = con.prepareStatement(SQL);
	         	prestmt.setString(1,"temp_domain");
	         	prestmt.setString(2,"pwd");
	         	prestmt.setInt(3,4);
	         	prestmt.setString(4,"temp");
			}catch(Exception e){
				e.printStackTrace();
			}finally {			
	         	if (prestmt != null) try { prestmt.close(); } catch(Exception e) {}
	         	if (con != null) try { con.close(); } catch(Exception e) {}
	      	}
	      	
	      	try{			
				      		
	      		con = monetDB.getConnection();
	         
	         	String SQL = "delete from dbo.domain_info where domain_id=?;";
	         	
	         	prestmt = con.prepareStatement(SQL);
	         	
	         	prestmt.setInt(1,4);         	
			}catch(Exception e){
				e.printStackTrace();
			}finally {			
	         	if (prestmt != null) try { prestmt.close(); } catch(Exception e) {}
	         	if (con != null) try { con.close(); } catch(Exception e) {}
	      	}
      	
      	}
		stopTime = System.currentTimeMillis();
		runTime = stopTime - startTime;
		System.out.println("UPDATE TIME TO EXECUTE: " + runTime);
		
	}
	
	
}
