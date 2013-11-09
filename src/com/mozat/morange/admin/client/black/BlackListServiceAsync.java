package com.mozat.morange.admin.client.black;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlackListServiceAsync {

	void add(String json, AsyncCallback<String> callback);

	void list(int page, int size, int monetid, AsyncCallback<String> callback);

	void delete(int monetId, AsyncCallback<String> callback);

	void update(String json, AsyncCallback<String> callback);

}
