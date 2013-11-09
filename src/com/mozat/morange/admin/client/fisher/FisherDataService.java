package com.mozat.morange.admin.client.fisher;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("fisherData")
public interface FisherDataService extends RemoteService{
	public String update(String json);
}
