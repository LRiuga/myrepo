package com.mozat.morange.util;

import org.apache.log4j.Logger;



public class GameAPI {
	private static Logger log = Logger.getLogger("sys");
	
	public static final String REMOVE_FISHER_CACHE = "REMOVE_FISHER_CACHE";
	public static final String REMOVE_BALANCE_CACHE = "REMOVE_BALANCE_CACHE";
	public static final String REMOVE_GEM_CACHE = "REMOVE_GEM_CACHE";
	public static final String REMOVE_BLACKLIST_CACHE = "REMOVE_BLACKLIST_CACHE";
	public static final String REMOVE_TRIBEMONSTER_CACHE = "REMOVE_TRIBEMONSTER_CACHE";//：更新家族怪开放控制缓存
	public static final String REMOVE_SHOPCONF_CACHE = "REMOVE_SHOPCONF_CACHE";//：更新商店配置缓存
	public static final String REMOVE_AREAN_CACHE = "REMOVE_AREAN_CACHE";//：更新竞技场配置缓存
	public static final String REMOVE_FUCTIONCONF_CACHE = "REMOVE_FUCTIONCONF_CACHE";//：更新functionOnline的缓存（包括邀请好友、装备打折、装备抽奖的开放）
	public static final String REMOVE_CHECKIN_CACHE = "REMOVE_CHECKIN_CACHE";//：更新连续签到奖励缓存
	public static final String REMOVE_DAILYEVENT_CACHE = "REMOVE_DAILYEVENT_CACHE";//：更新日常活动配置的缓存
	public static final String REMOVE_ACTIVITY_CACHE = "REMOVE_ACTIVITY_CACHE";//移除活动缓存
	public static final String REMOVE_VIP_CACHE  = "REMOVE_VIP_CACHE";//清理vip缓存，需要传monetId
	public static final String CREATE_WORLD_MONSTER = "CREATE_WORLD_MONSTER";//创建一个世界怪
	public static final String PREPARE_WORLD_MONSTER = "PREPARE_WORLD_MONSTER";//准备数据
	
	
	public static boolean call(String apiKey, int monetId) {
		try {
			String msg = HttpUtil.doGet(Global.getGameAPI()+"?action="+apiKey+"&monetId="+monetId);
			if ("success".equals(msg))
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	public static boolean call(String apiKey) {
		try {
			String msg = HttpUtil.doGet(Global.getGameAPI()+"?action="+apiKey);
			if ("success".equals(msg))
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
	public static boolean call(String apiKey,String parme) {
		try {
			String msg = HttpUtil.doPost2(Global.getGameAPI()+"?action="+apiKey,parme.getBytes());
			System.out.println(msg);
			if ("success".equals(msg))
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
	
}
