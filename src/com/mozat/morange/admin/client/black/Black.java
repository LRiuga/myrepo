package com.mozat.morange.admin.client.black;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.mozat.morange.admin.client.fisher.FisherDataService;
import com.mozat.morange.admin.client.fisher.FisherDataServiceAsync;

public class Black extends Composite  {

	interface BlackUiBinder extends UiBinder<Widget, Black> {
	}
	private final FisherDataServiceAsync service = GWT.create(FisherDataService.class);
	private final BlackListServiceAsync service2 = GWT.create(BlackListService.class);
	private static BlackUiBinder uiBinder = GWT.create(BlackUiBinder.class);
	
	@UiField
	Button serchBtn,blackBtn;

	@UiField
	TextBox searchKey,monetidBox,goldBox,nickNameBox,sappireBox,userNameBox,pearlBox,reasonBox;
	
	@UiField
	RadioButton dateRadio1,dateRadio2;
	
	@UiField
	DateBox blackDate;
	
	@UiField
	ListBox searchType;
	
	public Black() {
		initWidget(uiBinder.createAndBindUi(this));
		
		serchBtn.getElement().addClassName("btn btn-primary ");
		
		searchType.addItem("monetid");
		searchType.addItem("username");
		dateRadio1.setValue(true);
		blackDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		blackBtn.getElement().addClassName("btn btn-danger offset2");
		
		blackBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String reason = reasonBox.getValue();
				String date = null;
				String monetidStr = monetidBox.getValue();
				int monetid = 0;
				if(monetidStr == null || "".equals(monetidStr)){
					searchKey.setFocus(true);
					return;
				}else{
					monetid = Integer.parseInt(monetidStr);
				}
				if(reason== null || "".equals(reason)){
					Window.alert("请输入理由");
					reasonBox.setFocus(true);
					return;
				}
				if(dateRadio1.getValue()){
					Date bDate = blackDate.getValue();
					if(bDate == null){
						Window.alert("请输入日期");
						blackDate.setFocus(true);
						return;
					}
					date = DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss").format(bDate);
				}else{
					date = "2999-12-31 00:00:00";
				}
				JSONObject json = new JSONObject();
				json.put("monetId", new JSONNumber(monetid));
				json.put("reason", new JSONString(reason));
				json.put("expire", new JSONString(date));
				addBlack(json.toString());
			}
		});
	}
	
	private void loadFisher(String string) {
		service.update(string, new AsyncCallback<String>() {
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

	protected void showFisher(JSONValue fisher) {
		JSONObject obj = fisher.isObject();
		int monetId = 0;
		String nickName = null;;
		String userName = null;
		Long gold = 0L ;
		int sapphire = 0;
		int pearls = 0;
		
		try{
			monetId = Integer.parseInt(obj.get("monetId").isNumber().toString());
			nickName = obj.get("nickName").isString().stringValue();
			userName = obj.get("userName").isString().stringValue();
			gold = Long.parseLong(obj.get("gold").isNumber().toString());
			sapphire = Integer.parseInt(obj.get("sapphire").isNumber().toString());
			pearls = Integer.parseInt(obj.get("pearls").isNumber().toString());
		}
		catch(Exception e){
			Window.alert("no fisher be found!");
		}
		
		monetidBox.setValue(monetId +"");
		nickNameBox.setValue(nickName);
		userNameBox.setValue(userName);
		goldBox.setValue(gold + "");
		sappireBox.setValue(sapphire + "");
		pearlBox.setValue(pearls + "");
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
	private void addBlack(String jsonStr) {
		service2.add(jsonStr, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String str) {
				if("true".equals(str)){
					Window.alert("成功");
				}else{
					Window.alert("失败");
				}
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("网络有问题");
			}
		});
	}
}
