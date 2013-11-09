package com.mozat.morange.admin.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EquipmentDiscount extends Composite {

	private static EquipmentDiscountUiBinder uiBinder = GWT.create(EquipmentDiscountUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");

	interface EquipmentDiscountUiBinder extends UiBinder<Widget, EquipmentDiscount> {
	}

	@UiField
	DateBox fromDate, toDate;
	@UiField
	Button saveBtn, saveBtn2, saveBtn3;
	@UiField
	CheckBox openCb;
	@UiField
	FlexTable eList, eList2;

	Map<Integer,Integer> levelMap = new HashMap<Integer, Integer>();
	Map<Integer,Integer> levelMap2 = new HashMap<Integer, Integer>();
	Map<Integer,String> pinMap = new HashMap<Integer, String>();
	Map<String,Integer> pinMap2 = new HashMap<String, Integer>();

	public EquipmentDiscount() {
		initWidget(uiBinder.createAndBindUi(this));
		saveBtn.getElement().setClassName("span1 btn btn-primary");
		saveBtn2.getElement().setClassName("span1 btn btn-primary");
		saveBtn3.getElement().setClassName("span1 btn btn-primary");
		fromDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss")));
		toDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss")));

		setEquipmentDiscount();
		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveEquipmentDiscount();
			}
		});
		
		saveBtn2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				saveEquipmentDiscountConfig(1);
			}
		});
		
		saveBtn3.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				saveEquipmentDiscountConfig(0);
			}
		});
		
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
		levelMap.put(12,80);
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
		
		pinMap.put(1,"green");
		pinMap.put(2,"blue");
		pinMap.put(3,"purple");
		pinMap.put(4,"orange");
		
		pinMap2.put("green",1);
		pinMap2.put("blue",2);
		pinMap2.put("purple",3);
		pinMap2.put("orange",4);
		
		
		eList.setWidth("100%");
		for (int i = 0; i <= 15; i++) {
			eList.getCellFormatter().setWidth(0, i, "7%");
		}

		eList.setText(0, 0, "单件");
		eList.setText(0, 1, "1 级");
		eList.setText(0, 2, "10级");
		eList.setText(0, 3, "20级");
		eList.setText(0, 4, "25级");
		eList.setText(0, 5, "30级");
		eList.setText(0, 6, "35级");
		eList.setText(0, 7, "40级");
		eList.setText(0, 8, "45级");
		eList.setText(0, 9, "50级");
		eList.setText(0, 10, "60 级");
		eList.setText(0, 11, "70 级");
		eList.setText(0, 12, "80 级");
		eList.setText(0, 13, "90 级");
		eList.setText(0, 14, "100级 ");

		eList.setText(0, 15, "折扣");

		eList.setWidget(1, 0, new HTML("<font color='green'>绿色</font>"));
		eList.setWidget(2, 0, new HTML("<font color='blue'>蓝色</font>"));
		eList.setWidget(3, 0, new HTML("<font color='purple'>紫色</font>"));
		eList.setWidget(4, 0, new HTML("<font color='orange'>橙色</font>"));

		for (int i = 1; i <= 15; i++) {
			for (int j = 1; j <= 4; j++) {
				eList.setWidget(j, i, new CheckBox());

			}
		}
		for (int i = 1; i <= 4; i++) {
			TextBox tb = new TextBox();
			tb.getElement().addClassName("span1");
			eList.setWidget(i, 15, tb);
		}
        
		getEquipmentDiscountConfig(1);
		eList2.setWidth("100%");
		for (int i = 0; i <= 15; i++) {
			eList2.getCellFormatter().setWidth(0, i, "7%");
		}

		eList2.setText(0, 0, "套装");
		eList2.setText(0, 1, "1 级");
		eList2.setText(0, 2, "10级");
		eList2.setText(0, 3, "20级");
		eList2.setText(0, 4, "25级");
		eList2.setText(0, 5, "30级");
		eList2.setText(0, 6, "35级");
		eList2.setText(0, 7, "40级");
		eList2.setText(0, 8, "45级");
		eList2.setText(0, 9, "50级");
		eList2.setText(0, 10, "60 级");
		eList2.setText(0, 11, "70 级");
		eList2.setText(0, 12, "80 级");
		eList2.setText(0, 13, "90 级");
		eList2.setText(0, 14, "100级 ");

		eList2.setText(0, 15, "折扣");

		eList2.setWidget(1, 0, new HTML("<font color='green'>绿色</font>"));
		eList2.setWidget(2, 0, new HTML("<font color='blue'>蓝色</font>"));
		eList2.setWidget(3, 0, new HTML("<font color='purple'>紫色</font>"));
		eList2.setWidget(4, 0, new HTML("<font color='orange'>橙色</font>"));

		for (int i = 1; i <= 15; i++) {
			for (int j = 1; j <= 4; j++) {
				eList2.setWidget(j, i, new CheckBox());

			}
		}
		for (int i = 1; i <= 4; i++) {
			TextBox tb = new TextBox();
			tb.getElement().addClassName("span1");
			eList2.setWidget(i, 15, tb);
		}
		getEquipmentDiscountConfig(0);
	}

	protected void saveEquipmentDiscountConfig(int type) {
		FlexTable fList = eList ;
		if(type ==  0 ){
			fList = eList2 ;
		}
		List<String> confList = new ArrayList<String>();
		for(int row = 1;row<=4;row++){
			StringBuffer sb = new StringBuffer();
			String color = pinMap.get(row);
			sb.append(color);
			sb.append(":");
			for(int col = 1 ; col<=14;col++){
				CheckBox cb = (CheckBox)fList.getWidget(row,col);
				if(cb.getValue()){
					int level = levelMap.get(col);
					sb.append(level);
					sb.append(",");
				}
			}
			if(sb.toString().endsWith(",")){
				
			}else{
				continue;
			}
			TextBox tb = (TextBox)fList.getWidget(row,15);
			String text = tb.getText();
			try {
				int discountNum = Integer.parseInt(text);
				if(discountNum > 99 || discountNum < 1){
					Window.alert("请输入一个数字在1到99之间的数字");
					tb.setFocus(true);
					return;
				}
				if(sb.toString().endsWith(",")){
					sb.deleteCharAt(sb.length()-1);
					sb.append(":");
					sb.append(discountNum);
					confList.add(sb.toString());
					
				}else{
					continue;
				}
			} catch (NumberFormatException e) {
				Window.alert("请输入一个数字");
				tb.setFocus(true);
				return;
			}
		}
		service.saveEquipmentDiscountConfig(confList, type, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean succ) {
				if(succ){
					Window.alert("保存成功");
				}
			}
			
			@Override
			public void onFailure(Throwable tt) {
				Window.alert(tt.toString());
			}
		});
	}

	private void getEquipmentDiscountConfig(int type) {
		service.listEquipmentDiscountConfig(type, new AsyncCallback<Map<String,List<String>>>() {
			
			@Override
			public void onSuccess(Map<String,List<String>> map) {
				setEquipmentDiscountConfig(map);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}

	protected void setEquipmentDiscountConfig(Map<String,List<String>> map) {
		List<String> openList = map.get("openList");
		List<String> discountList = map.get("discountList");
		Map<String,Integer> thMap = new HashMap<String,Integer>();
		thMap.put("green",1);
		thMap.put("blue",2);
		thMap.put("purple",3);
		thMap.put("orange",4);
		String  type = openList.get(0);
		if("1".equals(type)){
			for(int i = 1;i < openList.size();i++){
				String[] a = openList.get(i).split(":");
				int level = Integer.parseInt(a[0]);
				CheckBox cb = (CheckBox)eList.getWidget( pinMap2.get(a[1]),levelMap2.get(level));
				cb.setValue(true);
			}
			for(String s : discountList ){
				String a[] = s.split(":");
				TextBox tb = (TextBox)eList.getWidget( thMap.get(a[0]),15);
				tb.setText((100-Integer.parseInt(a[1]))+"");
			}
			
			
		}else if("0".equals(type)){
			for(int i = 1;i < openList.size();i++){
				String[] a = openList.get(i).split(":");
				int level = Integer.parseInt(a[0]);
				CheckBox cb = (CheckBox)eList2.getWidget(pinMap2.get(a[1]),levelMap2.get(level));
				cb.setValue(true);
			}
			for(String s : discountList ){
				String a[] = s.split(":");
				TextBox tb = (TextBox)eList2.getWidget(thMap.get(a[0]),15);
				tb.setText((100-Integer.parseInt(a[1]))+"");
			}
		}
	}

	private void setEquipmentDiscount() {
		service.listEquipmentDiscount(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String s) {
				if (s != null) {
					String[] a = s.split("_");
					try {
						Date date = df.parse(a[0]);
						Date endDate = df.parse(a[2]);
						fromDate.setValue(date);
						toDate.setValue(endDate);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if ("1".equals(a[1])) {
						openCb.setValue(true);
					}
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("error:" + arg0.toString());
			}
		});
	}

	protected void saveEquipmentDiscount() {
		Date date = fromDate.getValue();
		Date endDate = toDate.getValue();
		int status = 0;
		if (openCb.getValue()) {
			status = 1;
		}
		service.saveEquipmentDiscount(df.format(date) + "_" + status + "_" + df.format(endDate), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer i) {
				if (i > 0) {
					Window.alert("保存成功");
				} else {
					Window.alert("保存失败");
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert(arg0.toString());
			}
		});
	}
}
