package com.mozat.morange.admin.client.reward;


import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Reward")
public interface RewardService extends RemoteService{

	public Map<Integer, String> getAllActivity();

	public Map<Integer, String> getItemName(int type);

	public Map<String, Integer> getActivityPrizeId();

	public boolean setRewad(Map<Integer, String> rewardMap);


	public Map<Integer, Map<String, String>> listReward(int page, int size, String type, String key);

	boolean delete(int id);

	
}
