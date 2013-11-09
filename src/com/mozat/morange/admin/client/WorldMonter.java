package com.mozat.morange.admin.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class WorldMonter extends Composite  {

	private static WorldMonterUiBinder uiBinder = GWT.create(WorldMonterUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	interface WorldMonterUiBinder extends UiBinder<Widget, WorldMonter> {
	}

	@UiField
	TextBox date,time,aid;
	
	@UiField
	
	ListBox weaponType;
	@UiField
	Button saveBtn;
	//@UiField
	//CheckBox autoCb;
	@UiField
	TextArea monetidArea;
	
	public WorldMonter() {
		initWidget(uiBinder.createAndBindUi(this));
		date.getElement().addClassName("span1");
		time.getElement().addClassName("span1");
		aid.getElement().addClassName("span1");
		weaponType.getElement().addClassName("span2");
		saveBtn.getElement().addClassName("span1 btn btn-primary");
		
		weaponType.addItem("M1","22");
		weaponType.addItem("M2","23");
		weaponType.addItem("M3","24");
		weaponType.addItem("M4","25");
		weaponType.addItem("M5","26");
		weaponType.addItem("M6","27");
		weaponType.addItem("M7","28");
		weaponType.addItem("M8","29");
		weaponType.addItem("M9","30");
		
		monetidArea.setWidth("500px");
		//monetidArea.setEnabled(false);
		
		/*autoCb.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				if(autoCb.getValue()){
					monetidArea.setEnabled(true);
				}else{
					monetidArea.setText("");
					monetidArea.setEnabled(false);
				}
			}
		});*/
		
		getWorldMonterConfig();
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String dnum = date.getText();
				if("".equals(dnum) || null==dnum){
					Window.alert("请输入持续天数");
					return;
				}
				try {
					Integer.parseInt(dnum);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					date.setFocus(true);
					return;
				}
				
				String activityId = aid.getText();
				if("".equals(activityId) || null==activityId){
					Window.alert("请输入活动id");
					return;
				}
				try {
					Integer.parseInt(activityId);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					aid.setFocus(true);
					return;
				}
				
				String tnum = time.getText();
				if("".equals(tnum) || null==tnum){
					Window.alert("请输入打怪次数");
					return;
				}
				
				try {
					Integer.parseInt(tnum);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					time.setFocus(true);
					return;
				}
				
				String monetid = monetidArea.getText();
				if("".equals(monetid) || null==monetid){
					Window.alert("请输入monetid");
					return;
				}
				
				try {
					String[] a = monetid.split(",");
					for(String s:a){
						Integer.parseInt(s);
					}
					
				} catch (NumberFormatException e) {
					Window.alert("请输入数字,多个以逗号隔开");
					monetidArea.setFocus(true);
					return;
				}
				
				List<String> list = new ArrayList<String>();
				list.add(dnum);
				list.add(tnum);
				list.add(weaponType.getValue(weaponType.getSelectedIndex()));
				list.add(activityId);
				list.add(monetid);
				
				saveWorldMonster(list);
				
			}
		});
	}

	protected void saveWorldMonster(List<String> list) {
			service.saveWorldMonster(list, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer i) {
					if(i==1){
						Window.alert("失败1： 打怪持续天数设置失败，其他没继续");
					}
					if(i==2){
						Window.alert("失败2： 打怪次数失败，其他没继续");
					}
					if(i==3){
						Window.alert("失败3： 打怪炸弹类型设置失败，其他没继续");
					}
					if(i==4){
						Window.alert("失败4： monetid有错误");
					}
					if(i==5){
						Window.alert("失败5：准备数据失败");
					}
					
					if(i==6){
						Window.alert("失败6：生成怪物失败");
					}
					
					if(i==0){
						Window.alert("成功");
					}
					
					
				}
				
				@Override
				public void onFailure(Throwable t) {
					Window.alert("Error:"+ t.toString());
				}
			});
	}

	private void getWorldMonterConfig() {
		service.getWorldMonterConfig(new AsyncCallback<List<Integer>>() {
			
			@Override
			public void onSuccess(List<Integer> list) {
				setWorldMonterConfig(list);
			}
			
			@Override
			public void onFailure(Throwable t) {
				Window.alert("ERROR:" + t.toString());
			}
		});
	}

	protected void setWorldMonterConfig(List<Integer> list) {
		date.setText(list.get(0)+"");
		time.setText(list.get(1)+"");
		weaponType.setSelectedIndex(list.get(2)-22);
	}


}
