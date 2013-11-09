package com.mozat.morange.admin.client.reward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mozat.morange.admin.client.Page;
import com.mozat.morange.admin.client.activity.ActivityListService;
import com.mozat.morange.admin.client.activity.ActivityListServiceAsync;

public class RewardList extends Composite {

	interface RewardListUiBinder extends UiBinder<Widget, RewardList> {
	}

	private static  Map<Integer, String> typeMap = new HashMap<Integer, String>();
	Map<Integer,String> huangguanMap = new HashMap<Integer, String>();
	Map<Integer,String> xingxiangkaMap = new HashMap<Integer, String>();
	Map<Integer,String> shipMap = new HashMap<Integer, String>();
	Map<Integer,String> shopMap = new HashMap<Integer, String>();
	Map<Integer,String> equipmentMap = new HashMap<Integer, String>();
	Map<Integer,String> vipMap = new HashMap<Integer, String>();
	
	private static RewardListUiBinder uiBinder = GWT.create(RewardListUiBinder.class);
	private final RewardServiceAsync service = GWT.create(RewardService.class);
	private final ActivityListServiceAsync service2 = GWT.create(ActivityListService.class);

	@UiField
	Button serchBtn, removeBtn;

	@UiField
	ListBox searchType, searchKey;

	@UiField
	FlexTable rewardList;

	@UiField
	SimplePanel div;

	TextBox monetidBox = new TextBox();

	Map<Integer, String> activityMap = null;

	int index = 0;
	int pageNum = 1;
	int pageSize = 15;
	int pageCount = 1;
	String listType = null;
	String listKey = null;

	CheckBox cb = new CheckBox();
	Page pageObj;
	private Map<Integer, String> activityIdAndNameMap;

	public RewardList() {
		initWidget(uiBinder.createAndBindUi(this));
		searchType.addItem("Reward For", 1 + "");
		searchType.addItem("MonetId", 2 + "");

		serchBtn.getElement().addClassName("btn btn-primary");
		serchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String type = searchType.getItemText(searchType.getSelectedIndex());
				String key = null;
				if ("Reward For".equals(type)) {
					listType = "activity";
					key = searchKey.getValue(searchKey.getSelectedIndex());
					try {
						Integer.parseInt(key);
					} catch (NumberFormatException e) {
						listType = "all";
					}
				} else if ("MonetId".equals(type)) {
					key = monetidBox.getValue();
					try{
						Integer.parseInt(key);
					}catch (Exception e) {
						Window.alert("请输入一个数字 ");
						monetidBox.setFocus(true);
						return;
					}
					
				}
				listKey = key;
				listReward(pageNum, pageSize, listType, listKey);
			}
		});
		removeBtn.getElement().addClassName("btn btn-danger");
		removeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (Window.confirm("确定要删除选中的条目，注意，删除不可恢复！！")) {
					for (int i = rewardList.getRowCount() - 1; i >= 1; i--) {
						CheckBox cc = (CheckBox) rewardList.getWidget(i, 0);
						if (cc.getValue()) {
							int id = Integer.parseInt(rewardList.getText(i, 1));
							delete(id);
							rewardList.removeRow(i);
						}
					}
					if(!"".equals(listKey) && listKey!= null && !"".equals(listType) && listType !=null ){
						listReward(pageNum, pageSize, listType, listKey);
					}else{
						listReward(pageNum, pageSize, "all", "");
					}
				}
			}
		});
		typeMap.put(0, "商店物品");
		typeMap.put(1, "金币");
		typeMap.put(2, "珍珠");
		typeMap.put(3, "船");
		typeMap.put(4, "VipChat");
		typeMap.put(5, "皇冠");
		typeMap.put(6, "宠物经验");
		typeMap.put(7, "家族贡献");
		typeMap.put(8, "宠物形象卡");
		typeMap.put(9, "装备");
		typeMap.put(10, "星玉");
		typeMap.put(11, "普通强化石");
		typeMap.put(12, "高级强化石");
		typeMap.put(13, "超级强化石");
		typeMap.put(14, "蓝宝石");
		
		
		huangguanMap.put(1, "小皇冠");
		huangguanMap.put(2, "铜皇冠");
		huangguanMap.put(3, "银皇冠");
		huangguanMap.put(4, "金皇冠");
		
		vipMap.put(1, "普通");
		vipMap.put(11, "家族之王");
		
		xingxiangkaMap.put(1, "三天形象卡");
		xingxiangkaMap.put(2, "15天形象卡");
		xingxiangkaMap.put(3, "永久形象卡");
		
		getShopType();
		getShipType();
		getEquipmentType();
		
		getActivity();
		rewardList.setWidget(index, 0, cb);
		rewardList.setText(index, 1, " Id ");
		rewardList.setText(index, 2, "玩家id");
		rewardList.setText(index, 3, "活动id");
		rewardList.setText(index, 4, "活动名称");
		rewardList.setText(index, 5, "物品类型");
		rewardList.setText(index, 6, "物品名称");
		rewardList.setText(index, 7, "物品数量");
		rewardList.setText(index, 8, "create_time");
		rewardList.setText(index, 9, "status");
		cb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				boolean v = cb.getValue();
				for (int i = 1; i < rewardList.getRowCount(); i++) {
					CheckBox cc = (CheckBox) rewardList.getWidget(i, 0);
					cc.setValue(v);
				}

			}
		});
		pageObj = new Page();
		DOM.sinkEvents(pageObj.li1, Event.ONCLICK);
		DOM.setEventListener(pageObj.li1, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					if (pageNum <= 1) {
						return;
					}
					pageNum = pageNum - 1;
					if(!"".equals(listKey) && listKey!= null && !"".equals(listType) && listType !=null ){
						listReward(pageNum, pageSize, listType, listKey);
					}else{
						listReward(pageNum, pageSize, "all", "");
					}
					
				}
			}
		});
		DOM.sinkEvents(pageObj.li2, Event.ONCLICK);
		DOM.setEventListener(pageObj.li2, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					if (pageNum >= pageCount) {
						return;
					}
					pageNum = pageNum + 1;
					if(!"".equals(listKey) && listKey!= null && !"".equals(listType) && listType !=null ){
						listReward(pageNum, pageSize, listType, listKey);
					}else{
						listReward(pageNum, pageSize, "all", "");
					}
				}
			}
		});

		DOM.sinkEvents(pageObj.li5, Event.ONCLICK);
		DOM.setEventListener(pageObj.li5, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					String str = pageObj.tb.getValue();
					int i = 0;
					try {
						i = Integer.parseInt(str);
					} catch (Exception e) {
						pageObj.tb.setFocus(true);
						return;
					}

					if (i > pageCount || i < 0 || i == pageNum) {
						return;
					}
					pageNum = i;
					listReward(pageNum, pageSize, "all", "");
				}
			}
		});
		rewardList.getParent().getElement().appendChild(pageObj.hPanel.getElement());
		listReward(pageNum, pageSize, "all", "");
		searchType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				String type = searchType.getItemText(searchType.getSelectedIndex());

				if ("Reward For".equals(type)) {
					div.clear();
					div.add(searchKey);
				} else if ("MonetId".equals(type)) {
					div.clear();
					div.add(monetidBox);
				}
			}
		});

	}

	protected void delete(int id) {
		service.delete(id, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(Boolean succ) {
				if (!succ) {
					Window.alert("有一个删除失败");
				}
			}

		});
	}

	private void getActivity() {
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
		activityIdAndNameMap  = map;
		activityIdAndNameMap.put(-1, "from admin for luck");
		searchKey.addItem("请选择");
		searchKey.addItem("from admin for luck","-1");
		List<Integer> list = new ArrayList<Integer>(map.keySet());
		for(int i:list){
			searchKey.addItem(i+"_" +map.get(i),i+"");
		}
		
	}
	private void listReward(int page, int size, String type, String key) {
		service.listReward(page, size, type, key, new AsyncCallback<Map<Integer, Map<String, String>>>() {

			@Override
			public void onFailure(Throwable msg) {
				Window.alert(msg.toString());
			}

			@Override
			public void onSuccess(Map<Integer, Map<String, String>> map) {

				for (int i = rewardList.getRowCount() - 1; i >= 1; i--) {
					rewardList.removeRow(i);
				}
				resetIndex();
				List<Integer> list = new ArrayList<Integer>(map.keySet());
				Collections.sort(list);
				Collections.reverse(list);
				for (int id : list) {
					Map<String, String> rowMap = map.get(id);
					if (id > 0) {
						rewardList.setWidget(index, 0, new CheckBox());
						rewardList.setText(index, 1, id + "");
						rewardList.setText(index, 2, rowMap.get(id + "_monet_id"));
						rewardList.setText(index, 3, rowMap.get(id + "_activity_id"));
						rewardList.setText(index, 4, activityIdAndNameMap.get(Integer.parseInt(rowMap.get(id + "_activity_id"))));
						int type = Integer.parseInt(rowMap.get(id + "_item_type"));
						rewardList.setText(index, 5, typeMap.get(type));
						rewardList.setText(index, 6, rowMap.get(id + "_reward_id"));
						int itemid = Integer.parseInt(rowMap.get(id + "_reward_id"));
						rewardList.setText(index, 6, getItemName(type,itemid));
						rewardList.setText(index, 7, rowMap.get(id + "_item_amount"));
						rewardList.setText(index, 8, rowMap.get(id + "_create_time").substring(0,19));
						rewardList.setText(index, 9, rowMap.get(id + "_getRewardTime"));
						index++;
					} else {
						pageNum = Integer.parseInt(rowMap.get("page"));
						pageCount = Integer.parseInt(rowMap.get("pages"));
						pageSize = Integer.parseInt(rowMap.get("size"));
						pageObj.li3.setInnerHTML("<a ><Label>" + pageNum + "/" + pageCount + "</Label></a>");
						pageObj.tb.setValue(pageNum + "");
						if (pageNum <= 1) {
							pageObj.li1.addClassName("disabled");
						} else {
							pageObj.li1.removeClassName("disabled");
						}
						if (pageNum >= pageCount) {
							pageObj.li2.addClassName("disabled");
						} else {
							pageObj.li2.removeClassName("disabled");
						}
					}
				}

			}
		});
	}

	protected String getItemName(int type, int id) {
		String name = "";
		if(type == 1 || type == 2  || type ==6 ||type ==7 ||type ==10 ||type ==11 ||type ==12|| type ==13 || type ==14){
			name = typeMap.get(type);
		}else if(type==4){
			name = vipMap.get(id);
		}else if(type==5){
			name = huangguanMap.get(id);
		}else if(type == 8){
			name = xingxiangkaMap.get(id);
		}else if(type == 0) {
			name = shopMap.get(id);
		}else if(type ==3){
			name = shipMap.get(id);
		}else if(type ==9){
			name = equipmentMap.get(id);
		}
		return name;
	}

	protected void resetIndex() {
		index = 1;
	}
	
	
	protected void getShopType() {
		service2.getAndSetItemName(0, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				shopMap = map;
				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}
	protected void getShipType() {
		service2.getAndSetItemName(3, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				shipMap = map;
				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}
	protected void getEquipmentType() {
		service2.getAndSetItemName(9, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				equipmentMap = map;
				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}

}
