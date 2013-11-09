package com.mozat.morange.admin.client.black;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("blackList")
public interface BlackListService extends RemoteService {
	
	public String add(String json) throws Exception;
	
	public String list(int page, int size, int monetid) throws Exception;
	
	public String delete(int monetId);
	
	public String update(String json) throws Exception;
}
