package com.mozat.morange.admin.server.fisher;

import org.apache.log4j.Logger;

import static com.mozat.morange.util.GameAPI.*;

import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.Global;
import com.mozat.morange.util.MD5;

public class Fisher {
	private static Logger log = Logger.getLogger("sys");
	private int monetId;
	private String nickName;
	private int vip = 0;

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	private long gold;
	private int sapphire;
	private int pearls;
	
	private String userName;
	
	public String getUserName(){
		return userName;
	}

	public int getMonetId() {
		return monetId;
	}

	public String getNickName() {
		return nickName;
	}

	public long getGold() {
		return gold;
	}

	public int getSapphire() {
		return sapphire;
	}

	public int getPearls() {
		return pearls;
	}
	
	public static boolean updateGold(int monetId, long gold) {
		String sql = "update fisher set money=? where monetid=?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{gold, monetId});
		log.info(call(REMOVE_FISHER_CACHE, monetId));
		if (call(REMOVE_FISHER_CACHE, monetId)){
			return c>0;
		}
		else
			return false;	
	}
	
	public static boolean updateSapphire(int monetId, int sapphire) {
		String sql = "update profile set balance=? where monetId=?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{sapphire, monetId});
		if (call(REMOVE_BALANCE_CACHE, monetId))
			return c>0;
		else
			return false;
	}
	
	public static boolean updateNickName(int monetId, String nickName) {
		String sql = "update profile set nickName= ? where monetId=?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{nickName, monetId});
		if (call(REMOVE_FISHER_CACHE, monetId))
			return c>0;
		else
			return false;
	}
	
	public static boolean setVip(int monetId, int vip_lv) {
		String sql = "update Vip set vip_lv =? where monet_Id=?";
		if(vip_lv == 0){
			sql = "delete Vip  where monet_Id= ? ";
			int c = Global.getModb().execSQLUpdate(sql, new Object[]{monetId});
			if (call(REMOVE_VIP_CACHE, monetId) && call(REMOVE_FISHER_CACHE, monetId))
				return c > 0;
		}else{
			int c = Global.getModb().execSQLUpdate(sql, new Object[]{vip_lv, monetId});
			if(c ==0) {
				sql = "insert into  Vip(vip_lv,monet_Id,total_sapphire) values (?,?,?)";
				c = Global.getModb().execSQLUpdate(sql, new Object[]{vip_lv,monetId,0});
			}
			if (call(REMOVE_VIP_CACHE, monetId) && call(REMOVE_FISHER_CACHE, monetId))
				return c > 0;
		}
		return false;
		
	}
	
	public static boolean setPassword(int monetId, String password) {
		String sql = "update user_info set password = ? where user_id = ?";
		password = MD5.getHashString(password);
		byte[] b = MD5.hexStringToBytes(password);

		int c = Global.getAccountDB().execSQLUpdate(sql, new Object[]{b, monetId});
		return c>0;
	}
	
	public static boolean updatePearls(int monetId, int pearls) {
		String sql = "update gem set [count]=? where ownerId=? and typeid=1";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{pearls, monetId});
		if (call(REMOVE_GEM_CACHE, monetId))
			return c>0;
		else
			return false;
	}
	
	public static Fisher get(String Name) throws Exception {
		String sql = "select user_id from user_info where user_name = '" + Name + "@shabik.com'" ;
		DBResultSet rs = Global.getAccountDB().execSQLQuery(sql, new Object[]{},true);
		int monetId = 0;
		try {
			if (rs!=null && rs.next()) {
				monetId = rs.getInt(0);
			}
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		return get(monetId);
	}
	
	public static Fisher get(int monetId) throws Exception {
		if (monetId == 0){
			throw new Exception() ;
		}
		String sql = "select monetId,nickName,balance from profile where monetId=?";
		Object[] params = new Object[]{monetId};
		DBResultSet rs = Global.getModb().execSQLQuery(sql, params,true);
		Fisher fisher = new Fisher();
		try {
			if (rs != null && rs.next()) {
				fisher.monetId = rs.getInt(0);
				fisher.nickName = rs.getString(1);
				fisher.sapphire = rs.getInt(2);
			}
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		
		sql = "select money from fisher where monetId=?";
		rs = Global.getModb().execSQLQuery(sql, params,true);
		try {
			if (rs!=null && rs.next()) {
				fisher.gold = rs.getInt(0);
			}
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		
		sql = "select [count] from gem where ownerId=? and typeId=1";
		rs = Global.getModb().execSQLQuery(sql, params,true);
		try {
			if (rs!=null && rs.next()) {
				fisher.pearls = rs.getInt(0);
			}
		} catch (Exception e) {
			log.error(e);
			return null;   
		}
		
		sql = "select user_name from user_info where user_id=?";
		rs = Global.getAccountDB().execSQL(sql, params);
		try {
			if (rs!=null && rs.next()) {
				fisher.userName = rs.getString(0);
			}
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		
		sql = "select vip_lv from  Vip  where monet_Id=?";
		rs  = Global.getModb().execSQL(sql, params);
		try {
			if (rs!=null && rs.next()) {
				fisher.vip = rs.getInt(0);
			}
			
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		return fisher;
	}
}
