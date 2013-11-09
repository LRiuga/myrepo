package com.mozat.morange.admin.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void login(String userName, String password, AsyncCallback<Integer> callback);

	void setServer(int server, AsyncCallback<Void> callback);

	void getServer(AsyncCallback<Integer> callback);
	
}
