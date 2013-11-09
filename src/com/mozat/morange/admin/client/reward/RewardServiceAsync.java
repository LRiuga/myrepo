package com.mozat.morange.admin.client.reward;


import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RewardServiceAsync {

	public void  getAllActivity(AsyncCallback<Map<Integer, String>> callback);

	public void getItemName(int type,AsyncCallback<Map<Integer, String>> asyncCallback);

	public void getActivityPrizeId(AsyncCallback<Map<String, Integer>> asyncCallback);

	public void setRewad(Map<Integer, String> rewardMap, AsyncCallback<Boolean> asyncCallback);
	
	public void listReward(int page, int size, String type, String key, AsyncCallback<Map<Integer, Map<String, String>>> asyncCallback);

	public void delete(int id, AsyncCallback<Boolean> asyncCallback);
}
