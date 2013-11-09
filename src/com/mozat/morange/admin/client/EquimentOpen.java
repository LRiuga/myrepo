package com.mozat.morange.admin.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class EquimentOpen extends Composite  {

	private static EquimentOpenUiBinder uiBinder = GWT.create(EquimentOpenUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	interface EquimentOpenUiBinder extends UiBinder<Widget, EquimentOpen> {
	}

	@UiField FlexTable eList;
	@UiField Button saveBtn;
	Map<Integer,Integer> levelMap = new HashMap<Integer, Integer>();
	Map<Integer,Integer> levelMap2 = new HashMap<Integer, Integer>();
	Map<Integer,String> pinMap = new HashMap<Integer, String>();
	Map<String,Integer> pinMap2 = new HashMap<String, Integer>();
	List<String> list = new ArrayList<String>();
	public EquimentOpen() {
		initWidget(uiBinder.createAndBindUi(this));
		eList.setText(0, 0, "等级\\品质");
		eList.setText(1, 0, "1级");
		eList.setText(2, 0, "10级");
		eList.setText(3, 0, "20级");
		eList.setText(4, 0, "25级");
		eList.setText(5, 0, "30级");
		eList.setText(6, 0, "35级");
		eList.setText(7, 0, "40级");
		eList.setText(8, 0, "45级");
		eList.setText(9, 0, "50级");
		eList.setText(10, 0, "60级");
		eList.setText(11, 0, "70级");
		eList.setText(12, 0, "80级");
		eList.setText(13, 0, "90级");
		eList.setText(14, 0, "100级");
		
		eList.setText(0, 1, "白色");
		eList.setText(0, 2, "绿色");
		eList.setText(0, 3, "蓝色");
		eList.setText(0, 4, "紫色");
		eList.setText(0, 5, "橙色");
		eList.setText(0, 6, "金色");
		
		levelMap.put(1, 1);
		levelMap.put(2, 10);
		levelMap.put(3, 20);
		levelMap.put(4, 25);
		levelMap.put(5, 30);
		levelMap.put(6, 35);
		levelMap.put(7, 40);
		levelMap.put(8, 45);
		levelMap.put(9, 50);
		levelMap.put(10, 60);
		levelMap.put(11, 70);
		levelMap.put(12, 80);
		levelMap.put(13, 90);
		levelMap.put(14, 100);
		
		levelMap2.put(1, 1);
		levelMap2.put(10, 2);
		levelMap2.put(20, 3);
		levelMap2.put(25, 4);
		levelMap2.put(30, 5);
		levelMap2.put(35, 6);
		levelMap2.put(40, 7);
		levelMap2.put(45, 8);
		levelMap2.put(50, 9);
		levelMap2.put(60, 10);
		levelMap2.put(70, 11);
		levelMap2.put(80, 12);
		levelMap2.put(90, 13);
		levelMap2.put(100, 14);
		
		pinMap.put(1,"white");
		pinMap.put(2,"green");
		pinMap.put(3,"blue");
		pinMap.put(4,"purple");
		pinMap.put(5,"orange");
		pinMap.put(6,"gold");
		
		pinMap2.put("white",1);
		pinMap2.put("green",2);
		pinMap2.put("blue",3);
		pinMap2.put("purple",4);
		pinMap2.put("orange",5);
		pinMap2.put("gold",6);
		
		for(int i = 1;i<=14;i++){
			for(int j =1;j<=6;j++){
				eList.setWidget(i, j, new CheckBox());
			}
		}
		saveBtn.getElement().addClassName("btn btn-primary");
		
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				list.clear();
				for(int i = 1;i<=9;i++){
					for(int j =1;j<6;j++){
						CheckBox cb = (CheckBox)eList.getWidget(i, j);
						if(cb.getValue()){
							list.add(levelMap.get(i)+":"+pinMap.get(j));
						}
					}
				}
				saveEquipment(list);
			}
		});
		getEquipment();
	}

	private void getEquipment() {
		service.listEquipment(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> list) {
				setEquipment(list);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("错误：" + arg0.toString());
			}
		});
	}

	protected void setEquipment(List<String> list) {
		for(String s: list){
			String[] a = s.split(":");
			int level = Integer.parseInt(a[0]);
			CheckBox cb = (CheckBox)eList.getWidget(levelMap2.get(level), pinMap2.get(a[1]));
			cb.setValue(true);
		}
	}

	protected void saveEquipment(List<String> list){
		System.out.println(list);
		service.saveEquipment(list, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean ret) {
				if(ret){
					Window.alert("保存成功");
				}else{
					Window.alert("保存失败");
				}
			}
			
			@Override
			public void onFailure(Throwable t) {
				Window.alert("错误:" +t.toString());
			}
		});
	}
}
