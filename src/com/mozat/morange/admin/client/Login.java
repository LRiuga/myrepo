package com.mozat.morange.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Login extends Composite {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	
	private final LoginServiceAsync service = GWT.create(LoginService.class);
	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	@UiField
	TextBox userBox;

	@UiField
	Button loginBtn, resetBtn;
	
	@UiField
	PasswordTextBox passBox;
	
	String user = null;

	public Login() {
		initWidget(uiBinder.createAndBindUi(this));
		loginBtn.getElement().addClassName("btn btn-primary");
		loginBtn.addClickHandler(saveHander);
		resetBtn.getElement().addClassName("btn ");
		resetBtn.addClickHandler(resetHander);
	}

	ClickHandler saveHander = new ClickHandler() {

		@Override
		public void onClick(ClickEvent arg0) {
			String userName = userBox.getValue();
			String password = passBox.getValue();
			user = userName;
			service.login(userName, password, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer i) {
					if(i < 3){
						Window.alert("密码或者用户名错误");
					}else{
						Menu.setUserInfo(user);
						Menu.dialogbox.hide();
					}
				}
				
				@Override
				public void onFailure(Throwable msg) {
					Window.alert(msg.toString());
				}
			});
		}
	};

	ClickHandler resetHander = new ClickHandler() {

		@Override
		public void onClick(ClickEvent arg0) {
			userBox.setValue("");
			passBox.setValue("");
		}
	};
}
