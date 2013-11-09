package com.mozat.morange.admin.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class tribeMonter extends Composite {

	private static tribeMonterUiBinder uiBinder = GWT.create(tribeMonterUiBinder.class);
	private final FriendServiceAsync service = GWT.create(FriendService.class);
	DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss");

	interface tribeMonterUiBinder extends UiBinder<Widget, tribeMonter> {
	}

	// DatePicker datePicker = new DatePicker();
	@UiField
	DateBox fromDate, toDate, toDate2, fromDate2;
	@UiField
	TextBox beginbox2, beginbox, endbox, endbox2;

	@UiField
	CheckBox closeCb;
	@UiField
	Button saveBtn;

	Map<String, Integer> weekNumMap = new HashMap<String, Integer>();
	Map<Integer, String> numWeekMap = new HashMap<Integer, String>();

	public tribeMonter() {
		initWidget(uiBinder.createAndBindUi(this));
		fromDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("HH:mm:ss")));
		fromDate2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("HH:mm:ss")));
		toDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("HH:mm:ss")));
		toDate2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("HH:mm:ss")));
		saveBtn.getElement().addClassName("btn btn-primary ");
		saveBtn.addClickHandler(saveHadle);
		
		fromDate.getElement().addClassName("span2");
		fromDate2.getElement().addClassName("span2");
		toDate.getElement().addClassName("span2");
		toDate2.getElement().addClassName("span2");
		beginbox.getElement().addClassName("span2");
		beginbox2.getElement().addClassName("span2");
		endbox.getElement().addClassName("span2");
		endbox2.getElement().addClassName("span2");
		
		closeCb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (closeCb.getValue()) {
					fromDate.setEnabled(false);
					toDate.setEnabled(false);
					fromDate2.setEnabled(false);
					toDate2.setEnabled(false);
					beginbox.setEnabled(false);
					beginbox2.setEnabled(false);
					endbox.setEnabled(false);
					endbox2.setEnabled(false);
				} else {
					fromDate.setEnabled(true);
					toDate.setEnabled(true);
					fromDate2.setEnabled(true);
					toDate2.setEnabled(true);
					beginbox.setEnabled(true);
					beginbox2.setEnabled(true);
					endbox.setEnabled(true);
					endbox2.setEnabled(true);
				}
			}
		});
		beginbox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent arg0) {
				String input = beginbox.getText();
				if (!input.matches("[0-6]")) {
					Window.alert("请输入0-6");
					beginbox.setText("");
					beginbox.setFocus(true);
				}
			}
		});
		beginbox2.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent arg0) {
				String input = beginbox2.getText();
				if (!input.matches("[0-6]")) {
					Window.alert("请输入0-6");
					beginbox2.setText("");
					beginbox2.setFocus(true);
				}
			}
		});
		endbox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent arg0) {
				String input = endbox.getText();
				if (!input.matches("[0-6]")) {
					Window.alert("请输入0-6");
					endbox.setText("");
					endbox.setFocus(true);
				}
			}
		});
		endbox2.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent arg0) {
				String input = endbox2.getText();
				if (!input.matches("[0-6]")) {
					Window.alert("请输入0-6");
					endbox2.setText("");
					endbox2.setFocus(true);
				}
			}
		});
		weekNumMap.put("Sun", 0);
		weekNumMap.put("Mon", 1);
		weekNumMap.put("Tue", 2);
		weekNumMap.put("Wed", 3);
		weekNumMap.put("Thu", 4);
		weekNumMap.put("Fri", 5);
		weekNumMap.put("Sat", 6);
		numWeekMap.put(0, "Sun");
		numWeekMap.put(1, "Mon");
		numWeekMap.put(2, "Tue");
		numWeekMap.put(3, "Wed");
		numWeekMap.put(4, "Thu");
		numWeekMap.put(5, "Fri");
		numWeekMap.put(6, "Sat");

		getTribeMonter();
	}

	private void getTribeMonter() {
		service.setTribeMonter(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String s) {
				setTribeMonter(s);

			}

			@Override
			public void onFailure(Throwable t) {
				Window.alert(t.toString());
			}
		});
	}

	private void setTribeMonter(String s) {
		if (s != null && "".equals("")) {
			String a[] = s.split(",");
			beginbox.setText(weekNumMap.get(a[0]) + "");
			beginbox2.setText(weekNumMap.get(a[2]) + "");
			endbox.setText(weekNumMap.get(a[4]) + "");
			endbox2.setText(weekNumMap.get(a[6]) + "");

			fromDate.setValue(df.parse(a[1]));
			fromDate2.setValue(df.parse(a[3]));
			toDate.setValue(df.parse(a[5]));
			toDate2.setValue(df.parse(a[7]));
		} else {
			closeCb.setValue(true);
			fromDate.setEnabled(false);
			toDate.setEnabled(false);
			fromDate2.setEnabled(false);
			toDate2.setEnabled(false);
			beginbox.setEnabled(false);
			beginbox2.setEnabled(false);
			endbox.setEnabled(false);
			endbox2.setEnabled(false);
		}

	}

	ClickHandler saveHadle = new ClickHandler() {

		@Override
		public void onClick(ClickEvent arg0) {
			String s = "";
			if (!closeCb.getValue()) {
				String text = null;
				text = beginbox.getText();
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					beginbox.setFocus(true);
					return;
				}else{
					text = numWeekMap.get(Integer.parseInt(text));
				}
				s =  text;

				text = df.format(fromDate.getValue());
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					fromDate.setFocus(true);
					return;
				}
				s = s + "," + text;
				text = endbox.getText();
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					endbox.setFocus(true);
					return;
				}else{
					text = numWeekMap.get(Integer.parseInt(text));
				}
				s = s + "," + text;

				text = df.format(toDate.getValue());
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					toDate.setFocus(true);
					return;
				}
				s = s + "," + text;
				text = beginbox2.getText();
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					beginbox2.setFocus(true);
					return;
				}else{
					text = numWeekMap.get(Integer.parseInt(text));
				}
				s = s + "," + text;

				text = df.format(fromDate2.getValue());
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					fromDate2.setFocus(true);
					return;
				}
				s = s + "," + text;
				text = endbox2.getText();
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					endbox2.setFocus(true);
					return;
				}else{
					text = numWeekMap.get(Integer.parseInt(text));
				}
				s = s + "," + text;

				text = df.format(toDate2.getValue());
				if (text == null || "".equals(text)) {
					Window.alert("不能为空");
					toDate2.setFocus(true);
					return;
				}
				s = s + "," + text;
			}
			
			saveTribeMonter(s);
		}
	};

	protected void saveTribeMonter(String s) {
		service.updateTribeMonter(s, new AsyncCallback<Integer>() {
			
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
				Window.alert("错误："+t.toString());
			}
		});
	}
}
