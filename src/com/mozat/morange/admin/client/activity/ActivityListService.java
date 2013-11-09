package com.mozat.morange.admin.client.activity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("ActivityList")
public interface ActivityListService extends RemoteService{

	public int add(String activityName, Date beginDate,
			Date endDate, Date bDate,Date eDate) ;
	
	
	public String delete(int id);
	public String deletePrice(int id);
	
	public boolean update(int activityId, String activityName, Date beginDate,
			Date endDate, Date bDate,Date eDate);	
	public Map<Integer,String> getItemName(int type);

	public String list(int page, int size, String sctivityName) throws Exception;

	public List<String> getAllActivityName() throws Exception;
	
	public Map<Integer,String> getActivityIdAndName();
	
	public Map<Integer,String> getAndSetItemName(int type);
	
	public int savePrice(Map<Integer, String> map);
	
	public String listPrice(int page, int size, int activityid);
	
	public boolean updateActivity(int id, String s3, String s4);
	
	public int saveHuang(int aaid, int hhaid, Date date, String mid);
	
	public List<String> listFishingConf();
	
	public boolean saveFisherConf(String string);
}
