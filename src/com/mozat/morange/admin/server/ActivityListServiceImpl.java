package com.mozat.morange.admin.server;

import static com.mozat.morange.util.GameAPI.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.activity.ActivityListService;
import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.Global;

public class ActivityListServiceImpl extends RemoteServiceServlet implements ActivityListService {
	private static final long serialVersionUID = 470542591324533525L;
	private static Logger log = Logger.getLogger("sys");
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int pcid = 0;

	@Override
	public String list(int page, int size, String activityName) throws Exception {
		DBResultSet rs = null;
		int count = 0;
		String sql = null;
		if ("".equals(activityName) || activityName == null) {
			sql = "select count(*) as num from _activity";
		} else {
			sql = "select count(*) as num from _activity where activity_name like '" + activityName + "' ";
		}

		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		while (rs.next()) {
			count = rs.getInt(0);
		}

		JSONObject obj = new JSONObject();
		int pages = count % size == 0 ? (count / size) : (count / size + 1);
		obj.put("page", page);
		obj.put("size", size);
		obj.put("pages", pages);

		if ("".equals(activityName) || activityName == null) {
			sql = "select * from (select top " + size + " * from (select top " + page * size + " * from _activity order by id  desc ) as tablePage"
					+ " order by id asc) as tablePage2 order by id desc";
		} else {
			sql = "select * from (select top " + size + " * from (select top " + page * size + " * from _activity where activity_name like '" + activityName
					+ "' order by id ) as tablePage" + " order by id desc) as tablePage2   order by id  desc ";
		}
		JSONArray array = new JSONArray();
		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		if (rs != null && rs.hasNext()) {
			while (rs.next()) {
				JSONObject o = new JSONObject();
				o.put("id", rs.getInt("id"));
				o.put("assign_reward", rs.getInt("assign_reward"));
				o.put("parent_id", rs.getInt("parent_id"));
				o.put("activity_name", rs.getString("activity_name"));
				o.put("activity_open", rs.getDate("activity_open") == null ? "" : sd.format(rs.getDate("activity_open")));
				o.put("activity_close", rs.getDate("activity_close") == null ? "" : sd.format(rs.getDate("activity_close")));
				array.put(o);
			}
			obj.put("list", array);
			log.info("_activity SERVICE list : obj = " + obj.toString());

		}
		return obj.toString();
	}

	@Override
	public String delete(int id) {
		String sql = "delete from _activity where id = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { id });
		call(REMOVE_ACTIVITY_CACHE);
		return c > 0 ? "true" : "false";
	}

	@Override
	public String deletePrice(int id) {
		String sql = "delete from _activityPrize where id = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { id });
		call(REMOVE_ACTIVITY_CACHE);
		return c > 0 ? "true" : "false";
	}

	@Override
	public List<String> getAllActivityName() throws Exception {
		String sql = "SELECT distinct activity_name FROM _activity";
		List<String> activityList = new ArrayList<String>();
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		if (rs != null && rs.hasNext()) {
			while (rs.next()) {
				activityList.add(rs.getString(0));
			}
		}
		return activityList;
	}

	@Override
	public int add(String activityName, Date beginDate, Date endDate, Date bDate, Date eDate) {
		String sql = "insert into  _activity(activity_name,activity_open,activity_close,parent_id,assign_reward) values(?,?,?,?,?)";
		Object[] o = new Object[] { activityName, beginDate, endDate, 0, 0 };
		
		if ("PCRegistration".equals(activityName)) {
			sql = "insert into  _activity(activity_name,activity_open,activity_close,parent_id,assign_reward,icon) values(?,?,?,?,?,?)";
			o = new Object[] { activityName, bDate, eDate, pcid, 0, "/petCompetition/registration.png" };
		}
		if ("PCPreliminary".equals(activityName)) {
			sql = "insert into  _activity(activity_name,activity_open,activity_close,parent_id,assign_reward,icon) values(?,?,?,?,?,?)";
			o = new Object[] { activityName, bDate, eDate, pcid, 0, "/petCompetition/preliminary.png" };
		}
		if ("PC32Final".equals(activityName)) {
			sql = "insert into  _activity(activity_name,activity_open,activity_close,parent_id,assign_reward,icon) values(?,?,?,?,?,?)";
			o = new Object[] { activityName, bDate, eDate, pcid, 0, "/petCompetition/final_32.png" };
		}
		if ("PCFinal".equals(activityName)) {
			sql = "insert into  _activity(activity_name,activity_open,activity_close,parent_id,assign_reward,icon) values(?,?,?,?,?,?)";
			o = new Object[] { activityName, bDate, eDate, pcid, 0, "/petCompetition/final.png" };
		}

		int c = Global.getModb().execSQLUpdate(sql, o);
		int id = 0;
		if (c > 0) {
			DBResultSet rs = Global.getModb().execSQLQuery("select IDENT_CURRENT('_activity') ", new Object[] {}, true);
			try {
				rs.next();
				id = rs.getInt(0);
				if ("PetCompetition".equals(activityName)) {
					pcid = id;
					call(REMOVE_ACTIVITY_CACHE);
					return id;
				}

				if (activityName.startsWith("PC")) {
					call(REMOVE_ACTIVITY_CACHE);
					return id;
				}
				
				if ("FishingCompetition".equals(activityName)) {
					Global.getModb().execSQLUpdate(sql, new Object[] { activityName, bDate, eDate, id, 0 });
					Global.getModb().execSQLUpdate(sql, new Object[] { activityName, bDate, eDate, id, 0 });
				}

				c = Global.getModb().execSQLUpdate(sql, new Object[] { activityName, bDate, eDate, id, 0 });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (c > 0) {
				rs = Global.getModb().execSQLQuery("select IDENT_CURRENT('_activity') ", new Object[] {}, true);
				try {
					rs.next();
					id = rs.getInt(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return -1;
			}
		} else {
			return -1;
		}
		call(REMOVE_ACTIVITY_CACHE);
		return id;
	}

	@Override
	public boolean update(int activityId, String activityName, Date beginDate, Date endDate, Date bDate, Date eDate) {
		String sql = "update   _activity set activity_name = ? , activity_open = ? , activity_close = ?  where id = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { activityName, bDate, eDate, activityId });
		int id = 0;
		if (c > 0) {
			DBResultSet rs = Global.getModb().execSQLQuery("select parent_id from  _activity where id = ?", new Object[] { activityId });
			try {
				rs.next();
				id = rs.getInt(0);
				if (id > 0) {
					c = Global.getModb().execSQLUpdate(sql, new Object[] { activityName, beginDate, endDate, id });
				}
				call(REMOVE_ACTIVITY_CACHE);
				return c > 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	@Override
	public Map<Integer, String> getItemName(int type) {
		Map<Integer, String> itemMap = new HashMap<Integer, String>();
		List<Integer> shopitemIdList = new ArrayList<Integer>();
		String sql = "select distinct shopitemid from ActivityPrizeList where type = ? ";
		DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] { type }, true);
		while (rs.next()) {
			try {
				shopitemIdList.add(rs.getInt(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String idStr = getStringfromList(shopitemIdList);
		if (type == 0) {// shopitemid
			sql = "select itemid,itemcreditText from shopitem where itemid in " + idStr;
		} else if (type == 3) {// shop
			sql = "select id,ship_name from shipType where id in " + idStr;
		} else if (type == 5) {// crown
			sql = "select itemtypeid,itemcreditText from shopitem where itemtype = 'Equipment' and itemtypeid in " + idStr;
		}
		rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);
		while (rs.next()) {
			try {
				itemMap.put(rs.getInt(0), rs.getString(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return itemMap;
	}

	private String getStringfromList(List<Integer> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i : list) {
			sb.append(i + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Map<Integer, String> getActivityIdAndName() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String sql = "select id,activity_name from _activity where parent_id !=  0 ";
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		while (rs.next()) {
			try {
				map.put(rs.getInt(0), rs.getString(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public Map<Integer, String> getAndSetItemName(int type) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String sql = "";
		if (type == 0) {
			sql = "select itemId,itemCreditText from shopitem";
			DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);
			while (rs.next()) {
				try {
					map.put(rs.getInt(0), rs.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (type == 3) {
			sql = "select id,ship_name from shiptype";
			DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);
			while (rs.next()) {
				try {
					map.put(rs.getInt(0), rs.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (type == 9) {
			sql = "select id,itemCreditText,userLevel,quality from ( "
					+ "select itemtypeid,itemCreditText,userLevel FROM shopitem WHERE itemType = 'Equipment' )a " + "JOIN "
					+ "(select id,quality from EquipmentType ) " + "b on a.itemtypeid = b.id";
			DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {}, true);
			while (rs.next()) {
				try {
					map.put(rs.getInt(0), rs.getInt(2) + "_" + rs.getString(3) + "_" + rs.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	@Override
	public int savePrice(Map<Integer, String> map) {
		String sql = "insert into _activityPrize(activity_id,item_type,item_id,item_amount,rank_start,rank_end,expire) values(?,?,?,?,?,?,?)";
		int c = 0;
		for (int i : map.keySet()) {
			String s = map.get(i);
			// 57:4:0:1:1-1:72
			String a[] = s.split(":");
			int activityid = Integer.parseInt(a[0]);
			int itemtypeid = Integer.parseInt(a[1]);
			int itemid = Integer.parseInt(a[2]);
			int itemamount = Integer.parseInt(a[3]);
			int expire = Integer.parseInt(a[5]);
			String b[] = a[4].split("-");
			int rank_start = Integer.parseInt(b[0]);
			int rank_end = Integer.parseInt(b[1]);

			c += Global.getModb().execSQLUpdate(sql, new Object[] { activityid, itemtypeid, itemid, itemamount, rank_start, rank_end, expire });
		}
		call(REMOVE_ACTIVITY_CACHE);
		return c;
	}

	@Override
	public String listPrice(int page, int size, int activityid) {
		DBResultSet rs = null;
		int count = 0;
		String sql = null;
		if (activityid == 0) {
			sql = "select count(*) as num from _activityPrize";
		} else {
			sql = "select count(*) as num from _activityPrize where activity_id = " + activityid + " ";
		}

		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		while (rs.next()) {
			try {
				count = rs.getInt(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JSONObject obj = new JSONObject();
		int pages = count % size == 0 ? (count / size) : (count / size + 1);

		try {
			obj.put("page", page);
			obj.put("size", size);
			obj.put("pages", pages);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (activityid == 0) {
			sql = "select * from (select top " + size + " * from (select top " + page * size + " * from _activityPrize order by id desc ) as tablePage"
					+ " order by id ) as tablePage2 order by id desc";
		} else {
			sql = "select * from (select top " + size + " * from (select top " + page * size + " * from _activityPrize where activity_id = " + activityid
					+ " order by id desc ) as tablePage" + " order by id ) as tablePage2   order by id desc";
		}
		JSONArray array = new JSONArray();
		rs = Global.getModb().execSQLQuery(sql, new Object[] {}, true);
		if (rs != null && rs.hasNext()) {
			while (rs.next()) {
				JSONObject o = new JSONObject();
				try {
					o.put("id", rs.getInt("id"));
					o.put("activity_id", rs.getInt("activity_id"));
					o.put("item_type", rs.getInt("item_type"));
					o.put("item_id", rs.getInt("item_id"));
					o.put("item_amount", rs.getInt("item_amount"));
					o.put("rank_start", rs.getInt("rank_start"));
					o.put("rank_end", rs.getInt("rank_end"));
					o.put("expire", rs.getInt("expire"));
					array.put(o);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				obj.put("list", array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			log.info("_activityPrize SERVICE list : obj = " + obj.toString());

		}
		return obj.toString();
	}

	@Override
	public boolean updateActivity(int id, String s3, String s4) {
		String sql = "update _activity set activity_open = ? ,activity_close = ? where id = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[] { s3, s4, id });
		call(REMOVE_ACTIVITY_CACHE);
		return c > 0;
	}

	@Override
	public int saveHuang(int aaid, int hhaid, Date date, String mid) {
		List<Integer> list = new ArrayList<Integer>();
		if ("".equals(mid)) {
			String s = "select top 300 monetid from _activityUserData where activityconfid = ? order by point desc  ";
			DBResultSet rs = Global.getModb().execSQLQuery(s, new Object[] {aaid}, true);
			while (rs.next()) {
				try {
					list.add(rs.getInt(0));
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
		} else {
			for (String m : mid.split(",")) {
				try {
					list.add(Integer.parseInt(m));
				} catch (NumberFormatException e) {
					return -2 ;
				}
			}
		}

		String s = "insert into Equipment(monet_id,type_id,expire_date,status,msg,reinforce_count) values(?,?,?,?,?,?)";
		for (int m : list) {
			try {
				Global.getModb().execSQLUpdate(s, new Object[] { m, 1, date, 1, "AttackFriend", 0 });
			} catch (Exception e) {
				e.printStackTrace();
				return -3 ;
			}
		}

		s = "insert into _activityUserData(activityConfid,monetid,point,gotReward,createTime,updateTime,originPoint1,originPoint2)" + "values(?,?,?,?,?,?,?,?)";
		for (int m : list) {
			try {
				Global.getModb().execSQLUpdate(s, new Object[] { hhaid, m, 1, 0, new Date(), new Date(), 1, 0 });
			} catch (Exception e) {
				e.printStackTrace();
				return -4 ;
			}
		}
		
		return 0;
	}

	@Override
	public List<String> listFishingConf() {
		List<String> list = new ArrayList<String>();
		String sql = "select * from _activityFishingScoreConf";
		DBResultSet ds = Global.getModb().execSQLQuery(sql, new Object[]{}, true);
		while(ds.next()){
			try {
				String  s  = ds.getInt(0) + "_" + ds.getInt(1) + "_" + ds.getInt(2) + "_" + ds.getInt(3) + "_" +ds.getInt(4) + "_" +ds.getInt(5)+ "_" +ds.getInt(6) ;
				list.add(s);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}
	//保存捕鱼活动配置
	@Override
	public boolean saveFisherConf(String string) {
		String  sql = "insert into _activityFishingScoreConf(activity_id,fish_type,fish_score,daily_norm,daily_rewardType,daily_rewardCount) values(?,?,?,?,?,?) ";
		String a[] = string.split(",");
		List<Integer> list = new ArrayList<Integer>();
		try {
			for(String s:a){
				list.add(Integer.parseInt(s));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		 int c = Global.getModb().execSQLUpdate(sql, new Object[]{list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5)});
		
		call(REMOVE_ACTIVITY_CACHE);
		
		return c > 0 ;
	}
}
