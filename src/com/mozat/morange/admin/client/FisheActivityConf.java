package com.mozat.morange.admin.client;

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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mozat.morange.admin.client.activity.ActivityListService;
import com.mozat.morange.admin.client.activity.ActivityListServiceAsync;

public class FisheActivityConf extends Composite  {

	private static FisheActivityConfUiBinder uiBinder = GWT.create(FisheActivityConfUiBinder.class);
	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	interface FisheActivityConfUiBinder extends UiBinder<Widget, FisheActivityConf> {
	}
	
	@UiField
	Button addBtn;
	//@UiField
	//Button removeBtn;

	@UiField 
	FlexTable fishActivityList,table;
	private int index = 0;
	CheckBox cb = new CheckBox();
	public FisheActivityConf() {
		initWidget(uiBinder.createAndBindUi(this));
		//removeBtn.getElement().addClassName("btn btn-danger");
		addBtn.getElement().addClassName("btn btn-primary");
		fishActivityList.setWidget(index, 0, cb);
		fishActivityList.setText(index, 1, "Id");
		fishActivityList.setText(index, 2, "活动ID");
		fishActivityList.setText(index, 3, "鱼种");
		fishActivityList.setText(index, 4, "每条鱼增加的分数");
		fishActivityList.setText(index, 5, "每日捕鱼条数");
		fishActivityList.setText(index, 6, "奖励类型");
		fishActivityList.setText(index, 7, "奖励数量");
		
		listFishingConf();
		cb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				boolean v = cb.getValue();
				for (int i = 1; i < fishActivityList.getRowCount(); i++) {
					CheckBox cc = (CheckBox) fishActivityList.getWidget(i, 0);
					cc.setValue(v);
				}
			}
		});
		
		table.setText(0, 0, "活动ID");
		table.setText(0, 1, "鱼种");
		table.setText(0, 2, "每条鱼增加的分数");
		table.setText(0, 3, "每日捕鱼条数");
		table.setText(0, 4, "奖励类型");
		table.setText(0, 5, "奖励数量");
		
		for(int i = 0;i<=5;i++){
			TextBox tb = new TextBox();
			tb.getElement().addClassName("span1");
			table.setWidget(1, i, tb);
		}
		
		addBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				StringBuffer sb = new StringBuffer();
				for(int i = 0;i<=5;i++){
					TextBox tb = (TextBox)table.getWidget(1, i);
					String value = tb.getValue();
					if("".equals(value) || value == null){
						Window.alert("请输入一个数值");
						tb.setFocus(true);
						return;
					}
					
					try {
						Integer.parseInt(value);
					} catch (NumberFormatException e) {
						Window.alert("请输入一个数字");
						tb.setFocus(true);
						return;
					}
					sb.append(value);
					sb.append(",");
				}
				sb.deleteCharAt(sb.toString().length()-1);
				saveFisherConf(sb.toString());
			}
		});
	}

	protected void saveFisherConf(String string) {
		service.saveFisherConf(string, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean succ) {
				if(succ){
					Window.alert("成功");
					FisheActivityConf fisheActivityConf = new FisheActivityConf();
					RootPanel content = RootPanel.get("content");
					content.clear();
					content.add(fisheActivityConf);
				}
			}
			
			@Override
			public void onFailure(Throwable tt) {
				Window.alert(tt.toString());
			}
		});
	}

	private void listFishingConf(){
		service.listFishingConf(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> list) {
				setFisherConf(list);
			}
			
			@Override
			public void onFailure(Throwable tt) {
				Window.alert(tt.toString());
			}
		});
	}

	protected void setFisherConf(List<String> list) {
		if(list != null) {
			for(String s : list){
				String[] a = s.split("_");
				index ++;
				fishActivityList.setWidget(index, 0, new CheckBox());
				for(int i=1;i<=7;i++){
					fishActivityList.setText(index, i, a[i-1]);
				}
			}
		}
	}

}
