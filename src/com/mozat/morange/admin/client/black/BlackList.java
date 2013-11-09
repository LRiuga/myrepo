package com.mozat.morange.admin.client.black;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mozat.morange.admin.client.Page;

public class BlackList extends Composite {

	interface BlackListUiBinder extends UiBinder<Widget, BlackList> {
	}

	private final BlackListServiceAsync service = GWT.create(BlackListService.class);
	private static BlackListUiBinder uiBinder = GWT.create(BlackListUiBinder.class);

	@UiField
	Button serchBtn, removeBtn;

	@UiField
	ListBox searchType;

	@UiField
	FlexTable blackList;

	@UiField
	SimplePanel div;

	@UiField
	TextBox searchKey;

	Button editButton = new Button("Edit");
	int index = 0;
	DialogBox dialogbox;
	BlackEdit blackEdit;
	CheckBox cb = new CheckBox();
	private int pageNum = 1;
	private int pageSize = 12;
	private int countPage = 1;
	private Page pageObj;

	public BlackList() {
		initWidget(uiBinder.createAndBindUi(this));
		searchType.addItem("MonetId");

		serchBtn.getElement().addClassName("btn btn-primary");
		removeBtn.getElement().addClassName("btn btn-danger");
		removeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (Window.confirm("确定要删除选中的条目，注意，删除不可恢复！！")) {
					for (int i = 1; i < blackList.getRowCount(); i++) {
						CheckBox cc = (CheckBox) blackList.getWidget(i, 0);
						if (cc.getValue()) {
							int monetid = Integer.parseInt(blackList.getText(i, 1));
							delete(monetid);
							blackList.removeRow(i);
						}
					}
					list(pageNum, pageSize, 0);
				}
			}
		});

		blackList.setWidget(index, 0, cb);
		blackList.setText(index, 1, "MonetId");
		blackList.setText(index, 2, "Reason");
		blackList.setText(index, 3, "Expire Date");
		blackList.setText(index, 4, "Edit");

		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dialogbox = new DialogBox(false);
				BlackEdit blackEdit = new BlackEdit();
				dialogbox.add(blackEdit);
				dialogbox.center();
				dialogbox.show();
				blackEdit.saveBtn.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						dialogbox.hide();
					}
				});
			}
		});
		list(pageNum, pageSize, 0);
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
					list(pageNum, pageSize, 0);
				}
			}
		});
		DOM.sinkEvents(pageObj.li2, Event.ONCLICK);
		DOM.setEventListener(pageObj.li2, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					if (pageNum >= countPage) {
						return;
					}
					pageNum = pageNum + 1;
					list(pageNum, pageSize, 0);
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

					if (i > countPage || i < 0 || i == pageNum) {
						return;
					}
					pageNum = i;
					list(pageNum, pageSize, 0);
				}
			}
		});

		blackList.getParent().getElement().appendChild(pageObj.hPanel.getElement());

		cb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				boolean v = cb.getValue();
				for (int i = 1; i < blackList.getRowCount(); i++) {
					CheckBox cc = (CheckBox) blackList.getWidget(i, 0);
					cc.setValue(v);
				}
			}
		});

	}

	private void list(int page, int size, int monetid) {
		service.list(page, size, monetid, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable msg) {
				Window.alert("错误->"+ msg.toString());
			}

			@Override
			public void onSuccess(String str) {
				JSONValue rel = JSONParser.parseStrict(str);
				JSONObject obj = rel.isObject();
				for (int i = index - 1; i > 1; i--) {
					blackList.removeRow(i);
				}
				resetIndex();
				JSONArray array = obj.get("list").isArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject o = array.get(i).isObject();
					blackList.setWidget(index, 0, new CheckBox());
					blackList.setText(index, 1, o.get("monetId").toString());
					blackList.setText(index, 2, o.get("reason").isString().stringValue());
					blackList.setText(index, 3, o.get("expire").isString().stringValue());
					Button btn = new Button("Edit");
					btn.addClickHandler(edithandler);
					blackList.setWidget(index, 4, btn);
					index++;
				}
				int pageNum = Integer.parseInt(obj.get("page").isNumber().toString());
				int pageCount = Integer.parseInt(obj.get("pages").isNumber().toString());
				pageObj.li3.setInnerHTML("<a ><Label>" + pageNum + "/" + pageCount + "</Label></a>");
				pageObj.tb.setValue(pageNum + "");
				countPage = pageCount;
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
		});
	}

	protected void resetIndex() {
		index = 1;
	}

	@UiHandler("serchBtn")
	void onClick(ClickEvent e) {
		String key = searchKey.getValue();
		int monetid = 0;
		try {
			monetid = Integer.parseInt(key);
		} catch (Exception ex) {
			Window.alert("请输入一个数字");
			searchKey.setFocus(true);
			return;
		}
		if (monetid > 0) {
			list(1, 1, monetid);
		}
	}

	ClickHandler edithandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			dialogbox = new DialogBox(false);
			blackEdit = new BlackEdit();
			int rowIndex = blackList.getCellForEvent(event).getRowIndex();

			String monetid = blackList.getText(rowIndex, 1);
			String reason = blackList.getText(rowIndex, 2);
			String expire = blackList.getText(rowIndex, 3);
			if (!"2999".equals(expire.substring(0, 4))) {
				blackEdit.dateRadio1.setValue(true);
				blackEdit.expireDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss").parse(expire));
			} else {
				blackEdit.dateRadio2.setValue(true);
			}
			blackEdit.monetidBox.setValue(monetid);
			blackEdit.reasonBox.setValue(reason);
			dialogbox.add(blackEdit);
			dialogbox.center();
			dialogbox.show();
			blackEdit.saveBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					int monetid = Integer.parseInt(blackEdit.monetidBox.getValue());
					String reason = blackEdit.reasonBox.getValue();
					Date date = blackEdit.expireDate.getValue();
					JSONObject json = new JSONObject();
					json.put("monetId", new JSONNumber(monetid));
					json.put("reason", new JSONString(reason));
					if (blackEdit.dateRadio2.getValue()) {
						json.put("expire", new JSONString("2999-12-31 00:00:00"));
					} else {
						json.put("expire", new JSONString(DateTimeFormat.getFormat("yyyy-MM-dd  HH:mm:ss").format(date)));
					}
					update(json.toString());
					dialogbox.hide();
				}
			});
		}
	};

	private void update(String json) {
		service.update(json, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String str) {
				if ("true".equals(str)) {
					Window.alert("成功");
					list(pageNum, pageSize, 0);
				} else {
					Window.alert("失败");
				}
			}

			@Override
			public void onFailure(Throwable msg) {
				System.out.println(msg.toString());
			}
		});
	}

	private void delete(int monetid) {
		service.delete(monetid, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String msg) {
				if ("true".equals(msg)) {

				} else {
					Window.alert("有一条删除失败");
				}
			}

			@Override
			public void onFailure(Throwable arg0) {

			}
		});
	}
}
