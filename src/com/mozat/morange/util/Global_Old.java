package com.mozat.morange.util;

import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


public class Global_Old {
	private static Logger log = Logger.getLogger("sys");
	private static MoDBRW modb;
	private static MoDBRW modbconf;
	
	private static MoDBRW logindb;
	
	public static MoDBRW getLogindb() {
		return logindb;
	}

	public static void setLogindb(MoDBRW logindb) {
		Global_Old.logindb = logindb;
	}
	private static MoDBRW server1db;
	private static MoDBRW server1dbconf;
	private static MoDBRW server2db;
	private static MoDBRW server2dbconf;
	private static MoDBRW server3db;
	private static MoDBRW server3dbconf;
	private static MoDBRW server4db;
	private static MoDBRW server4dbconf;
	private static MoDBRW server5db;
	private static MoDBRW server5dbconf;
	private static MoDBRW server6db;
	private static MoDBRW server6dbconf;
	
	private static MoDBRW accountdb;

	private static String GameAPI;
	private static String GameAPI1;
	private static String GameAPI2;
	private static String GameAPI3;
	private static String GameAPI4;
	private static String GameAPI5;
	private static String GameAPI6;

	private static TimeZone ClientTimeZone;
	private static long LocalToClientTimeDiff;
	static {
        CompositeConfiguration settings = null;
        Configuration serverConf = null;
        try {
            settings = new CompositeConfiguration();
            settings.addConfiguration(new PropertiesConfiguration("system.properties"));
            serverConf = settings.subset("service");
        } catch (ConfigurationException e) {
            System.out.println("load system setting error: " + e);
            System.exit(1);
        }
        
        try {
        	String dbDriver = serverConf.getString("dbDriver");
        	
          /*  String dbWriteUrl = serverConf.getString("dbWriteUrl");
            String[] dbReadUrls = serverConf.getStringArray("dbReadUrls");
            String dbconfUrl = serverConf.getString("dbconf");
            
            modb = new MoDBRW(dbWriteUrl, dbReadUrls, dbDriver);
            modbconf = new MoDBRW(dbconfUrl, serverConf.getStringArray("dbconf1"), dbDriver);*/
            
            String server1dbUrl = serverConf.getString("server1");
            String[] server1dbUrls = serverConf.getStringArray("server1");
            server1db = new MoDBRW(server1dbUrl, server1dbUrls, dbDriver);
            String server1dbconfUrl = serverConf.getString("server1conf");
            String[] server1dbconfUrls = serverConf.getStringArray("server1conf");
            server1dbconf = new MoDBRW(server1dbconfUrl, server1dbconfUrls, dbDriver);
            
            String server2dbUrl = serverConf.getString("server2");
            String[] server2dbUrls = serverConf.getStringArray("server2");
            server2db = new MoDBRW(server2dbUrl, server2dbUrls, dbDriver);
            String server2dbconfUrl = serverConf.getString("server2conf");
            String[] server2dbconfUrls = serverConf.getStringArray("server2conf");
            server2dbconf = new MoDBRW(server2dbconfUrl, server2dbconfUrls, dbDriver);
            
            String server3dbUrl = serverConf.getString("server3");
            String[] server3dbUrls = serverConf.getStringArray("server3");
            server3db = new MoDBRW(server3dbUrl, server3dbUrls, dbDriver);
            String server3dbconfUrl = serverConf.getString("server3conf");
            String[] server3dbconfUrls = serverConf.getStringArray("server3conf");
            server3dbconf = new MoDBRW(server3dbconfUrl, server3dbconfUrls, dbDriver);
            
            String server4dbUrl = serverConf.getString("server4");
            String[] server4dbUrls = serverConf.getStringArray("server4");
            server4db = new MoDBRW(server4dbUrl, server4dbUrls, dbDriver);
            String server4dbconfUrl = serverConf.getString("server4conf");
            String[] server4dbconfUrls = serverConf.getStringArray("server4conf");
            server4dbconf = new MoDBRW(server4dbconfUrl, server4dbconfUrls, dbDriver);
            
            String server5dbUrl = serverConf.getString("server5");
            String[] server5dbUrls = serverConf.getStringArray("server5");
            server5db = new MoDBRW(server5dbUrl, server5dbUrls, dbDriver);
            String server5dbconfUrl = serverConf.getString("server5conf");
            String[] server5dbconfUrls = serverConf.getStringArray("server5conf");
            server5dbconf = new MoDBRW(server5dbconfUrl, server5dbconfUrls, dbDriver);
            
            String server6dbUrl = serverConf.getString("server6");
            String[] server6dbUrls = serverConf.getStringArray("server6");
            server6db = new MoDBRW(server6dbUrl, server6dbUrls, dbDriver);
            String server6dbconfUrl = serverConf.getString("server6conf");
            String[] server6dbconfUrls = serverConf.getStringArray("server6conf");
            server6dbconf = new MoDBRW(server6dbconfUrl, server6dbconfUrls, dbDriver);
            
            String accountDBWriteUrl = serverConf.getString("accountDBWriteUrl");
            String[] accountDBReadUrls = serverConf.getStringArray("accountDBReadUrls");
            
            accountdb = new MoDBRW(accountDBWriteUrl, accountDBReadUrls, dbDriver);
            
            String loginDBWriteUrl = serverConf.getString("login");
            String[] loginDBReadUrls = serverConf.getStringArray("login");
            logindb = new MoDBRW(loginDBWriteUrl, loginDBReadUrls, dbDriver);
            
            GameAPI1 = serverConf.getString("GameAPI1");
            GameAPI2 = serverConf.getString("GameAPI2");
            GameAPI3 = serverConf.getString("GameAPI3");
            GameAPI4 = serverConf.getString("GameAPI4");
            GameAPI5 = serverConf.getString("GameAPI5");
            GameAPI6 = serverConf.getString("GameAPI6");
            
            modb = server1db;
            modbconf = server1dbconf;
            GameAPI = GameAPI1;
            
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
	
	public static String getGameAPI() {
		System.out.println(GameAPI);
		return GameAPI;
	}
	
	public static MoDBRW getModb() {
		return modb;
	}
	
	public static MoDBRW getModbconf() {
		return modbconf;
	}
	
	public static MoDBRW getModb(int server) {
		if(server == 1 ){
			return server1db;
		}else if(server == 2){
			return server2db;
		}else if(server == 3){
			return server3db;
		}else if(server == 4){
			return server4db;
		}else if(server == 5){
			return server5db;
		}else if (server == 6) {
			return server6db;
		}
		return modb;
	}
	
	public static MoDBRW getModbconf(int server) {
		if(server == 1 ){
			return server1dbconf;
		}else if(server == 2){
			return server2dbconf;
		}else if(server == 3){
			return server3dbconf;
		}else if(server == 4){
			return server4dbconf;
		}else if(server == 5){
			return server5dbconf;
		}else if(server == 6){
			return server6dbconf;
		}
		return modbconf;
	}
	
	public static void setServer(int server){
		if(server == 1 ){
			modb = server1db;
			modbconf = server1dbconf;
			GameAPI =GameAPI1;
		}else if(server == 2){
			modb = server2db;
			modbconf = server2dbconf;
			GameAPI =GameAPI2;
		}else if(server == 3){
			modb = server3db;
			modbconf = server3dbconf;
			GameAPI =GameAPI3;
		}else if(server == 4){
			modb = server4db;
			modbconf = server4dbconf;
			GameAPI =GameAPI4;
		}else if(server == 5){
			modb = server5db;
			modbconf = server5dbconf;
			GameAPI =GameAPI5;
		}else if(server == 6){
			modb = server6db;
			modbconf = server6dbconf;
			GameAPI =GameAPI6;
		}
	}
    public static void setModbconf(MoDBRW modbconf) {
		Global_Old.modbconf = modbconf;
	}
	
}
