package com.mozat.morange.admin.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Login")
public interface LoginService extends RemoteService{

	public int login(String userName,String password);

	public  void setServer(int server);
	
	public  int getServer();
}
