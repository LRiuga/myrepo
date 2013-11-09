package com.mozat.morange.admin.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FriendServiceAsync {
	
	


	void getAllArean(AsyncCallback<Map<String, Integer>> callback);


	void saveArean(int id, int minlevel, int maxlevel, int visiable, AsyncCallback<Integer> callback);


	void saveEquipment(List<String> list, AsyncCallback<Boolean> callback);


	void listFriend(AsyncCallback<String> callback);


	void saveFriend(String s, AsyncCallback<Integer> callback);


	void listEquipmentLuckyDraw(AsyncCallback<String> callback);


	void saveEquipmentLuckyDraw(String s, AsyncCallback<Integer> callback);


	void listEquipmentDiscount(AsyncCallback<String> callback);


	void saveEquipmentDiscount(String s, AsyncCallback<Integer> callback);


	void setTribeMonter(AsyncCallback<String> callback);




	void updateTribeMonter(String s, AsyncCallback<Integer> callback);


	void listFunction(AsyncCallback<List<Integer>> callback);


	void saveFunction(List<Integer> list, AsyncCallback<Boolean> callback);


	void listEquipment(AsyncCallback<List<String>> callback);


	void listEquipmentDiscountConfig(int type, AsyncCallback<Map<String,List<String>>> callback);


	void getWorldMonterConfig( AsyncCallback<List<Integer>> callback);


	void saveWorldMonster(List<String> list, AsyncCallback<Integer> callback);


	void saveEquipmentDiscountConfig(List<String> list, int type, AsyncCallback<Boolean> callback);


}
