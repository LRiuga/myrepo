package com.mozat.morange.admin.client.activity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ActivityListServiceAsync {
	
	public void add(String activityName, Date beginDate,
			Date endDate, Date bDate,Date eDate, AsyncCallback<Integer> callback) ;
	
	public void list(int page, int size, String sctivityName, AsyncCallback<String> callback);
	
	public void delete(int id, AsyncCallback<String> callback);

	public void update(int activityId, String activityName, Date beginDate,
			Date endDate, Date bDate,Date eDate,AsyncCallback<Boolean> asyncCallback);
	public void getItemName(int type, AsyncCallback<Map<Integer, String>> asyncCallback);

	public void getAllActivityName(AsyncCallback<List<String>> callback);

	void getActivityIdAndName(AsyncCallback<Map<Integer, String>> callback);

	void getAndSetItemName(int type, AsyncCallback<Map<Integer, String>> callback);

	void savePrice(Map<Integer, String> map, AsyncCallback<Integer> callback);

	void listPrice(int page, int size, int activityid, AsyncCallback<String> callback);

	void updateActivity(int id, String s3, String s4, AsyncCallback<Boolean> callback);

	void deletePrice(int id, AsyncCallback<String> callback);

	void saveHuang(int aaid, int hhaid, Date date, String mid, AsyncCallback<Integer> callback);

	void listFishingConf(AsyncCallback<List<String>> callback);

	void saveFisherConf(String string, AsyncCallback<Boolean> callback);
	

}
