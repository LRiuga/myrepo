package com.mozat.morange.admin.client;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Arean extends Composite  {

	private static AreanUiBinder uiBinder = GWT.create(AreanUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	interface AreanUiBinder extends UiBinder<Widget, Arean> {
	}
	
	@UiField
	TextBox tb11,tb12,tb21,tb22,tb31,tb32,tb41,tb42,tb51,tb52,tb61,tb62,tb71,tb72,tb81,tb82;
	
	@UiField 
	CheckBox z1,z2,z3,z4,z5,z6,z7,z8;
	
	@UiField
	Button s1,s2,s3,s4,s5,s6,s7,s8;
	
	private int min =1,max=10,visiable = 0;
	
	public Arean() {
		initWidget(uiBinder.createAndBindUi(this));
		s1.getElement().addClassName("btn btn-primary");
		s2.getElement().addClassName("btn btn-primary");
		s3.getElement().addClassName("btn btn-primary");
		s4.getElement().addClassName("btn btn-primary");
		s5.getElement().addClassName("btn btn-primary");
		s6.getElement().addClassName("btn btn-primary");
		s7.getElement().addClassName("btn btn-primary");
		s8.getElement().addClassName("btn btn-primary");
		getAllArean();
		
		s1.addClickHandler(clickHandle1);
		s2.addClickHandler(clickHandle2);
		s3.addClickHandler(clickHandle3);
		s4.addClickHandler(clickHandle4);
		s5.addClickHandler(clickHandle5);
		s6.addClickHandler(clickHandle6);
		s7.addClickHandler(clickHandle7);
		s8.addClickHandler(clickHandle8);
		
		tb11.getElement().addClassName("span1");
		tb21.getElement().addClassName("span1");
		tb31.getElement().addClassName("span1");
		tb41.getElement().addClassName("span1");
		tb51.getElement().addClassName("span1");
		tb61.getElement().addClassName("span1");
		tb71.getElement().addClassName("span1");
		tb81.getElement().addClassName("span1");
		
		tb12.getElement().addClassName("span1");
		tb22.getElement().addClassName("span1");
		tb32.getElement().addClassName("span1");
		tb42.getElement().addClassName("span1");
		tb52.getElement().addClassName("span1");
		tb62.getElement().addClassName("span1");
		tb72.getElement().addClassName("span1");
		tb82.getElement().addClassName("span1");
	}
	
	
	ClickHandler clickHandle1 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z1.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb11.getValue());
				max = Integer.parseInt(tb12.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(1,min,max,visiable);
		}
	};
	ClickHandler clickHandle2 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z2.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb21.getValue());
				max = Integer.parseInt(tb22.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(2,min,max,visiable);
		}
	};
	ClickHandler clickHandle3 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z3.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb31.getValue());
				max = Integer.parseInt(tb32.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(3,min,max,visiable);
		}
	};
	ClickHandler clickHandle4 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z4.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb41.getValue());
				max = Integer.parseInt(tb42.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(4,min,max,visiable);
		}
	};
	ClickHandler clickHandle5 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z5.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb51.getValue());
				max = Integer.parseInt(tb52.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(5,min,max,visiable);
		}
	};
	ClickHandler clickHandle6 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z6.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb61.getValue());
				max = Integer.parseInt(tb62.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(6,min,max,visiable);
		}
	};
	ClickHandler clickHandle7 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z7.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb71.getValue());
				max = Integer.parseInt(tb72.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(7,min,max,visiable);
		}
	};
	ClickHandler clickHandle8 = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(z8.getValue()){
				visiable = 1;
			}else{
				visiable = 0;
			}
			
			try {
				min = Integer.parseInt(tb81.getValue());
				max = Integer.parseInt(tb82.getValue());
			} catch (NumberFormatException e) {
				Window.alert("请输入正确的数字，不能为字母或者其他");
			}
			saveArean(8,min,max,visiable);
		}
	};
	
	private void getAllArean(){
		service.getAllArean( new AsyncCallback<Map<String,Integer>>() {
			
			@Override
			public void onSuccess(Map<String, Integer> map) {
				setArean(map);
			}
			
			@Override
			public void onFailure(Throwable tt) {
				Window.alert("错误："+ tt);
			}
		});
	}

	protected void saveArean(int id, int minlevel, int maxlevel, int visiable) {
		service.saveArean(id, minlevel, maxlevel, visiable, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer i) {
				if(i>0){
					Window.alert("保存成功");
				}else{
					Window.alert("保存失败");
				}
			}
			
			@Override
			public void onFailure(Throwable t) {
				Window.alert("error:" + t.toString());
			}
		});
	}

	protected void setArean(Map<String, Integer> map) {
		tb11.setText(map.get("1_minLevel") + "");
		tb21.setText(map.get("2_minLevel") + "");
		tb31.setText(map.get("3_minLevel") + "");
		tb41.setText(map.get("4_minLevel") + "");
		tb51.setText(map.get("5_minLevel") + "");
		tb61.setText(map.get("6_minLevel") + "");
		tb71.setText(map.get("7_minLevel") + "");
		tb81.setText(map.get("8_minLevel") + "");
		
		tb12.setText(map.get("1_maxLevel") + "");
		tb22.setText(map.get("2_maxLevel") + "");
		tb32.setText(map.get("3_maxLevel") + "");
		tb42.setText(map.get("4_maxLevel") + "");
		tb52.setText(map.get("5_maxLevel") + "");
		tb62.setText(map.get("6_maxLevel") + "");
		tb72.setText(map.get("7_maxLevel") + "");
		tb82.setText(map.get("8_maxLevel") + "");
		
		if(map.get("1_isPublic")==1){
			z1.setValue(true);
		}else{
			z1.setValue(false);
		}
		if(map.get("2_isPublic")==1){
			z2.setValue(true);
		}else{
			z2.setValue(false);
		}
		if(map.get("3_isPublic")==1){
			z3.setValue(true);
		}else{
			z3.setValue(false);
		}
		if(map.get("4_isPublic")==1){
			z4.setValue(true);
		}else{
			z4.setValue(false);
		}
		if(map.get("5_isPublic")==1){
			z5.setValue(true);
		}else{
			z5.setValue(false);
		}
		if(map.get("6_isPublic")==1){
			z6.setValue(true);
		}else{
			z6.setValue(false);
		}
		if(map.get("7_isPublic")==1){
			z7.setValue(true);
		}else{
			z7.setValue(false);
		}
		if(map.get("8_isPublic")==1){
			z8.setValue(true);
		}else{
			z8.setValue(false);
		}
	}
}
