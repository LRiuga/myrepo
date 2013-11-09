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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class FunctionOpen extends Composite {

	private static FunctionOpenUiBinder uiBinder = GWT.create(FunctionOpenUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	interface FunctionOpenUiBinder extends UiBinder<Widget, FunctionOpen> {
	}

	@UiField FlexTable eList;
	@UiField Button saveBtn;
	
	CheckBox cb1 = new CheckBox();
	CheckBox cb2 = new CheckBox();
	
	RadioButton s11,s12;
	public FunctionOpen() {
		initWidget(uiBinder.createAndBindUi(this));
		saveBtn.getElement().addClassName("btn btn-primary");
		eList.setText(0, 0, "功能点");
		eList.setText(0, 1, "开放");
		
		eList.setText(1, 0, "强化石");
		eList.setText(2, 0, "周三竞技场双倍");
		eList.setText(3, 0, "连续签到");
		eList.setText(4, 0, "含强化石");
		
		
		
		s11 = new RadioButton("s1");
		s12 = new RadioButton("s1");
		
		eList.setWidget(1, 1, cb1);
		eList.setWidget(2, 1, cb2);
		eList.setWidget(3, 1, s11);
		eList.setWidget(4, 1, s12);
		
		saveBtn.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				List<Integer> list = new ArrayList<Integer>();
				if(cb1.getValue()){
					list.add(11);
				}else{
					list.add(10);
				}
				if(cb2.getValue()){
					list.add(21);
				}else{
					list.add(20);
				}
				if(s11.getValue()){
					list.add(30);
				}else{
					list.add(31);
				}
				saveFunction(list);
			}
		});
		getFunction();
		
	}
	
	protected void saveFunction(List<Integer> list) {
		service.saveFunction(list, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean succ) {
				if(succ){
					Window.alert("保存成功");
				}else{
					Window.alert("保存失败");
				}
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("error:" + arg0.toString());
			}
		});
	}

	private void getFunction(){
		service.listFunction(new AsyncCallback<List<Integer>>() {
			
			@Override
			public void onSuccess(List<Integer> list) {
				setFunction(list);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}

	protected void setFunction(List<Integer> list) {
		for(int i :list){
			if(i == 11){
				cb1.setValue(true);
			}
			if(i == 21){
				cb2.setValue(true);
			}
			if(i == 30){
				s12.setValue(true);
			}
			if(i == 31){
				s11.setValue(true);
			}
		}
	}

}
