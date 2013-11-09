package com.mozat.morange.admin.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Friend")
public interface FriendService extends RemoteService{

	
	//public boolean update(int activityId, String activityName, Date beginDate,
	//		Date endDate, Date last);	

	//public String list() ;
	
	public Map<String,Integer> getAllArean();
	
	public int  saveArean(int id, int minlevel, int maxlevel, int visiable);
	
	public boolean saveEquipment(List<String> list);

	public String listFriend();
	public int  saveFriend(String s );
	
	public String listEquipmentLuckyDraw();
	public int  saveEquipmentLuckyDraw(String s );
	
	public String listEquipmentDiscount();
	public int  saveEquipmentDiscount(String s );
	
	public String setTribeMonter();
	
	public int  updateTribeMonter(String s);
	
	public List<Integer> listFunction();
	
	public List<String> listEquipment();
	
	public boolean saveFunction(List<Integer> list );
	
	public Map<String,List<String>> listEquipmentDiscountConfig(int type);
	
	public List<Integer> getWorldMonterConfig();
	
	public int saveWorldMonster(List<String> list);
	
	public boolean saveEquipmentDiscountConfig(List<String> list,int type);

}
