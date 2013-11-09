package com.mozat.morange.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {
	public static void main(String[] args) {
		JSONObject obj = new  JSONObject();
		JSONArray ids = new JSONArray();
		ids.put(10080);
		ids.put(2);
		ids.put(3);
		try {
			obj.put("ids", ids);
			obj.put("activityId", 123);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		GameAPI.call(GameAPI.CREATE_WORLD_MONSTER,"json=" + obj.toString());
	}
}
