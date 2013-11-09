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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EquipmentLuckyDraw extends Composite  {

	private static EquipmentLuckyDrawUiBinder uiBinder = GWT.create(EquipmentLuckyDrawUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	interface EquipmentLuckyDrawUiBinder extends UiBinder<Widget, EquipmentLuckyDraw> {
	}
	
	@UiField
	DateBox fromDate;
	@UiField
	Button saveBtn;
	@UiField
	CheckBox openCb;
	
	public EquipmentLuckyDraw() {
		initWidget(uiBinder.createAndBindUi(this));
		saveBtn.getElement().setClassName("span1 btn btn-primary");
		fromDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));

		setEquipmentLuckyDraw();
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveEquipmentLuckyDraw();
			}
		});
	}
	private void setEquipmentLuckyDraw() {
		service.listEquipmentLuckyDraw(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String s) {
				if (s != null) {
					String[] a = s.split("_");
					try {
						Date date = df.parse(a[0]);
						fromDate.setValue(date);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if("1".equals(a[1])){
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
	
	protected void saveEquipmentLuckyDraw() {
		Date date = fromDate.getValue();
		System.out.println(date);
		int status = 0;
		if(openCb.getValue()){
			status = 1;
		}
		service.saveEquipmentLuckyDraw(df.format(date)+"_" + status, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer i) {
				if(i> 0){
					Window.alert("保存成功");
				}else{
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
