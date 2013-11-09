package com.mozat.morange.admin.client.fisher;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FisherDataServiceAsync {
	public void update(String json, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
