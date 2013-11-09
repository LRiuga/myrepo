package com.mozat.morange.admin.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.black.BlackListService;
import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.GameAPI;
import com.mozat.morange.util.Global;

public class BlackListServiceImpl extends RemoteServiceServlet implements
		BlackListService {
	private static final long serialVersionUID = -3435758259880172268L;
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String add(String json) throws Exception{
		JSONObject obj = new JSONObject(json);
		int monetId = obj.getInt("monetId");
		String reason = obj.getString("reason");
		String expire = obj.getString("expire");
		Date date = null;
		if (expire!=null && expire.length()>0) {
				date = sd.parse(expire);
		}
		String sql = "insert into blacklist (monet_id, msg, expire_date) values (?,?,?)";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{monetId, reason, date});
		if(GameAPI.call(GameAPI.REMOVE_BLACKLIST_CACHE, monetId)){
			return c > 0 ? "true":"false";
		}
		return "false";
	}

	@Override
	public String list(int page, int size,int monetid) throws Exception{
		String sql = "select count(*) as num from blacklist";
		int count = 0;
	
		DBResultSet rs = Global.getModb().execSQLQuery(sql, new Object[]{},true);
		while(rs.next()) {
			count = rs.getInt(0);
		}
	
		JSONObject obj = new JSONObject();
		int pages = count%size==0?(count/size):(count/size+1);
		obj.put("page", page);
		obj.put("size", size);
		obj.put("pages", pages);
		if(!(monetid > 0)) {
		sql = "select * from (select top "+size+" * from (select top "
				+page*size+" * from blacklist order by monet_id asc) as tablePage" +
						" order by monet_id desc) as tablePage2 order by monet_id asc";
		}else{
			sql = "select * from blacklist where monet_id = " + monetid;
		}
		rs = Global.getModb().execSQLQuery(sql, new Object[]{},true);
		JSONArray array = new JSONArray();
		while(rs.next()) {
			JSONObject o = new JSONObject();
			o.put("monetId", rs.getInt(0));
			o.put("reason", rs.getString(1));
			o.put("expire", rs.getDate(2)==null?"":sd.format(rs.getDate(2)));
			array.put(o);
		}
		obj.put("list", array);
		System.out.println("BlackListServiceImpl list obj = "+obj.toString());
		return obj.toString();
	}

	@Override
	public String delete(int monetId) {
		String sql = "delete from blacklist where monet_id=?";
		int c =Global.getModb().execSQLUpdate(sql, new Object[]{monetId});
		if(GameAPI.call(GameAPI.REMOVE_BLACKLIST_CACHE, monetId)){
			return c>0 ? "true":"false";
		}
		return "false";
	}

	@Override
	public String update(String json) throws Exception {
		JSONObject obj = new JSONObject(json);
		int monetId = obj.getInt("monetId");
		String reason = obj.getString("reason");
		String expire = obj.getString("expire");
		Date date = null;
		if (expire!=null && expire.length()>0) {
				date = sd.parse(expire);
		}
		String sql = "update blacklist set  msg = ?, expire_date = ? where monet_id = ?";
		int c = Global.getModb().execSQLUpdate(sql, new Object[]{reason, date,monetId});
		if(GameAPI.call(GameAPI.REMOVE_BLACKLIST_CACHE, monetId)){
			return c > 0 ? "true":"false";
		}
		return "false";
	}

}
