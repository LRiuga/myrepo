package com.mozat.morange.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.apache.log4j.Logger;



/*
 * A wrapper for JDBC
 * MoDBRW can redirect the database operation to master/slave database automatically,
 * if the slave database is specified.
 * it also wrap the query/update operation into a convenient way.
 */
public class MoDBRW {

	public static String DEFAULT_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

	int timeout = 30;

	static final Logger logger = Logger.getLogger(MoDBRW.class);
	static Random randomGenerator = new Random();

	//---------------------------------testing------------------------------
	static MoDB db;

	static void db_test_insert() throws Exception
	{}



	static void db_test_select(int i) throws Exception
	{
		Connection con = null;
	  	PreparedStatement stmt = null;
	  	ResultSet rs = null;
		try{
      		con = db.getConnection();

         	//String SQL = "exec get_user_email 10000";
      		String SQL = "select * from tt where f_int="+(10000+i);
      		//String SQL = "exec get_quota "+(10000+i);

         	stmt = con.prepareStatement(SQL);
         	rs = stmt.executeQuery();
         	while (rs.next()) {

         		print("f_str="+rs.getString("f_str"));
				print("f_int="+rs.getInt("f_int"));
				print("f_float="+rs.getFloat("f_float"));
				print("f_binary="+(rs.getBytes("f_binary")));
				print("f_datetime="+rs.getDate("f_datetime"));
				print("f_nvarchar_max="+rs.getString("f_nvarchar_max"));
				print("f_tinyint="+rs.getInt("f_tinyint"));
				print("f_nvarchar_50="+rs.getString("f_nvarchar_50"));
				print("f_smalldatetime="+rs.getDate("f_smalldatetime"));
				print("f_char_2="+rs.getString("f_char_2"));
				print("f_binary_16="+(rs.getBytes("f_binary_16")));
         	}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (rs != null) try { rs.close(); } catch(Exception e) {}
         	if (stmt != null) try { stmt.close(); } catch(Exception e) {}
         	if (con != null) try { con.close(); } catch(Exception e) {}
      	}
	}

	public static void main(String[] args)
	{}

	static void print(String str){
		//System.out.println(str);
	}

	static void setArgs(PreparedStatement stmt,int idx,Object arg) throws Exception
	{
		//logger.info(type+":"+arg);
		if(arg instanceof Integer){
			stmt.setInt(idx, (Integer)arg);
		}else if(arg instanceof Long){
			stmt.setLong(idx, (Long)arg);
		}else if(arg instanceof Byte){
			stmt.setByte(idx, (Byte)arg);
		}else if(arg instanceof Float){
			stmt.setFloat(idx, (Float)arg);
		}else if(arg instanceof Double){
			stmt.setDouble(idx, (Double)arg);
		}
		else if(arg instanceof byte[]){
			stmt.setBytes(idx, (byte[])arg);
		}else if(arg instanceof Boolean){
			stmt.setBoolean(idx, (Boolean)arg);
		}else if(arg instanceof String){
			stmt.setString(idx, (String)arg);
		}else if(arg instanceof java.util.Date){
			stmt.setTimestamp(idx, new java.sql.Timestamp(((java.util.Date)arg).getTime()));
		}
		else{
			stmt.setObject(idx, arg);
		}
	}
	
	MoDB[] readDBs =null;
	
	
	MoDB writeDB = null;

	public MoDBRW (String writeDbURL,String dbDriver) throws Exception{

		String[] readDbURLs = null;
		if(writeDbURL==null){
			logger.error("cannot find writeDBURL");
			throw new Exception("cannot find writeDBURL");
		}

		if(dbDriver==null || dbDriver.length()==0){
			dbDriver = DEFAULT_DRIVER;
		}

		writeDB = new MoDB(dbDriver,writeDbURL);

		if(readDbURLs==null || readDbURLs.length==0){
			readDBs = null;
		}
	}


	/*
	 * writeDbURL: the url of the master database
	 * readDbURLs: the urls of the slave databases, readDbURL can be same as writeDbURL. if readDbURLs is null,
	 *    all the read operation will be done at the master database
	 * dbDriver: the database driver. If null, set as DEFAULT_DRIVER
	 */
	public MoDBRW (String writeDbURL,String[] readDbURLs,String dbDriver) throws Exception{

		if(writeDbURL==null){
			logger.error("cannot find writeDBURL");
			throw new Exception("cannot find writeDBURL");
		}

		if(dbDriver==null || dbDriver.length()==0){
			dbDriver = DEFAULT_DRIVER;
		}
		writeDB = new MoDB(dbDriver,writeDbURL);

		if(readDbURLs==null || readDbURLs.length==0){
			readDBs = null;
		}else{
			readDBs = new MoDB[readDbURLs.length];
			for(int i=0;i<readDbURLs.length;i++){
				readDBs[i] = new MoDB(dbDriver,readDbURLs[i]);
			}
		}
	}
	public DBResultSet doExecSQLQuery(MoDB db,String sql,Object[] args) throws Exception
	{
		
		long st = System.currentTimeMillis();
		Connection con = null;
      	PreparedStatement stmt = null;
      	ResultSet rs = null;
		try{
			con = db.getConnection();
			con.setReadOnly(true);
         	stmt = con.prepareStatement(sql);
         	if(args!=null){
	         	for(int i=0;i<args.length;i++){
	         		MoDBRW.setArgs(stmt,i+1,args[i]);
	         	}
         	}
        	stmt.setQueryTimeout(this.timeout);
         	return new DBResultSet(stmt.executeQuery());

		}finally{
			if(rs!=null) try{rs.close();}catch(Exception e){rs=null;}
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.info("doExecSQLQuery: "+sql+"@"+db.connectionURL+" ="+(System.currentTimeMillis()-st));
		}
	}

	public DBResultSet execSQL(String sql,Object[] args) 
	{						
		MoDB db= this.getWriteDB();

		long st=System.currentTimeMillis();
		Connection con = null;
      	PreparedStatement stmt = null;
      	ResultSet rs = null;
		try{
			con = db.getConnection();
         	stmt = con.prepareStatement(sql);
         	if(args!=null){
	         	for(int i=0;i<args.length;i++){
	         		MoDBRW.setArgs(stmt,i+1,args[i]);
	         	}
         	}
        	stmt.setQueryTimeout(this.timeout);
        	Logger log = Logger.getLogger("sys");
        	log.info(stmt.getParameterMetaData().toString());
         	return new DBResultSet(stmt.executeQuery());

		}catch(Exception e){
			logger.error("execSQL error",e);
			return null;
		}finally{
		
			if(rs!=null) try{rs.close();}catch(Exception e){rs=null;}
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.info("execSQL: "+sql+" ="+(System.currentTimeMillis()-st));
		}
	}
	public DBResultSet execSQLQuery(String sql,Object[] args) 
	{		
		return execSQLQuery(sql,args,false);
	}


	public DBResultSet execSQLQuery(String sql,Object[] args,boolean isForceMaster) 
	{
		MoDB db;
		if(isForceMaster==false){
			int start = randomGenerator.nextInt();
			if (start<0) start = -start;
			start = start %readDBs.length;
			
			for(int i=start;i<readDBs.length;i++){
				db = readDBs[i];
				try{
					DBResultSet rs = doExecSQLQuery(db,sql,args);
					return rs;
				}catch(Exception e){
					logger.error("execSQLQuery db("+i+") error",e);
				}
			}
			
			for(int i=0;i<start;i++){
				db = readDBs[i];
				try{
					DBResultSet rs = doExecSQLQuery(db,sql,args);
					return rs;
				}catch(Exception e){
					logger.error("execSQLQuery db("+i+") error",e);
				}
			}					
			return null;
		}else{
			db = this.getWriteDB();
			try{
				DBResultSet rs = doExecSQLQuery(db,sql,args);
				return rs;
			}catch(Exception e){
				logger.error("execSQLQuery (true) error",e);
				return null;
			}
		}		
	}



	public int execSQLUpdate(String sql,Object[] args)
	{
		long st = System.currentTimeMillis();
		MoDB db = this.getWriteDB();
		Connection con = null;
      	PreparedStatement stmt = null;

		try{
			con = db.getConnection();
			stmt = con.prepareStatement(sql);
			if(args!=null){
				for(int i=0;i<args.length;i++){
					MoDBRW.setArgs(stmt,i+1,args[i]);
				}
			}
         	stmt.setQueryTimeout(this.timeout);
         	return stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("execSQLUpdate error",e);
			return -1;
		}finally{
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.info("execSQLUpdate: "+sql+" ="+(System.currentTimeMillis()-st));
		}
	}
	public MoDB getReadDB(){
		if(readDBs==null || readDBs.length==0){
			return this.getWriteDB();
		}else{
			int a = randomGenerator.nextInt();
			if (a<0) a = -a;
			return readDBs[a % readDBs.length];
		}
	}

	int getTimeout(){
		return this.timeout;
	}

	public MoDB getWriteDB(){
		return writeDB;
	}
	void setTimeout(int timeout){
		this.timeout = timeout;
	}
}
