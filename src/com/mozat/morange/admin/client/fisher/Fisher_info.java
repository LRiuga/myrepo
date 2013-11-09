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

public class Fisher_info extends Composite {

	interface FisherUiBinder extends UiBinder<Widget, Fisher_info> {
	}
	private final FisherDataServiceAsync service = GWT.create(FisherDataService.class);
	private static FisherUiBinder uiBinder = GWT.create(FisherUiBinder.class);

	@UiField
	Button serchBtn,goldBtn,pearlBtn,sapphireBtn;

	@UiField
	ListBox searchType;
	
	@UiField
	TextBox searchKey,goldBox,sapphireBox,pearlBox,monetidBox,userNameBox;
	
	public Fisher_info() {
		initWidget(uiBinder.createAndBindUi(this));
		
		serchBtn.getElement().addClassName("btn btn-primary");
		
		searchType.addItem("monetid");
		searchType.addItem("username");
		
		searchKey.getElement().addClassName("search-query");
		
		goldBtn.getElement().addClassName("btn btn-warning");
		goldBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String goldstr = goldBox.getValue();
				Long gold = Long.parseLong(goldstr); 
				if(Window.confirm("修改gold为：" + gold)){
					updateGold(gold);
				}
			}
		});
		
		
		sapphireBtn.getElement().addClassName("btn btn-warning");
		sapphireBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String sapphire = sapphireBox.getValue();
				int sapp = Integer.parseInt(sapphire);
				if(Window.confirm("修改sapphire为：" + sapphire)){
					updateSapphire(sapp);
				}
			}
		});
		
		pearlBtn.getElement().addClassName("btn btn-warning");
		pearlBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String pearl = pearlBox.getValue();
				int pearls = Integer.parseInt(pearl);
				if(Window.confirm("修改pearl为：" + pearl)){
					updatePearls(pearls);
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
		service.update(json.toString(), new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				JSONValue fisher = JSONParser.parseStrict(result);
				showFisher(fisher);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("网络不通!");
			}
		});
	}
	
	private void showFisher(JSONValue fisher){
		JSONObject obj = fisher.isObject();
		
		
		
		
		
		int monetId = 0;
		String userName = null;
		Long gold = 0L ;
		int sapphire = 0;
		int pearls = 0;
		
		try{
			monetId = Integer.parseInt(obj.get("monetId").isNumber().toString());
			userName = obj.get("userName").isString().stringValue();
			gold = Long.parseLong(obj.get("gold").isNumber().toString());
			sapphire = Integer.parseInt(obj.get("sapphire").isNumber().toString());
			pearls = Integer.parseInt(obj.get("pearls").isNumber().toString());
			
		}
		catch(Exception e){
			Window.alert("no fisher be found!");
		}
		
		goldBox.setValue(gold + "");
		sapphireBox.setValue(sapphire + "");
		pearlBox.setValue(pearls + "");
		monetidBox.setValue(monetId +"");
		userNameBox.setValue(userName);
		
	}
	
	private void updateGold(Long Gold){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("gold", new JSONNumber(Gold));
		json.put("monetId", new JSONNumber(Integer.parseInt(monetidBox.getValue())));
		
		uodateFisher(json.toString());
	}
	private void updateSapphire(int Sapphire){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("sapphire", new JSONNumber(Sapphire));
		json.put("monetId", new JSONNumber(Integer.parseInt(monetidBox.getValue())));
		uodateFisher(json.toString());
	}
	private void updatePearls(int pearls){
		JSONObject json = new JSONObject();
		json.put("type", new JSONString("updateFisher"));
		json.put("pearls", new JSONNumber(pearls));
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
