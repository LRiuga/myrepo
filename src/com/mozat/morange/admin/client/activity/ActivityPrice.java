package com.mozat.morange.admin.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ActivityPrice  extends Composite {

	private static ActivityPriceUiBinder uiBinder = GWT.create(ActivityPriceUiBinder.class);
	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	interface ActivityPriceUiBinder extends UiBinder<Widget, ActivityPrice> {
	}
	Map<Integer, String> activityIdAndNameMap = new HashMap<Integer, String>(); 
	@UiField
	TextBox rank,itemCount,activityName,expire;
	@UiField
	ListBox itemType,itemName,activityId;
	@UiField
	Button addRank,confirm;
	@UiField
	FlexTable rank_flexTable;
	int rewardIndex = 0;
	int saveTime = 0;
	Map<Integer,String> priceMap = new HashMap<Integer, String>();
	public ActivityPrice() {
		initWidget(uiBinder.createAndBindUi(this));
		
		getActivityIdAndName();
		
		activityName.setEnabled(false);
		
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
		addRank.getElement().addClassName("span1 btn btn-primary offset1");
		confirm.getElement().addClassName("btn btn-primary");
		
		expire.setText("0");
		expire.setEnabled(false);
		
		expire.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				String s = expire.getText();
				try {
					Integer.parseInt(s);
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
					expire.setFocus(true);
				}
			}
		});
		itemType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				String typeStr = itemType.getValue(itemType.getSelectedIndex());
				int type  = Integer.parseInt(typeStr);
				itemName.clear();
				expire.setEnabled(false);
				if(type == 1 || type == 2  || type ==6 ||type ==7 ||type ==10 ||type ==11 ||type ==12||type ==13 || type ==14){
					itemName.addItem("0");
					itemName.setEnabled(false);
				}else if(type==4){
					//getAndSetItemName(type);
					itemName.addItem("普通","1");
					itemName.addItem("家族之王","11");
					itemName.setEnabled(true);
				}else if(type==5){
					//getAndSetItemName(type);
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
				
				if(type == 4 || type == 5){
					expire.setEnabled(true);
				}else{
					expire.setValue("0");
					expire.setEnabled(false);
				}
			}
		});
		
		
		rank.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				String[] rankStr = rank.getValue().split("-");
				if(rankStr.length == 2){
					try{
						int num1 = Integer.parseInt(rankStr[0]);
						int num2 = Integer.parseInt(rankStr[1]);
						/*if(num1 > num2) {
							Window.alert("请输入N-M的格式,N < M ");
							rank.setFocus(true);
							return;
						}*/
					}catch (Exception e) {
						Window.alert("请输入N-M的格式,N和M必须为数字");
						rank.setFocus(true);
					}
				}else{
					Window.alert("请输入N-M的格式");
					rank.setFocus(true);
					return;
				}
			}
		});
		rank_flexTable.setText(rewardIndex, 0, "activityId");
		rank_flexTable.setText(rewardIndex, 1, "activityName");
		rank_flexTable.setText(rewardIndex, 2, "Rank");
		rank_flexTable.setText(rewardIndex, 3, "itemType");
		rank_flexTable.setText(rewardIndex, 4, "itemName");
		rank_flexTable.setText(rewardIndex, 5, "count");
		rank_flexTable.setText(rewardIndex, 6, "有效期");
		rank_flexTable.setText(rewardIndex, 7, "delete");

		addRank.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				String id = activityId.getItemText(activityId
						.getSelectedIndex());
				String name = activityName.getText();
				
				String rankStr = rank.getValue();
				String itemTypeStr = itemType.getItemText(itemType
						.getSelectedIndex());
				String itemNameStr = itemName.getItemText(itemName
						.getSelectedIndex());
				String countStr = itemCount.getValue();
				String exTime = expire.getText();
				if (rankStr == null || rankStr.equals("")) {
					Window.alert("请输入rank");
					return;
				}

				int count = 0;
				if (countStr == null || "".equals(countStr)) {
					Window.alert("请输入count");
					return;
				} else {
					try {
						count = Integer.parseInt(countStr);
					} catch (Exception e) {
						Window.alert("请输入count为一个数字");
					}
					if (count <= 0) {
						Window.alert("请输入count大于0的数字");
						return;
					}
				}
				
				rewardIndex++;
				rank_flexTable.setText(rewardIndex, 0, id);
				rank_flexTable.setText(rewardIndex, 1, name);
				rank_flexTable.setText(rewardIndex, 2, rankStr);
				rank_flexTable.setText(rewardIndex, 3, itemTypeStr);
				rank_flexTable.setText(rewardIndex, 4, itemNameStr);
				rank_flexTable.setText(rewardIndex, 5, count + "");
				rank_flexTable.setText(rewardIndex, 6, exTime);
				
				Button btnDelete = new Button("Delete");
				btnDelete.addClickHandler(btnDeleteClick);
				rank_flexTable.setWidget(rewardIndex, 7, btnDelete);
				StringBuffer sb = new StringBuffer();
				sb.append(id + ":");
				sb.append(itemType.getValue(itemType.getSelectedIndex()) + ":");
				sb.append(itemName.getValue(itemName.getSelectedIndex()) + ":");
				sb.append(count + ":");
				sb.append(rankStr + ":");
				sb.append(exTime);
				
				priceMap.put(rewardIndex,sb.toString());			
				}
		});
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				savePrice(priceMap);
			}
		});
		
		
	}

	protected void savePrice(Map<Integer, String> map) {
		saveTime = map.size();
		service.savePrice(map, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer i) {
				Window.alert("保存:" + saveTime + "条,成功:"+i+"条");
				ActivityPrice activityPrice = new ActivityPrice();
				RootPanel content = RootPanel.get("content");
				content.clear();
				content.add(activityPrice);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
		
	}

	protected void getAndSetItemName(int type) {
		service.getAndSetItemName(type, new AsyncCallback<Map<Integer,String>>() {
			
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

	private void getActivityIdAndName() {
		service.getActivityIdAndName(new AsyncCallback<Map<Integer,String>>() {
			
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
		activityId.addItem("请选择");
		List<Integer> list = new ArrayList<Integer>(map.keySet());
		Collections.sort(list);
		Collections.reverse(list);
		for(int i:list){
			activityId.addItem(i+"");
		}
		
	}

	ClickHandler btnDeleteClick = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            int rowIndex = rank_flexTable.getCellForEvent(event).getRowIndex();
            priceMap.remove(rowIndex);
            rank_flexTable.removeRow(rowIndex);

        }
    };
    

}
