package com.mozat.morange.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


public class Global{
	private static Logger log = Logger.getLogger("sys");
	private static MoDBRW modb;
	private static MoDBRW modbconf;
	private static MoDBRW logindb;
	private static List<MoDBRW> serverdbs = new ArrayList<MoDBRW>();
	private static List<MoDBRW> configdbs = new ArrayList<MoDBRW>();
	private static List<String> apis = new ArrayList<String>();
	private static String GameAPI;
	private static MoDBRW accountdb;
	private static String serverIP;
	private static String accountIP;
	private static String accountDb;
	private static String loginIP;
	private static String flag;
	private static String gameIP;
	private static  String[] dbList;

	private static TimeZone ClientTimeZone;
	private static long LocalToClientTimeDiff;
	static {
        CompositeConfiguration settings = null;
        Configuration serverConf = null;
        try {
            settings = new CompositeConfiguration();
            settings.addConfiguration(new PropertiesConfiguration("test.properties"));
            serverConf = settings.subset("service");
        } catch (ConfigurationException e) {
            System.out.println("load system setting error: " + e);
            System.exit(1);
        }
        
        try {
        	String dbDriver = serverConf.getString("dbDriver");
        	serverIP = serverConf.getString("serverIP");
        	accountIP = serverConf.getString("accountIP");
        	loginIP = serverConf.getString("loginIP");
        	flag = serverConf.getString("flag");
        	gameIP = serverConf.getString("gameIP");
        	accountDb = serverConf.getString("accountDb");
        	
         
            String accountDBWriteUrl = "jdbc:jtds:sqlserver://"+accountIP+"/" + accountDb + ";user=mozone;password=morangerunmozone;useLOBs=false";
            
            accountdb = new MoDBRW(accountDBWriteUrl, null, dbDriver);
            
            String loginDBWriteUrl = "jdbc:jtds:sqlserver://"+loginIP+"/OceanAge;user=mozone;password=morangerunmozone;useLOBs=false";
            logindb = new MoDBRW(loginDBWriteUrl, null, dbDriver);
            
            String dbListString = serverConf.getString("serverDBList");
            dbList = dbListString.split("\\.");
            for(String s : dbList){
            	String serverUrl = "jdbc:jtds:sqlserver://"+serverIP+"/OceanAge_" + s + ";user=mozone;password=morangerunmozone;useLOBs=false";
            	String configUrl = "jdbc:jtds:sqlserver://"+serverIP+"/OceanAgeConfig_" + s + ";user=mozone;password=morangerunmozone;useLOBs=false";
            	serverdbs.add(new MoDBRW(serverUrl, null, dbDriver));
            	configdbs.add(new MoDBRW(configUrl, null, dbDriver));
            }
            
            String apiListString = serverConf.getString("GameAPIList");
            String[] apiList = apiListString.split("\\.");
            
            for(String s : apiList){
            	apis.add("http://"+gameIP + s + ":8080/gameapi");
            }
            
            modb = serverdbs.get(0);
            modbconf = configdbs.get(0);
            GameAPI = apis.get(0);
            
        } catch (Exception e) {
            log.error("load system setting error:", e);
            System.exit(1);
        }
        
        try {
			int timezone = serverConf.getInt("timezone");
			ClientTimeZone = new SimpleTimeZone(timezone*60*60*1000, "GMT+"+timezone);
		} catch (Exception e) {
			ClientTimeZone = new SimpleTimeZone(8*60*60*1000, "GMT+8");
		} finally {
			LocalToClientTimeDiff = SimpleTimeZone.getDefault().getRawOffset() - ClientTimeZone.getRawOffset();
		}
	}
	public static Date convertLocal2SpTime(Date localTime) {
        return new Date(localTime.getTime() - LocalToClientTimeDiff);
    }
	public static MoDBRW getAccountDB() {
		return accountdb;
	}
	
	
	public static Date getClientNow() {
        return convertLocal2SpTime(new Date());
    }
	public static String[] getDbList() {
		return dbList;
	}
	public static String getFlag() {
		return flag;
	}
	public static String getGameAPI() {
		return GameAPI;
	}
	public static MoDBRW getLogindb() {
		return logindb;
	}
	
	public static MoDBRW getModb() {
		return modb;
	}
	
	public static MoDBRW getModb(int server) {
		return serverdbs.get(server);
	}
	
	public static MoDBRW getModbconf() {
		return modbconf;
	}
	
	public static MoDBRW getModbconf(int server) {
		return configdbs.get(server);
	}
	
	public static void setDbList(String[] dbList) {
		Global.dbList = dbList;
	}
	
	public static void setFlag(String flag) {
		Global.flag = flag;
	}
	
	public static void setLogindb(MoDBRW logindb) {
		Global.logindb = logindb;
	}
	
	public static void setModbconf(MoDBRW modbconf) {
		Global.modbconf = modbconf;
	}
    public static void setServer(int server){
		modb = serverdbs.get(server);
        modbconf = configdbs.get(server);
        GameAPI = apis.get(server);
	}
	
}
