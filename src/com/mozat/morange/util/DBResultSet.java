package com.mozat.morange.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class DBResultSet {
		
	int idx = -1;

	
	public boolean hasNext()
	{
		return this.resultSet.size()-1 > idx;	
	}
	
	public boolean hasPrev(){ 
		return idx>0;
	}
	
	public boolean prev()
	{
		if(hasPrev()){
			idx-=1;
			return true;
		}else{
			return false;
		}
	}
	
	public void reset(){
		idx = -1;
	}
	
	public boolean next()
	{
		if(hasNext()){
			idx+=1;
			return true;
		}else{
			return false;
		}
	}
	
	Hashtable<String,Integer> columns=new  Hashtable<String,Integer>();
	byte[] fieldTypes;
		
	Vector<Object[]> resultSet = new Vector<Object[]>();
	
	public DBResultSet(ResultSet rs) throws Exception{

		
		ResultSetMetaData rsMeta = rs.getMetaData();
		
		int fieldCount = rsMeta.getColumnCount();
		if(fieldCount == 0){
			return;
		}
		for(int i=1;i<fieldCount+1;i++){
			String fieldName  = rsMeta.getColumnName(i);
			this.columns.put(fieldName, i-1);		
		}
		while(rs.next()){
			Object[] row =new Object[fieldCount];
			for(int i=1;i<fieldCount+1;i++){
				row[i-1] = rs.getObject(i);
			}
			this.resultSet.add(row);
		}		
		this.idx = -1;	
	}
	
	
	Object[] getCurrentRow()
	{
		return this.resultSet.get(this.idx);
	}
	
	Object getCurField(String key){		
		if(columns.containsKey(key))
			return getCurField(columns.get(key).intValue());
		else
			return null;
		
	}
	
	Object getCurField(int fieldIdx){		
		return getCurrentRow()[fieldIdx];
	}
	
	public Boolean getBoolean(String key)throws Exception{
		
		return (Boolean)getCurField(key);
	}
	
	public Boolean getBoolean(int fieldIdx)throws Exception{				
		return (Boolean)getCurField(fieldIdx);		
	}
	
	public Byte getByte(String key)throws Exception{							
		Number num = ((Number)getCurField(key));
		if(num ==null) return 0;
		else 
			return num.byteValue();	
	}
	
	public Byte getByte(int fieldIdx)throws Exception{		
		
		Number num = ((Number)getCurField(fieldIdx));
		if(num ==null) return 0;
		else 
			return num.byteValue();	
				
	}
	
	
	public Integer getInt(String key)throws Exception{		
		Number num = ((Number)getCurField(key));
		if(num ==null) return 0;
		else return num.intValue();	
	}
	
	public Integer getInt(int fieldIdx)throws Exception{		
		return ((Number)getCurField(fieldIdx)).intValue();	
	}
	
	public Float getFloat(String key)throws Exception{
		Double d=getDouble(key);
		if(d==null) return new Float(0);
		return d.floatValue();
	}
	
	public Float getFloat(int fieldIdx)throws Exception{
		Double d=getDouble(fieldIdx);
		if(d==null) return new Float(0);
		return d.floatValue();
	}
	
	public Double getDouble(String key)throws Exception{
		Number num = ((Number)getCurField(key));
		if(num ==null) return new Double(0);
		else 
			return num.doubleValue();	
				
	}
	
	public Double getDouble(int fieldIdx)throws Exception{
		Number num = ((Number)getCurField(fieldIdx));
		if(num ==null) return new Double(0);
		else 
			return num.doubleValue();	
	}
	
	
	
	
	public Long getLong(String key)throws Exception{
		Number num = ((Number)getCurField(key));
		if(num ==null) return new Long(0);
		else 
			return num.longValue();
	}
	
	public Long getLong(int fieldIdx)throws Exception{
		Number num = ((Number)getCurField(fieldIdx));
		if(num ==null) return new Long(0);
		else 
			return num.longValue();
	}
		
	
	public byte[] getBytes(String key) throws Exception 
	{
		return (byte[])getCurField(key);			
	}
	
	public byte[] getBytes(int fieldIdx) throws Exception 
	{
		return (byte[])getCurField(fieldIdx);			
	}
	
	public Date getDate(String key) throws Exception
	{
		return (Date)getCurField(key);

	}
	
	public Date getDate(int fieldIdx) throws Exception
	{
		return (Date)getCurField(fieldIdx);

	}
	
	public String getString(String key)throws Exception
	{
		return (String)getCurField(key);		
	}
	
	public String getString(int fieldIdx)throws Exception
	{
		return (String)getCurField(fieldIdx);	
	}
	
	public Object getObject(int fieldIdx) throws Exception {
		return getCurField(fieldIdx);
	}
	
	public Object getObject(String key) throws Exception {
		return getCurField(key);
	}
}
