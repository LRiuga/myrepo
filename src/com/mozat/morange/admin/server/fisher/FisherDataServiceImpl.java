package com.mozat.morange.admin.server.fisher;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.fisher.FisherDataService;

public class FisherDataServiceImpl extends RemoteServiceServlet implements
	FisherDataService{
	private static final long serialVersionUID = 4705425913245329695L;
	private static Logger log = Logger.getLogger("sys");
	@Override
	public String update(String json) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
		} catch (Exception e) {
			log.error("obj format fail : "+e);
			return "{\"ret\":0}";
		}
		try {
			obj = handler(obj);
			return obj.toString();
		} catch (Exception e) {
			log.error(e);
			return "{\"ret\":0}";
		}
	}
	
	private JSONObject handler(JSONObject json) throws Exception {
		String type = json.getString("type");
		JSONObject obj = new JSONObject();
		if ("monetid".equals(type)) {
			int monetId = json.getInt("monetId");
			Fisher fisher = Fisher.get(monetId);
			obj.put("monetId", fisher.getMonetId());
			obj.put("nickName", fisher.getNickName());
			obj.put("gold", fisher.getGold());
			obj.put("sapphire", fisher.getSapphire());
			obj.put("pearls", fisher.getPearls());
			obj.put("userName",fisher.getUserName());
			obj.put("vip",fisher.getVip());
			return obj;
		} else if ("username".equals(type)) {
			String name = json.getString("name");
			Fisher fisher = Fisher.get(name);
			obj.put("monetId", fisher.getMonetId());
			obj.put("nickName", fisher.getNickName());
			obj.put("gold", fisher.getGold());
			obj.put("sapphire", fisher.getSapphire());
			obj.put("pearls", fisher.getPearls());
			obj.put("userName",fisher.getUserName());
			return obj;
		} else if ("updateFisher".equals(type)) {
			if (json.has("gold")) {
				long gold = json.getLong("gold");
				int monetId = json.getInt("monetId");
				if (Fisher.updateGold(monetId, gold)) 
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			if (json.has("sapphire")) {
				int sapphire = json.getInt("sapphire");
				int monetId = json.getInt("monetId");
				if (Fisher.updateSapphire(monetId, sapphire))
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			if (json.has("pearls")) {
				int pearls = json.getInt("pearls");
				int monetId = json.getInt("monetId");
				if (Fisher.updatePearls(monetId, pearls))
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			
			if (json.has("nickName")) {
				String nickName = json.getString("nickName");
				int monetId = json.getInt("monetId");
				if (Fisher.updateNickName(monetId, nickName))
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			if (json.has("vip")) {
				int vip_lv = json.getInt("vip");
				int monetId = json.getInt("monetId");
				if (Fisher.setVip(monetId, vip_lv))
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			if (json.has("password")) {
				String password = json.getString("password");
				int monetId = json.getInt("monetId");
				if (Fisher.setPassword(monetId, password))
					obj.put("ret", 1);
				else
					obj.put("ret", 0);
			}
			return obj;
		}
		return null;
	}
}
