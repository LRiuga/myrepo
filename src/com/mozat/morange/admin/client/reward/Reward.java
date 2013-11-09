package com.mozat.morange.admin.client.reward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mozat.morange.admin.client.activity.ActivityListService;
import com.mozat.morange.admin.client.activity.ActivityListServiceAsync;

public class Reward extends Composite {

	interface RewardUiBinder extends UiBinder<Widget, Reward> {
	}

	private final RewardServiceAsync service = GWT.create(RewardService.class);
	private final ActivityListServiceAsync service2 = GWT.create(ActivityListService.class);
	private static RewardUiBinder uiBinder = GWT.create(RewardUiBinder.class);

	@UiField
	ListBox itemName, activityId, itemType;

	@UiField
	TextArea userId;

	@UiField
	TextBox itemCount,activityName;

	@UiField
	Button addBtn, confirm;

	@UiField
	FlexTable reawrdFlexTable;

	int reawrdIndex = 0;
	
	Map<Integer, String> activityIdAndNameMap = new HashMap<Integer, String>(); 
	Map<Integer, String> saveMap = new HashMap<Integer, String>(); 
	
	Map<String, Integer> activityPrizeIdMap = null;
	
	public Reward() {
		initWidget(uiBinder.createAndBindUi(this));
		
		getActivityIdAndName();
		
		activityName.setEnabled(false);
		
		getActivityPrizeId();
		
		itemType.addItem("请选择", "999");
		itemType.addItem("商店物品", "0");
		itemType.addItem("金币", "1");
		itemType.addItem("珍珠", "2");
		itemType.addItem("船", "3");
		itemType.addItem("VipChat", "4");
		itemType.addItem("皇冠", "5");
		itemType.addItem("宠物经验", "6");
		itemType.addItem("家族贡献", "7");
		itemType.addItem("宠物形象卡", "8");
		itemType.addItem("装备", "9");
		itemType.addItem("星玉", "10");
		itemType.addItem("普通强化石", "11");
		itemType.addItem("高级强化石", "12");
		itemType.addItem("超级强化石", "13");
		itemType.addItem("蓝宝石", "14");
		
		itemType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				String typeStr = itemType.getValue(itemType.getSelectedIndex());
				int type  = Integer.parseInt(typeStr);
				itemName.clear();
				if(type == 1 || type == 2  || type ==6 ||type ==7 ||type ==10 ||type ==11 ||type ==12 ||type ==13 || type ==14){
					itemName.addItem("0");
					itemName.setEnabled(false);
				}else if(type==4){
					//getAndSetItemName(type);
					itemName.addItem("普通","1");
					itemName.addItem("家族之王","11");
					itemName.setEnabled(true);
				}else if(type==5){
					itemName.addItem("小皇冠","1");
					itemName.addItem("铜皇冠","2");
					itemName.addItem("银皇冠","3");
					itemName.addItem("金皇冠","4");
					itemName.setEnabled(true);
				}else if(type == 8 ){
					itemName.addItem("三天形象卡","1");
					itemName.addItem("15 天形象卡","2");
					itemName.addItem("永久形象卡","3");
					itemName.setEnabled(true);
				}else{
					getAndSetItemName(type);
					itemName.setEnabled(true);
				}
				
			}
		});


		addBtn.getElement().addClassName("btn btn-primary");
		confirm.getElement().addClassName("btn btn-primary");

		
		
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				saveReward();
			}
		});
		
		reawrdFlexTable.setText(reawrdIndex, 0, "活动id");
		reawrdFlexTable.setText(reawrdIndex, 1, "活动名称");
		reawrdFlexTable.setText(reawrdIndex, 2, "User Id");
		reawrdFlexTable.setText(reawrdIndex, 3, "RewardItemType");
		reawrdFlexTable.setText(reawrdIndex, 4, "RewardItemName");
		reawrdFlexTable.setText(reawrdIndex, 5, "RewardItemCount");
		reawrdFlexTable.setText(reawrdIndex, 6, "Remove");

		addBtn.addClickHandler(new ClickHandler() { 

			@Override
			public void onClick(ClickEvent arg0) {
				String activityStr = activityId.getValue(activityId.getSelectedIndex());
				String aName = activityName.getText();
				String itemTypeStr = itemType.getItemText(itemType.getSelectedIndex());
				String itemTypeId =  itemType.getValue(itemType.getSelectedIndex());
				String itemNameStr = itemName.getItemText(itemName.getSelectedIndex());
				String itemId =  itemName.getValue(itemName.getSelectedIndex());
				String userIdStr = userId.getValue();
				String[] monetids = userIdStr.split(",");
				for (String s : monetids) {
					try {
						Integer.parseInt(s);
					} catch (Exception e) {
						Window.alert("User ID 输入错误，请输入数字，多个用逗号分隔");
						return;
					}
				}
				if (userIdStr == null || "".equals(userIdStr)) {
					Window.alert("请输入userId");
					return;
				}
				String itemCountStr = itemCount.getValue();
				if (itemCountStr == null || "".equals(itemCountStr)) {
					Window.alert("请输入数量");
					return;
				}
				int itemNum = 0;
				try {
					itemNum = Integer.parseInt(itemCountStr);
				} catch (Exception e) {
					Window.alert("请输入itemCount一个数字");
					return;
				}
				if (itemNum <= 0) {
					Window.alert("请输入 大于0的数字给itemCount");
					return;
				}

				reawrdIndex++;
				reawrdFlexTable.setText(reawrdIndex, 0, activityStr);
				reawrdFlexTable.setText(reawrdIndex, 1, aName);
				reawrdFlexTable.setText(reawrdIndex, 2, userIdStr);
				reawrdFlexTable.setText(reawrdIndex, 3, itemTypeStr);
				reawrdFlexTable.setText(reawrdIndex, 4, itemNameStr);
				reawrdFlexTable.setText(reawrdIndex, 5, itemNum + "");
				StringBuffer sb = new StringBuffer();
				sb.append(userIdStr +":");
				sb.append(activityStr +":");
				sb.append(itemTypeId +":");
				sb.append(itemId +":");
				sb.append(itemCountStr +":");
				sb.append(aName);
				
				saveMap.put(reawrdIndex, sb.toString());
				System.out.println(sb.toString());
				Button rBtn = new Button("remove");
				rBtn.addClickHandler(btnDeleteClick);
				reawrdFlexTable.setWidget(reawrdIndex, 6, rBtn);

			}
			
		});
		activityId.addChangeHandler( new ChangeHandler() {
					
					@Override
					public void onChange(ChangeEvent arg0) {
						try {
							int id = Integer.parseInt(activityId.getValue(activityId.getSelectedIndex()));
							activityName.setText(activityIdAndNameMap.get(id));
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return;
						}
					}
				});
	}


	

	protected void getAndSetItemName(int type) {
		service2.getAndSetItemName(type, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				SetItemName(map);
				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}
	protected void SetItemName(Map<Integer, String> map) {
		for(int i : map.keySet()){
			itemName.addItem(map.get(i),i+"");
		}
	}
	
	ClickHandler btnDeleteClick = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			int rowIndex = reawrdFlexTable.getCellForEvent(event).getRowIndex();
			reawrdFlexTable.removeRow(rowIndex);
			saveMap.remove(rowIndex);
		}
	};
	ClickHandler btnEditClick = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			int rowIndex = reawrdFlexTable.getCellForEvent(event).getRowIndex();
			int cellIndex = reawrdFlexTable.getCellForEvent(event).getCellIndex();
			int i = 0;
			while (i < cellIndex) {

				for (int j = 0; j < activityId.getItemCount(); j++) {
					if (activityId.getItemText(j).equals(reawrdFlexTable.getText(rowIndex, i))) {
						activityId.setSelectedIndex(j);
					}
				}
				i++;
				userId.setText(reawrdFlexTable.getText(rowIndex, i));
				i++;
				for (int j = 0; j < itemType.getItemCount(); j++) {
					if (itemType.getItemText(j).equals(reawrdFlexTable.getText(rowIndex, i))) {
						itemType.setSelectedIndex(j);
						DomEvent.fireNativeEvent(Document.get().createChangeEvent(),
								itemType);
						
					}
				}
				i++;
				for (int j = 0; j < itemName.getItemCount(); j++) {
					if (itemName.getItemText(j).equals(reawrdFlexTable.getText(rowIndex, i))) {
						itemName.setSelectedIndex(j);
					}
				}
				i++;

				itemCount.setText(reawrdFlexTable.getText(rowIndex, i));
				i++;
			}

		}
	};


	private void getActivityPrizeId() {
		service.getActivityPrizeId(new AsyncCallback<Map<String, Integer>>() {

			@Override
			public void onSuccess(Map<String, Integer> map) {
				activityPrizeIdMap = map;

			}

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert(arg0.toString());
			}
		});
	}
	private void saveReward(){
		service.setRewad(saveMap,new AsyncCallback<Boolean>(){

			@Override
			public void onFailure(Throwable arg0) {
				
			}


			@Override
			public void onSuccess(Boolean succ) {
				if(succ){
					Window.alert("发奖成功");
					Reward reward = new Reward();
					RootPanel content = RootPanel.get("content");
					content.clear();
					content.add(reward);
				}else{
					Window.alert("发奖有失败，请看失败日志");
				}
			}
			
		});
	}
	
	
	private void getActivityIdAndName() {
		service2.getActivityIdAndName(new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				getActivityId(map);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}

	protected void getActivityId(Map<Integer, String> map) {
		activityIdAndNameMap = map;
		activityIdAndNameMap.put(-1,"from admin for luck");
		
		activityId.addItem("请选择");
		activityId.addItem("from admin for luck","-1");
		List<Integer> list = new ArrayList<Integer>(map.keySet());
		Collections.sort(list);
		Collections.reverse(list);
		for(int i:list){
			activityId.addItem(i+"");
		}
		
	}
}
