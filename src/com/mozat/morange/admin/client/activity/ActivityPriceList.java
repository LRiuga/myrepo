package com.mozat.morange.admin.client.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mozat.morange.admin.client.Page;

public class ActivityPriceList extends Composite  {

	private static ActivityPriceListUiBinder uiBinder = GWT.create(ActivityPriceListUiBinder.class);
	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	interface ActivityPriceListUiBinder extends UiBinder<Widget, ActivityPriceList> {
	}
	
	
	Map<Integer,String> typeMap = new HashMap<Integer, String>();
	Map<Integer,String> huangguanMap = new HashMap<Integer, String>();
	Map<Integer,String> xingxiangkaMap = new HashMap<Integer, String>();
	Map<Integer,String> shipMap = new HashMap<Integer, String>();
	Map<Integer,String> shopMap = new HashMap<Integer, String>();
	Map<Integer,String> equipmentMap = new HashMap<Integer, String>();
	Map<Integer, String> activityIdAndNameMap = new HashMap<Integer, String>(); 
	Map<Integer, String> vipMap = new HashMap<Integer, String>(); 
	public ActivityPriceList() {
		initWidget(uiBinder.createAndBindUi(this));
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
		
		searchType.addItem("Activityid");
		
		getActivityIdAndName();
		getShopType();
		getShipType();
		getEquipmentType();
		serchBtn.getElement().addClassName("btn btn-primary");
		removeBtn.getElement().addClassName("btn btn-danger");
		serchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				try {
					String ks = searchKey.getText();
					if("".equals(ks)){
						key = 0;
					}else{
						key = Integer.parseInt(ks);
					}
				} catch (NumberFormatException e) {
					Window.alert("请输入一个数字");
				}
				list(pageNum, pageSize, key);
			}
		});
		removeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (Window.confirm("确定要删除选中的条目，注意，删除不可恢复！！")) {
					for (int i = activityPriceList.getRowCount()-1; i >= 1; i--) {
						CheckBox cc = (CheckBox) activityPriceList.getWidget(i, 0);
						if (cc.getValue()) {
							int id = Integer.parseInt(activityPriceList.getText(i, 1));
							System.out.println(id);
							delete(id);
							activityPriceList.removeRow(i);
						}
					}
					list(pageNum, pageSize, key);
				}
			}
		});
		
		activityPriceList.setWidget(index, 0, cb);
		activityPriceList.setText(index, 1, "Id");
		activityPriceList.setText(index, 2, "活动id");
		activityPriceList.setText(index, 3, "活动名字");
		activityPriceList.setText(index, 4, "物品类型");
		activityPriceList.setText(index, 5, "物品名称");
		activityPriceList.setText(index, 6, "物品数量");
		activityPriceList.setText(index, 7, "排名1");
		activityPriceList.setText(index, 8, "排名2");
		activityPriceList.setText(index, 9, "有效时间");
		
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
					list(pageNum, pageSize, key);
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
					list(pageNum, pageSize, key);
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
					list(pageNum, pageSize, key);
				}
			}
		});
		activityPriceList.getParent().getElement().appendChild(pageObj.hPanel.getElement());
		
		list(pageNum,pageSize,0);
		cb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				boolean v = cb.getValue();
				for (int i = 1; i < activityPriceList.getRowCount(); i++) {
					CheckBox cc = (CheckBox) activityPriceList.getWidget(i, 0);
					cc.setValue(v);
				}
			}
		});
	}

	@UiField
	Button serchBtn,removeBtn;

	@UiField 
	ListBox searchType;
	
	@UiField 
	TextBox searchKey;
	@UiField 
	FlexTable activityPriceList;
	
	Page pageObj = null;
	private int index = 0;
	private int pageNum = 1;
	private int pageCount =1;
	private int pageSize = 15;
	private int key = 0;
	CheckBox cb = new CheckBox();
	
	protected void delete(int id) {
		service.deletePrice(id, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String msg) {
				if ("true".equals(msg)) {

				}else {
					Window.alert("有一条删除失败");
				}
			}

			@Override
			public void onFailure(Throwable arg0) {

			}
		});
	}

	private void list(int page,int size,int key){
		service.listPrice(page, size, key, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String str) {
				JSONValue rel = JSONParser.parseStrict(str);
				JSONObject obj = rel.isObject();
				for (int i = activityPriceList.getRowCount() - 1; i > 1; i--) {
					activityPriceList.removeRow(i);
				}
				resetIndex();
				JSONArray array = obj.get("list").isArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject o = array.get(i).isObject();
					activityPriceList.setWidget(index, 0, new CheckBox());
					activityPriceList.setText(index, 1, o.get("id").toString());
					activityPriceList.setText(index, 2, o.get("activity_id").isNumber().toString());
					int activity_id = (int)o.get("activity_id").isNumber().doubleValue();
					activityPriceList.setText(index, 3, activityIdAndNameMap.get(activity_id));
					int type = (int)o.get("item_type").isNumber().doubleValue();
					activityPriceList.setText(index, 4, typeMap.get(type));
					int id = (int)o.get("item_id").isNumber().doubleValue();
					activityPriceList.setText(index, 5, getItemName(type,id));
					activityPriceList.setText(index, 6, o.get("item_amount").isNumber().toString());
					activityPriceList.setText(index, 7, o.get("rank_start").isNumber().toString());
					activityPriceList.setText(index, 8, o.get("rank_end").isNumber().toString());
					activityPriceList.setText(index, 9, o.get("expire").isNumber().toString());
					index++;
				}
				pageNum = Integer.parseInt(obj.get("page").isNumber().toString());
				pageCount = Integer.parseInt(obj.get("pages").isNumber().toString());
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


			@Override
			public void onFailure(Throwable msg) {
				System.out.println(msg.toString());
			}
			
			
		});
	}
	private void resetIndex() {
		index = 1;
	}
	
	private void getActivityIdAndName() {
		service.getActivityIdAndName(new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onSuccess(Map<Integer, String> map) {
				activityIdAndNameMap = map;
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}
	
	protected String getItemName(int type,int id){
		String name = "";
		if(type == 1 || type == 2  || type ==6 ||type ==7 ||type ==10 ||type ==11 ||type ==12||type ==13 || type ==14){
			name = typeMap.get(type);
		}else if(type==4){
			name = vipMap.get(id);
		}
		else if(type==5){
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
	
	protected void getShopType() {
		service.getAndSetItemName(0, new AsyncCallback<Map<Integer,String>>() {
			
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
		service.getAndSetItemName(3, new AsyncCallback<Map<Integer,String>>() {
			
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
		service.getAndSetItemName(9, new AsyncCallback<Map<Integer,String>>() {
			
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
