package com.mozat.morange.admin.client;

import java.util.Date;

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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.mozat.morange.admin.client.activity.ActivityListService;
import com.mozat.morange.admin.client.activity.ActivityListServiceAsync;

public class Cronk extends Composite  {

	private static CronkUiBinder uiBinder = GWT.create(CronkUiBinder.class);
	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	interface CronkUiBinder extends UiBinder<Widget, Cronk> {
	}
	
	@UiField TextBox activityId,huangId;
	@UiField DateBox toDate;
	@UiField CheckBox openCb;
	@UiField Button saveBtn;
	@UiField TextArea monetId;
	public Cronk() {
		initWidget(uiBinder.createAndBindUi(this));
		
		activityId.getElement().addClassName("span1");
		huangId.getElement().addClassName("span1");
		toDate.getElement().addClassName("span3");
		toDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		saveBtn.getElement().addClassName("span1 btn btn-primary");
		monetId.setEnabled(false);
		monetId.setWidth("500px");
		monetId.setHeight("100px");
		openCb.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				if(openCb.getValue()){
					monetId.setEnabled(true);
				}else{
					monetId.setEnabled(false);
				}
			}
		});
		
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String aid = activityId.getText();
				int aaid = 0;
				try {
					aaid = Integer.parseInt(aid);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					activityId.setFocus(true);
					return;
				}
				
				if(aaid < 0 ){
					Window.alert("请输入一个大于0的数字");
					activityId.setFocus(true);
					return;
				}
				String hid = huangId.getText();
				int hhaid = 0;
				try {
					hhaid = Integer.parseInt(hid);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					huangId.setFocus(true);
					return;
				}
				
				if(hhaid < 0 ){
					Window.alert("请输入一个大于0的数字");
					huangId.setFocus(true);
					return;
				}
				
				Date date = toDate.getValue();
				if(date == null){
					Window.alert("请输入一个时间");
					toDate.setFocus(true);
					return;
				}
				
				String mid = "";
				if(openCb.getValue()){
					mid = monetId.getValue();
					String a[] = mid.split(",");
					for(String s: a){
						try {
							int m = Integer.parseInt(s);
							if(m<=0){
								Window.alert("请输入一个大于0的monetid");
								monetId.setFocus(true);
								return;
							}
						} catch (NumberFormatException e) {
							Window.alert("monetid错误,请检查，多个数字以逗号隔开");
							monetId.setFocus(true);
							return;
						}
					}
					
				}
				
				
				saveHuang(aaid,hhaid,date,mid);
			}
		});
	}
	protected void saveHuang(int aaid, int hhaid, Date date, String mid) {
		service.saveHuang(aaid, hhaid, date, mid, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer i) {
				if(i==-1){
					Window.alert("查找300名错误 ");
				}
				if(i==-2){
					Window.alert("monetid输入错误 ");
				}
				if(i==-3){
					Window.alert("插入小皇冠错误 ");
				}
				if(i==-4){
					Window.alert("插入分数错误 ");
				}
				if(i==0){
					Window.alert("成功 ");
				}
				
			}
			
			@Override
			public void onFailure(Throwable t) {
				Window.alert(t.toString());
			}
		});
	}

	

}
