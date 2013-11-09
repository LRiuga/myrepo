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
public class MoDBHA {

	public static String DEFAULT_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

	int timeout = 30;
	
	
	static final Logger logger = Logger.getLogger(MoDBHA.class);
	static Random randomGenerator = new Random();

	MoDB[] DBs =null;
	
	int masterIdx = 0;
	
	

	void setTimeout(int timeout){
		this.timeout = timeout;
	}
	int getTimeout(){
		return this.timeout;
	}

	public MoDBHA (String dbURL,String dbDriver) throws Exception{

		DBs = new MoDB[1];
		if(dbURL==null){
			logger.error("cannot find dbURL");
			throw new Exception("cannot find dbURL");
		}

		if(dbDriver==null || dbDriver.length()==0){
			dbDriver = DEFAULT_DRIVER;
		}

		DBs[0] = new MoDB(dbDriver,dbURL);

		
	}
	
	public MoDBHA (String[] dbURLs,String dbDriver) throws Exception{

	
		if(dbURLs==null || dbURLs.length==0){
			dbURLs = null;
			return;
		}else{
			DBs = new MoDB[dbURLs.length];
			for(int i=0;i<dbURLs.length;i++){
				DBs[i] = new MoDB(dbDriver,dbURLs[i]);
			}
		}
	}



	public MoDB getReadDB(){
		if(DBs==null || DBs.length==0){
			return null;
		}else{
			int a = randomGenerator.nextInt();
			if (a<0) a = -a;
			return DBs[a % DBs.length];
		}
	}		
	
	
	
	int masterError = 0;
	void processMasterError(){
		this.masterError +=1;
		if(masterError > 10){
			this.masterIdx = (this.masterIdx+1) %DBs.length;
			logger.info("switch master db to index "+this.masterIdx);
			masterError = 0;
		}
	}
	
	
	public MoDB getDB(int idx){
		return DBs[idx];
	}

	DBResultSet doExecSQL(MoDB db,String sql,Object[] args) throws Exception
	{		
		
		long s = System.currentTimeMillis();
		Connection con = null;
      	PreparedStatement stmt = null;
      	ResultSet rs = null;
		try{
			con = db.getConnection();
         	stmt = con.prepareStatement(sql);
         	if(args!=null){
	         	for(int i=0;i<args.length;i++){
	         		MoDBHA.setArgs(stmt,i+1,args[i]);
	         	}
         	}
        	stmt.setQueryTimeout(this.timeout);
         	return new DBResultSet(stmt.executeQuery());

		}finally{
			if(rs!=null) try{rs.close();}catch(Exception e){rs=null;}
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.debug("doExecSQL: "+sql+"="+(System.currentTimeMillis()-s));
		}
	}
	
	public DBResultSet execSQL(String sql,Object[] args)
	{
		
		DBResultSet ret = null;
		
		int oldMasterIdx = this.masterIdx;
		int start = oldMasterIdx;
		for(int i=start;i<this.DBs.length;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQL(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQL error in db["+i+"]",e);
				if(i == oldMasterIdx){
					processMasterError();
				}
			}
		}
		
		for(int i=0;i<start;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQL(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQL error in db["+i+"]",e);				
			}
		}
		return ret;
	}

	
	DBResultSet doExecSQLQuery(MoDB db,String sql,Object[] args) throws Exception
	{		
		
		long s = System.currentTimeMillis();
		Connection con = null;
      	PreparedStatement stmt = null;
      	ResultSet rs = null;
		try{
			con = db.getConnection();
			con.setReadOnly(true);
         	stmt = con.prepareStatement(sql);
         	if(args!=null){
	         	for(int i=0;i<args.length;i++){
	         		MoDBHA.setArgs(stmt,i+1,args[i]);
	         	}
         	}
        	stmt.setQueryTimeout(this.timeout);
        	rs = stmt.executeQuery();
        	
         	return new DBResultSet(rs);

		}finally{
			if(rs!=null) try{rs.close();}catch(Exception e){rs=null;}
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.debug("doExecSQLQuery: "+sql+"="+(System.currentTimeMillis()-s));
			
		}
	}
	
	
	public DBResultSet execSQLQuery(String sql,Object[] args)
	{
		return execSQLQuery(sql,args,true);
	}
	public DBResultSet execSQLQuery(String sql,Object[] args,boolean forceMaster) 
	{
		
		
		
		DBResultSet ret = null;
		
		int oldMasterIdx = this.masterIdx;
		int start = this.masterIdx;
		if(forceMaster==false){ //random choose read db;
			start = randomGenerator.nextInt();
			if (start<0) start = -start;
			start = start % DBs.length;
		}
		
		
		for(int i=start;i<this.DBs.length;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQLQuery(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQLQuery error in db["+i+"]",e);
				if(i == oldMasterIdx){
					processMasterError();
				}
			}
		}
		
		for(int i=0;i<start;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQLQuery(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQLQuery error in db["+i+"]",e);				
			}
		}
		return ret;
	}

	int doExecSQLUpdate(MoDB db,String sql,Object[] args) throws Exception
	{		
		long s = System.currentTimeMillis();
		Connection con = null;
      	PreparedStatement stmt = null;

		try{
			con = db.getConnection();
			stmt = con.prepareStatement(sql);
			if(args!=null){
				for(int i=0;i<args.length;i++){
					MoDBHA.setArgs(stmt,i+1,args[i]);
				}
			}
         	stmt.setQueryTimeout(this.timeout);
         	return stmt.executeUpdate();
		}finally{
			if(stmt!=null) try{stmt.close();}catch(Exception e){stmt=null;}
			if(con!=null) try{con.close();}catch(Exception e){con=null;}
			logger.debug("doExecSQLUpdate: "+sql+"="+(System.currentTimeMillis()-s));
		}
	}
	
	public int execSQLUpdate(String sql,Object[] args) 
	{				
		int ret = -1;
		
		int oldMasterIdx = this.masterIdx;
		int start = this.masterIdx;
		for(int i=start;i<this.DBs.length;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQLUpdate(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQLUpdate error in db["+i+"]",e);
				if(i == oldMasterIdx){
					processMasterError();
				}
			}
		}
		
		for(int i=0;i<start;i++){						
			try{
				MoDB db= this.getDB(i);
				ret =  doExecSQLUpdate(db,sql, args);
				return ret;
			}catch(Exception e){
				logger.error("execSQLUpdate error in db["+i+"]",e);				
			}
		}
		return ret;
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



	//---------------------------------testing------------------------------
	static MoDB db;
	static void db_test_insert() throws Exception
	{
	
	}

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

	static void print(String str){
		System.out.println(str);
	}
}
