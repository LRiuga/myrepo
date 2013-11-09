package com.mozat.morange.admin.client.activity;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.mozat.morange.admin.client.Page;

public class ActivityList extends Composite  {

	interface ActivityListUiBinder extends UiBinder<Widget, ActivityList> {
	}

	private final ActivityListServiceAsync service = GWT.create(ActivityListService.class);
	private static ActivityListUiBinder uiBinder = GWT.create(ActivityListUiBinder.class);
	DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	@UiField
	Button serchBtn,removeBtn;

	@UiField 
	ListBox searchType,searchKey;
	

	@UiField 
	FlexTable activityList;

	Page pageObj = null;
	private int index = 0;
	private int pageNum = 1;
	private int pageCount =1;
	private int pageSize = 15;
	private String key = null;
	CheckBox cb = new CheckBox();
	
	public ActivityList() {
		initWidget(uiBinder.createAndBindUi(this));
		
		searchType.addItem("ActivityName");
		//searchType.addItem("Status");
		setActivityName();
		
		serchBtn.getElement().addClassName("btn btn-primary");
		removeBtn.getElement().addClassName("btn btn-danger");
		serchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				key = searchKey.getValue(searchKey.getSelectedIndex());
				list(pageNum, pageSize, key);
			}
		});
		removeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (Window.confirm("确定要删除选中的条目，注意，删除不可恢复！！")) {
					for (int i = activityList.getRowCount()-1; i >= 1; i--) {
						CheckBox cc = (CheckBox) activityList.getWidget(i, 0);
						if (cc.getValue()) {
							int id = Integer.parseInt(activityList.getText(i, 1));
							delete(id);
							activityList.removeRow(i);
						}
					}
					list(pageNum, pageSize, key);
				}
			}
		});
		
		activityList.setWidget(index, 0, cb);
		activityList.setText(index, 1, "Id");
		activityList.setText(index, 2, "activity_name");
		activityList.setText(index, 3, "activity_open");
		activityList.setText(index, 4, "activity_close");
		activityList.setText(index, 5, "assign_reward");
		activityList.setText(index, 6, "parent_id");
		activityList.setText(index, 7, "修改");
		
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
		activityList.getParent().getElement().appendChild(pageObj.hPanel.getElement());
		
		list(pageNum,pageSize,"");
		cb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				boolean v = cb.getValue();
				for (int i = 1; i < activityList.getRowCount(); i++) {
					CheckBox cc = (CheckBox) activityList.getWidget(i, 0);
					cc.setValue(v);
				}
			}
		});
	}
	
	protected void delete(int id) {
		service.delete(id, new AsyncCallback<String>() {

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

	private void list(int page,int size,String key){
		service.list(page, size, key, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String str) {
				JSONValue rel = JSONParser.parseStrict(str);
				JSONObject obj = rel.isObject();
				for (int i = activityList.getRowCount() - 1; i > 1; i--) {
					activityList.removeRow(i);
				}
				resetIndex();
				JSONArray array = obj.get("list").isArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject o = array.get(i).isObject();
					activityList.setWidget(index, 0, new CheckBox());
					activityList.setText(index, 1, o.get("id").toString());
					activityList.setText(index, 2, o.get("activity_name").isString().stringValue());
					activityList.setText(index, 3, o.get("activity_open").isString().stringValue());
					activityList.setText(index, 4, o.get("activity_close").isString().stringValue());
					activityList.setText(index, 5, o.get("assign_reward").isNumber().toString());
					activityList.setText(index, 6, o.get("parent_id").isNumber().toString());
					Button btnDelete = new Button("修改");
					btnDelete.addClickHandler(btnClick);
					activityList.setWidget(index, 7, btnDelete);
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
	private void setActivityName(){
		service.getAllActivityName(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> list) {
				for(String s : list){
					searchKey.addItem(s);
				}
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				
			}
		});
	}
	
	ClickHandler btnClick = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            int rowIndex = activityList.getCellForEvent(event).getRowIndex();
            Button btn = (Button)activityList.getWidget(rowIndex, 7);
            String s = btn .getText();
            if("修改".equals(s)){
            	DateBox db1 = new DateBox();
            	DateBox db2 = new DateBox();
            	
            	db1.getElement().addClassName("span2");
            	db2.getElement().addClassName("span2");
            	
            	
            	db1.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));
            	db2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss")));

            	String s3 = activityList.getText(rowIndex, 3);
            	String s4 = activityList.getText(rowIndex, 4);
            	db1.setValue(df.parse(s3));
            	db2.setValue(df.parse(s4));
            	activityList.setWidget(rowIndex, 3, db1);
            	activityList.setWidget(rowIndex, 4, db2);
            	btn.setText("保存");
            }else{
            	DateBox db1 = (DateBox)activityList.getWidget(rowIndex, 3);
            	DateBox db2 = (DateBox)activityList.getWidget(rowIndex, 4);
            	int id = Integer.parseInt(activityList.getText(rowIndex, 1));
            	String s3 = df.format(db1.getValue());
            	String s4 = df.format(db2.getValue());
            	activityList.setText(rowIndex, 3, s3);
            	activityList.setText(rowIndex, 4, s4);
            	updateActivity(id,s3,s4);
            	btn.setText("修改");
            }
        }
    };

	protected void updateActivity(int id, String s3, String s4) {
		service.updateActivity(id, s3, s4, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean arg0) {
				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("保存失败" + arg0.toString());
			}
		});
	}
}
