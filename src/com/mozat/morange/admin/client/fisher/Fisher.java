package com.mozat.morange.admin.client.fisher;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Fisher extends Composite  {

	interface FisherUiBinder extends UiBinder<Widget, Fisher> {
	}
	private final FisherDataServiceAsync service = GWT.create(FisherDataService.class);
	private static FisherUiBinder uiBinder = GWT.create(FisherUiBinder.class);

	@UiField
	Button serchBtn,nickNameBtn,passwordBtn,vipBtn;

	@UiField
	ListBox searchType,vipList;
	
	@UiField
	TextBox nickNameBox,passwordBox,monetidBox,userNameBox,searchKey;
	
	public Fisher() {
		initWidget(uiBinder.createAndBindUi(this));
		
		serchBtn.getElement().addClassName("btn btn-primary");
		
		searchType.addItem("monetid");
		searchType.addItem("username");
		
		vipList.addItem("不是VIP","0");
		vipList.addItem("VIP1","1");
		vipList.addItem("VIP2","2");
		vipList.addItem("VIP3","3");
		
		searchKey.getElement().addClassName("search-query");
		
		nickNameBtn.getElement().addClassName("btn btn-warning");
		nickNameBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				String nickName = nickNameBox.getValue();
				if(Window.confirm("修改nickName为：" + nickName)){
					updateNickName(nickName);
				}
			}
		});
		
		vipBtn.getElement().addClassName(" btn btn-warning");
		vipBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String vip = vipList.getValue(vipList.getSelectedIndex());
				if(Window.confirm("设置vip为：" + vip)){
					setVip(Integer.parseInt(vip));
				}
			}
		});
		
		passwordBtn.getElement().addClassName("btn btn-warning");
		passwordBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				String password = passwordBox.getValue();
				if(Window.confirm("修改password为：" + password)){
					updatePassword(password);
				}
			}
		});
		
	}


	@UiHandler("serchBtn")
	void onClick(ClickEvent e) {
		String str = searchKey.getText();
		String type = searchType.getValue(searchType.getSelectedIndex());
		JSONObject json = new JSONObject();
		if(str == null || str.equals("")){
			Window.alert("请输入要查询的值");
			return;
		}
		int monetId = 0;
		if(type.equals("monetid")){
			try {
				monetId = Integer.parseInt(str);
				json.put("monetId", new JSONNumber(monetId));
			} catch (NumberFormatException exception) {
				Window.alert("请输入一个monetid数字");
				return;
			}
		}else{
			json.put("name", new JSONString(str));
		}
		json.put("type", new JSONString(type));

		loadFisher(json.toString());
	}

	
	
	
	private void loadFisher(String json) {
		service.update(json, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				JSONValue fisher = JSONParser.parseStrict(result);
				showFisher(fisher);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("网络失败!");
			}
		});
	}
	
	private void showFisher(JSONValue fisher){
		JSONObject obj = fisher.isObject();
		
		
		System.out.println(obj);
		int monetId = 0;
		String nickName = null;;
		String userName = null;
		int  vip = 0;
		
		try{
			monetId = Integer.parseInt(obj.get("monetId").isNumber().toString());
			nickName = obj.get("nickName").isString().stringValue();
			userName = obj.get("userName").isString().stringValue();
			vip = (int)obj.get("vip").isNumber().doubleValue();
			
		}
		catch(Exception e){
			Window.alert("no fisher be found!");
		}
		
		monetidBox.setValue(monetId +"");
		nickNameBox.setValue(nickName);
		passwordBox.setValue("******");
		userNameBox.setValue(userName);
		
		vipList.setSelectedIndex(vip);
		
	}
	
	private void updateNickName(String nickName){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("nickName", new JSONString(nickName));
		json.put("monetId", new JSONNumber(Integer.parseInt(monetidBox.getValue())));
		uodateFisher(json.toString());
	}
	private void updatePassword(String Password){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("password", new JSONString(Password));
		json.put("monetId", new JSONNumber(Integer.parseInt(monetidBox.getValue())));
		uodateFisher(json.toString());
	}
	
	private void setVip(int vip_lv){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("vip", new JSONNumber(vip_lv));
		json.put("monetId", new JSONNumber(Integer.parseInt(monetidBox.getValue())));
		uodateFisher(json.toString());
	}
	
	private void uodateFisher(String json){
		service.update(json.toString(), new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				JSONValue rel = JSONParser.parseStrict(result);
				JSONObject obj = rel.isObject();
				int num = Integer.parseInt(obj.get("ret").isNumber().toString());
				if(num == 1){
					Window.alert("修改成功");
				}else{
					Window.alert("修改失败");
				}
			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("服务器错误!");
			}
		});
	}
}
