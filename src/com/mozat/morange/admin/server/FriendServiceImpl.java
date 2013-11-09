package com.mozat.morange.admin.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.FriendService;
import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.GameAPI;
import com.mozat.morange.util.Global;
import static com.mozat.morange.util.GameAPI.*;

public class FriendServiceImpl extends RemoteServiceServlet implements FriendService {
	private static final long serialVersionUID = 470542591324533525L;
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Map<Integer, String> sqlMap = new HashMap<Integer, String>();

	@Override
	public Map<String, Integer> getAllArean() {
		String sql = "select * from ArenaType";
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		Map<String, Integer> map = new HashMap<String, Integer>();
		while (rs.next()) {
			try {
				int id = rs.getInt("id");
				map.put(id + "_minLevel", rs.getInt("minLevel"));
				map.put(id + "_maxLevel", rs.getInt("maxLevel"));
				map.put(id + "_isPublic", rs.getInt("isPublic"));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return map;
	}

	@Override
	public List<String> listEquipment() {
		List<String> list = new ArrayList<String>();
		String sql = "select userlevel,quality from ( " + "SELECT id ,buy_level,quality from EquipmentType  " + "where buy_level > 0   " + ") a   " + "JOIN  " + "(  "
				+ "select itemid ,itemtypeid ,userlevel from shopitem " + "WHERE itemtype = 'Equipment'  " + ")b on a.id = b.itemtypeid " + "JOIN " + "( "
				+ "	select shopitemid,isVisible from ShopItemViewConfig  where pageid = 5" + ")c on b.itemid = c.shopitemid where c.isVisible > 0 ";
		DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);

		while (rs.next()) {
			try {
				list.add(rs.getInt("userlevel") + ":" + rs.getString("quality"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;      
	}

	@Override
	public String listEquipmentDiscount() {
		String sql = "select startTime,endTime,status from functionOnlineConf where name = 'equipmentDiscount'";
		String ret = null;
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		while (rs.next()) {
			try {
				ret = sd.format(rs.getDate("startTime")) + "_" + rs.getInt("status") + "_" + sd.format(rs.getDate("endTime"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (ret == null) {
			sql = "SELECT MAX(id) from functionOnlineConf";
			rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
			int id = 1;
			if (rs.next()) {
				try {
					id = rs.getInt(0) + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sql = "insert into functionOnlineConf(id,name,startTime,endTime,status) values(?,?,?,?,?)";
			Global.getModb().execSQLUpdate(sql, new Object[] { id, "equipmentDiscount", new Date(), new Date(), 0 });
		}

		return ret;
	}

	@Override
	public String listEquipmentLuckyDraw() {
		String sql = "select startTime,status from functionOnlineConf where name = 'equipmentLuckyDraw'";
		String ret = null;
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {},true);
		while (rs.next()) {
			try {
				ret = sd.format(rs.getDate("startTime")) + "_" + rs.getInt("status");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (ret == null) {
			sql = "SELECT MAX(id) from functionOnlineConf";
			rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
			int id = 1;
			if (rs.next()) {
				try {
					id = rs.getInt(0) + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sql = "insert into functionOnlineConf(id,name,startTime,status) values(?,?,?,?)";
			Global.getModb().execSQLUpdate(sql, new Object[] { id, "equipmentLuckyDraw", new Date(), 0 });
		}

		return ret;
	}

	@Override
	public String listFriend() {
		String sql = "select startTime,status from functionOnlineConf where name = 'invitationFriend'";
		String ret = null;
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {},true);
		while (rs.next()) {
			try {
				ret = sd.format(rs.getDate("startTime")) + "_" + rs.getInt("status");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (ret == null) {
			sql = "SELECT MAX(id) from functionOnlineConf";
			rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
			int id = 1;
			if (rs.next()) {
				try {
					id = rs.getInt(0) + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sql = "insert into functionOnlineConf(id,name,startTime,status) values(?,?,?,?)";
			Global.getModb().execSQLUpdate(sql, new Object[] { id, "invitationFriend", new Date(), 0 });
		}

		return ret;
	}

	@Override
	public List<Integer> listFunction() {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select sum(isVisible) from  ShopItemViewConfig " + "where shopitemid in ( " + "select  itemId from shopitem " + "where itemType = 'ReinforceStone')";
		DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {},true);
		if (rs.next()) {
			try {
				int ret = rs.getInt(0);
				if (ret > 0) {
					list.add(11);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sql = "select * from DailyEventSchedule  where id = 3 ";
		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		if (rs.next()) {
			list.add(21);
		}

		sql = "SELECT itemtype  from continousCheckInConf WHERE id = 8 and rewardlevel = 5 ";
		rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);
		if (rs.next()) {
			try {
				String ret = rs.getString(0);
				if ("ReinforceStone".equals(ret)) {
					list.add(30);
				} else if ("Weapon".equals(ret)) {
					list.add(31);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public int saveArean(int id, int minlevel, int maxlevel, int visiable) {
		String sql = "update ArenaType set minLevel = ? , maxLevel = ? , isPublic = ? where id = ?";
		Object[] o = new Object[] { minlevel, maxlevel, visiable, id };
		int c = Global.getModb().execSQLUpdate(sql, o);
		sql = "DELETE from ArenaRanking  " + "WHERE arenaTypeId = ? " + "and userId in ( " + "SELECT ownerid from Pet " + "WHERE petLevel < ? " + "or petLevel > ? " + "	)";

		Global.getModb().execSQLUpdate(sql, new Object[] { id, minlevel, maxlevel });

		if (call(REMOVE_AREAN_CACHE)) {
			return c;
		}
		return 0;
	}

	@Override
	public boolean saveEquipment(List<String> list) {
		boolean ret = true;
		String cleanSql = "UPDATE ShopItemViewConfig set isVisible = 0 WHERE shopitemid in ( " + "select itemid from ( " + "SELECT id ,buy_level,quality from EquipmentType "
				+ "where buy_level > 0 " + ") a " + "JOIN " + "( " + "select itemid ,itemtypeid ,userlevel from shopitem " + "WHERE itemtype = 'Equipment' "
				+ ")b on id = itemtypeid " + ") and pageid = 5";

		int cc = Global.getModbconf().execSQLUpdate(cleanSql, new Object[] {});
		if (cc <= 0) {
			return false;
		}

		String sql = "UPDATE ShopItemViewConfig set isVisible = 1 WHERE shopitemid in ( " + "select itemid from ( " + "SELECT id ,buy_level,quality from EquipmentType "
				+ "where buy_level > 0 " + ") a " + "JOIN " + "( " + "select itemid ,itemtypeid ,userlevel from shopitem " + "WHERE itemtype = 'Equipment' "
				+ ")b on id = itemtypeid " + "where quality = ? and  userlevel = ? " + ") and pageid = 5 ";
		Object[] obj = null;
		for (String s : list) {
			String[] a = s.split(":");
			int c = 0;
			try {
				int level = Integer.parseInt(a[0]);
				obj = new Object[] { a[1], level };
				System.out.println(level + "->" + a[1]);
				c = Global.getModbconf().execSQLUpdate(sql, obj);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			ret &= c > 0;
		}
		if (call(REMOVE_SHOPCONF_CACHE)) {
			return ret;
		}
		return false;
	}

	@Override
	public int saveEquipmentDiscount(String s) {
		System.out.println(s);
		String[] a = s.split("_");
		Date date = null;
		Date endDate = null;
		int status = 0;
		try {
			date = sd.parse(a[0]);
			endDate = sd.parse(a[2]);
			status = Integer.parseInt(a[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String cleanSql = "UPDATE functionOnlineConf set status = ? ,startTime = ?,endTime=? where name = 'equipmentDiscount'";

		int c = Global.getModb().execSQLUpdate(cleanSql, new Object[] { status, date, endDate });

		if (call(REMOVE_FUCTIONCONF_CACHE)) {
			return c;
		}
		return 0;
	}

	@Override
	public int saveEquipmentLuckyDraw(String s) {
		String[] a = s.split("_");
		Date date = null;
		int status = 0;
		try {
			date = sd.parse(a[0]);
			status = Integer.parseInt(a[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String cleanSql = "UPDATE functionOnlineConf set status = ? ,startTime = ? where name = 'equipmentLuckyDraw'";

		int c = Global.getModb().execSQLUpdate(cleanSql, new Object[] { status, date });
		if (call(REMOVE_FUCTIONCONF_CACHE)) {
			return c;
		}
		return 0;
	}

	@Override
	public int saveFriend(String s) {

		String[] a = s.split("_");
		Date date = null;
		int status = 0;
		try {
			date = sd.parse(a[0]);
			status = Integer.parseInt(a[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String cleanSql = "UPDATE functionOnlineConf set status = ? ,startTime = ? where name = 'invitationFriend'";

		int c = Global.getModb().execSQLUpdate(cleanSql, new Object[] { status, date });
		if (call(REMOVE_FUCTIONCONF_CACHE)) {
			return c;
		}
		return 0;
	}

	@Override
	public boolean saveFunction(List<Integer> list) {
		setSQLMap();
		int c = 0;
		String sql = "delete from DailyEventSchedule  where id = 3 and eventtypeid  = 3";
		c = Global.getModb().execSQLUpdate(sql, new Object[] {});
		for (int i : list) {
			if (i == 11 || i == 10 || i == 30 || i == 31) {
				c += Global.getModbconf().execSQLUpdate(sqlMap.get(i), new Object[] {});
			}
			if (i == 20 || i == 21) {
				c += Global.getModb().execSQLUpdate(sqlMap.get(i), new Object[] {});
			}

			switch (i) {
			case 10:
			case 11:
				call(REMOVE_SHOPCONF_CACHE);
				break;
			case 20:
			case 21:
				call(REMOVE_DAILYEVENT_CACHE);
				break;
			case 30:
			case 31:
				call(REMOVE_CHECKIN_CACHE);
				break;

			}
		}
		return c > 0;
	}

	void setSQLMap() {

		String sql = "update  ShopItemViewConfig set isVisible = 0  " + "where shopitemid in ( " + "select  itemId from shopitem "
				+ "where itemType = 'ReinforceStone') and pageid = 5";

		sqlMap.put(10, sql);

		sql = "update  ShopItemViewConfig set isVisible = 1  " + "where shopitemid in ( " + "select  itemId from shopitem " + "where itemType = 'ReinforceStone')";

		sqlMap.put(11, sql);

		sql = "delete from DailyEventSchedule  where id = 3";

		sqlMap.put(20, sql);

		sql = "insert into DailyEventSchedule(id,eventTypeid,startDate,endDate,occurs) values(3,3,'00:00:00','23:59:59',3)";

		sqlMap.put(21, sql);

		sql = "update continousCheckInConf set itemtype = 'Weapon' ,itemtext = 'name.weapontype.26',itemtypeid = 1 ,itemAmount = 1 where id = 8 and rewardlevel = 5";

		sqlMap.put(30, sql);

		sql = "update continousCheckInConf set itemtype = 'ReinforceStone' ,itemtext = 'name.ReinforceStone',itemtypeid = 3 ,itemAmount = 5 where id = 8 and rewardlevel = 5";

		sqlMap.put(31, sql);
	}

	@Override
	public String setTribeMonter() {
		String ret = null;
		String s = "select *  from DailyEventSchedule  where id = 4 and eventtypeid  = 4";
		DBResultSet rs = Global.getModb().execSQLQuery(s, new Object[] {}, true);
		if (!rs.next()) {
			return null;
		}

		String sql = "select value from TribeMonsterConf where name = 'AppearPeriod'";
		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		if (rs.next()) {
			try {
				ret = rs.getString(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	@Override
	public int updateTribeMonter(String s) {
		String sql = "update TribeMonsterConf set value = ? where  name = 'AppearPeriod' ";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { s });
		if ("".equals(s) || s == null) {
			sql = "delete from DailyEventSchedule  where id = 4 and eventtypeid  = 4";
			Global.getModb().execSQLUpdate(sql, new Object[] {});
			call(REMOVE_DAILYEVENT_CACHE);
		} else {
			sql = "delete from DailyEventSchedule  where id = 4 and eventtypeid  = 4";
			Global.getModb().execSQLUpdate(sql, new Object[] {});
			sql = "insert into DailyEventSchedule values(4,4,'2000-01-01 00:00:00','2100-01-01 00:00:00',0)";
			Global.getModb().execSQLUpdate(sql, new Object[] {});
			call(REMOVE_DAILYEVENT_CACHE);
		}
		if (call(REMOVE_TRIBEMONSTER_CACHE)) {
			return c;
		}
		return 0;
	}

	@Override
	public Map<String, List<String>> listEquipmentDiscountConfig(int type) {
		Map<String, List<String>> retMap = new HashMap<String, List<String>>();
		List<String> openList = new ArrayList<String>();
		List<String> discountList = new ArrayList<String>();
		String sql = null;
		DBResultSet ds = null;
		openList.add(type + "");
		if (1 == type) {
			sql = "select distinct buy_level, quality from ( " + "select id ,buy_level,quality from EquipmentType " + ") t1 " + "join " + "( "
					+ "select itemid,itemtypeid from shopitem " + "where itemtype = 'Equipment' " + ") t2 on id = itemtypeid " + "JOIN " + "( "
					+ "	SELECT shopitemid  from ShopItemViewConfig " + "	where  pageId = 6 and isVisible = 1  " + ") t3 on t3.shopitemid = t2.itemid ";
			ds = Global.getModbconf().execSQL(sql, new Object[] {});
			while (ds.next()) {
				try {
					openList.add(ds.getInt(0) + ":" + ds.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			sql = "select itemid ,quality from ( " + "select id ,quality from EquipmentType " + ") t1 join ( " + "select itemid,itemtypeid from shopitem  "
					+ "where itemtype = 'Equipment' )  " + "t2 on t1.id = t2.itemtypeid ";
			Map<Integer, String> id_quality = new HashMap<Integer, String>();
			ds = Global.getModbconf().execSQL(sql, new Object[] {});
			while (ds.next()) {
				try {
					id_quality.put(ds.getInt(0), ds.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Map<String, Integer> quality_percent = new HashMap<String, Integer>();
			sql = "select shopitems ,[percent] from  discountConf where discountType = 1";
			ds = Global.getModb().execSQL(sql, new Object[] {});
			while (ds.next()) {
				try {
					quality_percent.put(id_quality.get(ds.getInt(0)), ds.getInt(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (String s : quality_percent.keySet()) {
				discountList.add(s + ":" + quality_percent.get(s));
			}

		} else if (0 == type) {
			sql = "select shopitemid from ShopItemViewConfig where pageId = 6 and isVisible = 1";
			ds = Global.getModbconf().execSQL(sql, new Object[] {});
			Map<Integer, String> tempMap = new HashMap<Integer, String>();
			while (ds.next()) {
				try {
					tempMap.put(ds.getInt(0), "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sql = "select lv,title,shopitems from discountConf where discountType = 0 ";

			ds = Global.getModb().execSQL(sql, new Object[] {});
			while (ds.next()) {
				try {
					int shopitem = ds.getInt(2);
					if (tempMap.containsKey(shopitem)) {
						openList.add(ds.getInt(0) + ":" + ds.getString(1).split("\\.")[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			sql = "select  title ,avg([percent]) [percent]  from  discountConf where discountType = 0 GROUP BY title";
			ds = Global.getModb().execSQL(sql, new Object[] {});
			while (ds.next()) {
				try {
					discountList.add(ds.getString(0).split("\\.")[1] + ":" + ds.getInt(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		retMap.put("openList", openList);
		retMap.put("discountList", discountList);

		return retMap;
	}

	@Override
	public List<Integer> getWorldMonterConfig() {
		String sql = "select * from WorldMonsterConf";
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(0);
		list.add(0);
		DBResultSet ds = Global.getModb().execSQL(sql, new Object[] {});
		while (ds.next()) {
			try {
				String name = ds.getString("name");

				if ("AttackMonsterDaysLimit".equals(name)) {
					int value = Integer.parseInt(ds.getString("value"));
					list.set(0, value);
				}

				if ("AttackMonsterTimesLimit".equals(name)) {
					int value = Integer.parseInt(ds.getString("value"));
					list.set(1, value);
				}

				if ("MissileTypeId".equals(name)) {
					int value = Integer.parseInt(ds.getString("value"));
					list.set(2, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int saveWorldMonster(List<String> list) {
		String sql = "update  WorldMonsterConf set value = ? where name = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { list.get(0), "AttackMonsterDaysLimit" });
		if (!(c > 0)) {
			return 1;
		}
		c = Global.getModb().execSQLUpdate(sql, new Object[] { list.get(1), "AttackMonsterTimesLimit" });
		if (!(c > 0)) {
			return 2;
		}

		c = Global.getModb().execSQLUpdate(sql, new Object[] { list.get(2), "MissileTypeId" });
		if (!(c > 0)) {
			return 3;
		}

		String activityIdStr = list.get(3);

		int activityid = Integer.parseInt(activityIdStr);

		String monetids = list.get(4);

		JSONObject obj = new JSONObject();
		JSONArray ids = new JSONArray();
		JSONArray idss = new JSONArray();
		int sum = 1;
		for (String m : monetids.split(",")) {
			try {
				int mid = Integer.parseInt(m);
				ids.put(mid);
				idss.put(mid);
				sum++;
				if (sum == 100) {
					obj.put("ids", ids);
					obj.put("activityId", activityid);
					if (!GameAPI.call(GameAPI.PREPARE_WORLD_MONSTER, "json=" + obj.toString())) {
						return 5;
					} else {
						ids = new JSONArray();
						sum = 1;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				return 4;
			}
		}

		if (sum > 1) {
			try {
				obj.put("ids", ids);
				obj.put("activityId", activityid);
				if (!GameAPI.call(GameAPI.PREPARE_WORLD_MONSTER, "json=" + obj.toString())) {
					return 5;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			obj.put("ids", idss);
			obj.put("activityId", activityid);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (!GameAPI.call(GameAPI.CREATE_WORLD_MONSTER, "json=" + obj.toString())) {
			return 6;
		}
		return 0;
	}

	@Override
	public boolean saveEquipmentDiscountConfig(List<String> list, int type) {
		if (type == 1) {
			String sql = "update ShopItemViewConfig set isVisible = 0 where pageId = 6 " + 
					   "and shopItemId in  (SELECT itemid from shopitem where itemType = 'Equipment')" ;
			Global.getModbconf().execSQLUpdate(sql,  new Object[] {});
			
			for (String s : list) {
				// green:60,80:20
				String[] a = s.split(":");
				sql = "SELECT " + "itemid " + "FROM " + "	shopitem " + "WHERE " + "	itemType = 'Equipment' " + "AND itemTypeId IN ( " + "	SELECT " + "		id " + "	FROM "
						+ "		EquipmentType " + "	WHERE " + "	quality = '" + a[0] + "'  " + "	and buy_level in (" + a[1] + ") " + ")";
				
				List<Integer> itemidMap = new ArrayList<Integer>();
				int discountNum = Integer.parseInt(a[2]);
				DBResultSet ds = Global.getModbconf().execSQL(sql, new Object[] {});
				while (ds.next()) {
					try {
						itemidMap.add(ds.getInt(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for(int shopitemid :itemidMap){
					sql = "update discountConf set newPrice = listPrice * "+discountNum +" / 100 ,[percent] = ?  where shopItems =  ?" ;
					Global.getModb().execSQLUpdate(sql, new Object[]{100-discountNum,shopitemid});
					sql = "update ShopItemViewConfig set isVisible = 1 where pageId = 6 and shopItemId = ?  ";
					Global.getModbconf().execSQLUpdate(sql,  new Object[] {shopitemid});
				}
			}
			
			if(call(REMOVE_SHOPCONF_CACHE)){
				return true ;
			}
		}else if(type == 0){
			String sql = "update ShopItemViewConfig set isVisible = 0 where pageId = 6 " + 
					   "and shopItemId in  (SELECT itemid from shopitem where itemType = 'combo')" ;
			Global.getModbconf().execSQLUpdate(sql,  new Object[] {});
			for (String s : list) {
				// green:60,80:20
				String[] a = s.split(":");
				sql = "SELECT  shopitems from discountConf where 	title = 'combo." + a[0] + "'  " + "	and lv in (" + a[1] + ") ";
				
				List<Integer> itemidMap = new ArrayList<Integer>();
				int discountNum = Integer.parseInt(a[2]);
				DBResultSet ds = Global.getModb().execSQL(sql, new Object[] {});
				while (ds.next()) {
					try {
						itemidMap.add(ds.getInt(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for(int shopitemid :itemidMap){
					sql = "update discountConf set newPrice = listPrice * "+discountNum +" / 100 ,[percent] = ?  where shopItems =  ?" ;
					Global.getModb().execSQLUpdate(sql, new Object[]{100-discountNum,shopitemid});
					
					sql = "select newPrice from discountConf where shopItems =  ? " ;
					
					ds = Global.getModb().execSQL(sql, new Object[]{shopitemid});
					
					if(ds.next()){
						try {
							int newPrice = ds.getInt(0);
							sql = "update shopitem set creditPrice  = ? where itemid = ? ";
							Global.getModbconf().execSQLUpdate(sql, new Object[]{newPrice,shopitemid});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					sql = "update ShopItemViewConfig set isVisible = 1 where pageId = 6 and shopItemId = ?  ";
					Global.getModbconf().execSQLUpdate(sql,  new Object[] {shopitemid});
				}
			}
			
			if(call(REMOVE_SHOPCONF_CACHE)){
				return true ;
			}
		}
		return false;
	}

}
