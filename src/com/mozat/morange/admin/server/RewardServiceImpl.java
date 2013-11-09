package com.mozat.morange.admin.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.reward.RewardService;
import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.Global;

public class RewardServiceImpl extends RemoteServiceServlet implements RewardService {
	private static final long serialVersionUID = 470542591324543525L;

	@Override
	public Map<Integer, String> getAllActivity() {
		Map<Integer, String> activityMap = new HashMap<Integer, String>();
		String sql = "SELECT  id,ActivityName FROM activityConf";
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[] {},true);
		if (rs != null && rs.hasNext()) {
			while (rs.next()) {
				try {
					activityMap.put(rs.getInt(0), rs.getString(1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return activityMap;
	}

	@Override
	public Map<Integer, String> getItemName(int type) {
		Map<Integer, String> itemMap = new HashMap<Integer, String>();
		List<Integer> shopitemIdList = new ArrayList<Integer>();
		String sql = "select distinct shopitemid from ActivityPrizeList where type = ? ";
		DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] { type },true);
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
		rs = Global.getModbconf().execSQLQuery(sql, new Object[] {},true);
		while (rs.next()) {
			try {
				itemMap.put(rs.getInt(0), rs.getString(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return itemMap;
	}

	private String getStringfromList(List<Integer> shopitemIdList) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i : shopitemIdList) {
			sb.append(i + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Map<String, Integer> getActivityPrizeId() {
		Map<String, Integer> itemMap = new HashMap<String, Integer>();
		String sql = "select id,shopitemid,type  from ActivityPrizeList ";
		DBResultSet rs = Global.getModbconf().execSQLQuery(sql, new Object[] {},true);
		while (rs.next()) {
			try {
				itemMap.put(rs.getInt(1) + "" + rs.getInt(2), rs.getInt(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return itemMap;
	}

	@Override
	public boolean setRewad(Map<Integer, String> rewardMap) {
		String sql = "insert into _activityReward(monet_id,activity_id,item_type,item_id,item_amount,create_time,status,message) values(?,?,?,?,?,?,?,?)";
		boolean result = true;
		for (int i : rewardMap.keySet()) {
			String s =   rewardMap.get(i);
			String[] a = s.split(":");
			int activityid = Integer.parseInt(a[1]);
			int itemtypeid = Integer.parseInt(a[2]);
			int itemid = Integer.parseInt(a[3]);
			int itemamount = Integer.parseInt(a[4]);
			String message = a[5];
			
			String[] b = a[0].split(",");
			for(String monetid : b){
				int mid = Integer.parseInt(monetid);
				int c = Global.getModb().execSQLUpdate(sql, new Object[] { mid, activityid, itemtypeid, itemid,itemamount, new Date(),0, message});
				result = result && c > 0;
			}
			
			
		}

		return result;
	}

	@Override
	public Map<Integer,Map<String, String>> listReward(int page,int size,String type,String key) {
		Map<Integer,Map<String, String>> itemMap = new HashMap<Integer,Map<String, String>>();
		String sql = null;
		if("activity".equals(type)){
			sql = "select count(*) as num from _activityReward where activity_id = " + key  ;
		}else if("monetId".equals(type)){
			sql = "select count(*) as num from _activityReward where monet_id = " ;
		}else{
			sql = "select count(*) as num from _activityReward ";
		}
		int count = 0;
	
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[]{},true);
		while(rs.next()) {
			try {
				count = rs.getInt(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		Map<String,String> map = new HashMap<String, String>();
		int pages = count%size==0?(count/size):(count/size+1);
		map.put("page", page+ "");
		map.put("size", size+"");
		map.put("pages", pages+"");
		itemMap.put(-100, map);
		
		if("activity".equals(type)){
			sql = "SELECT TOP 15 * FROM (SELECT ROW_NUMBER() OVER (ORDER BY id desc ) AS RowNumber,  * FROM  _activityReward) a WHERE  activity_id = "+key+" and RowNumber >? order by id desc ";
		}else if("monetId".equals(type)){
			sql = "SELECT TOP 15 * FROM (SELECT ROW_NUMBER() OVER (ORDER BY id desc ) AS RowNumber,  * FROM  _activityReward) a WHERE monet_id = " + key + " and RowNumber >? order by id desc ";
		}else{
			sql = "SELECT TOP 15 * FROM (SELECT ROW_NUMBER() OVER (ORDER BY id desc ) AS RowNumber,  * FROM  _activityReward) a WHERE  RowNumber > ? order by id desc ";
		}
		  
		count = (page-1) * 15;
		rs = Global.getModb().execSQLQuery(sql, new Object[] { count },true);
		while (rs.next()) {
			try {
				Map<String,String> rowMap = new HashMap<String, String>();
				int id = rs.getInt("id");
				int userid = rs.getInt("monet_id");
				int activityId = rs.getInt("activity_id");
				int rewardType = rs.getInt("item_type");
				int rewardid = rs.getInt("item_id");
				int rewardAmount = rs.getInt("item_amount");
				Date addRewardTime = rs.getDate("create_time");
				int status = rs.getInt("status");
				rowMap.put(id + "_monet_id",userid + "");
				rowMap.put(id + "_activity_id",activityId + "");
				rowMap.put(id + "_item_type",rewardType+"");
				rowMap.put(id + "_reward_id",rewardid+"");
				rowMap.put(id + "_item_amount",rewardAmount + "");
				rowMap.put(id + "_create_time",addRewardTime + "");
				rowMap.put(id + "_getRewardTime",status== -1 ? "领奖" :"没领奖" );
				itemMap.put(id, rowMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return itemMap;
	}

	


	@Override
	public boolean delete(int id) {
		String sql = "delete from _activityReward where id=?";
		int c =Global.getModb().execSQLUpdate(sql, new Object[]{id});
		return c > 0 ;
	}

}
