package com.mozat.morange.admin.client.black;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class BlackEdit extends Composite  {

	private static BlackEditUiBinder uiBinder = GWT
			.create(BlackEditUiBinder.class);

	interface BlackEditUiBinder extends UiBinder<Widget, BlackEdit> {
	}

	public BlackEdit() {
		initWidget(uiBinder.createAndBindUi(this));
		expireDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		saveBtn.getElement().addClassName("span1 btn btn-primary offset1");
	}
	
	@UiField
	DateBox expireDate;
	
	@UiField
	Button saveBtn;
	
	@UiField
	TextBox monetidBox;
	
	@UiField
	TextArea reasonBox;
	
	@UiField
	RadioButton dateRadio1,dateRadio2;
}
