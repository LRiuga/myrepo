package com.mozat.morange.admin.client.activity;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ActivitySetting extends Composite {

	interface ActivitySettingUiBinder extends UiBinder<Widget, ActivitySetting> {
	}
	DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	private static ActivitySettingUiBinder uiBinder = GWT.create(ActivitySettingUiBinder.class);

	@UiField
	ListBox activityList;

	@UiField
	DateBox fromDate, toDate, beginDate,endDate;

	@UiField
	Button saveBtn, editBtn;

	@UiField
	TextBox activityId;

	Button editRank = new Button();

	int rewardIndex = 0;

	public ActivitySetting() {
		initWidget(uiBinder.createAndBindUi(this));


		saveBtn.getElement().addClassName("span1 btn btn-primary");
		editBtn.getElement().addClassName("span1 btn btn-primary");

		editBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				fromDate.setEnabled(true);
				toDate.setEnabled(true);
				beginDate.setEnabled(true);
				endDate.setEnabled(true);
			}
		});

		fromDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		toDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		beginDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
		endDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));

		activityList.addItem("AttackFriend");
		activityList.addItem("AttackFriend2");
		activityList.addItem("CrownAttack");
		activityList.addItem("Fishing");
		activityList.addItem("FishingCompetition");
		activityList.addItem("FreeSapphiresEvent");
		activityList.addItem("FreeSapphiresEventEnd");
		activityList.addItem("PetCompetition");
		activityList.addItem("PCRegistration");
		activityList.addItem("PCPreliminary");
		activityList.addItem("PCFinal");
		activityList.addItem("PC32Final");
		activityList.addItem("PetLevelUp");
		activityList.addItem("TribeWar");
		activityList.addItem("TribeWar2");
		activityList.addItem("UpgradeShipyard");
		activityList.addItem("TribeCompetition");
		activityList.addItem("WorldMonster");

	}

	@UiHandler("saveBtn")
	void onClick(ClickEvent e) {
		String activityName = activityList.getItemText(activityList.getSelectedIndex());
		Date fDate = fromDate.getValue();
		System.out.println(fDate);
		System.out.println(df.format(fDate));
		Date tDate = toDate.getValue();
		Date bDate = beginDate.getValue();
		Date eDate = endDate.getValue();
		if (fDate == null) {
			Window.alert("请输入展示开始时间");
			return;
		}
		if (tDate == null) {
			Window.alert("请输入展示结束时间");
			return;
		}
		if (bDate == null) {
			Window.alert("请输入活动开始时间");
			return;
		}
		if (eDate == null) {
			Window.alert("请输入活动结束时间");
			return;
		}
		saveOrUpdate(activityName, fDate, tDate, bDate,eDate);
	}

	private void saveOrUpdate(String activityName, Date fDate, Date tDate, Date bDate,Date eDate) {
		String idStr = activityId.getValue();
		if (idStr == null || "".equals(idStr)) {
			service.add(activityName, fDate, tDate, bDate,eDate, new AsyncCallback<Integer>() {

				@Override
				public void onSuccess(Integer id) {
					if (id > 0) {
						activityId.setValue(id + "");
						fromDate.setEnabled(false);
						toDate.setEnabled(false);
						beginDate.setEnabled(false);
						endDate.setEnabled(false);
					}
				}

				@Override
				public void onFailure(Throwable arg0) {
					Window.alert("保存失败");
				}
			});
		} else {
			int id = Integer.parseInt(idStr);
			service.update(id, activityName, fDate, tDate, bDate,eDate, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean succ) {
					if (succ) {
						fromDate.setEnabled(false);
						toDate.setEnabled(false);
						beginDate.setEnabled(false);
						endDate.setEnabled(false);
					} else {
						Window.alert("保存失败");
					}
				}

				@Override
				public void onFailure(Throwable arg0) {

				}
			});
		}
	}

	

}
