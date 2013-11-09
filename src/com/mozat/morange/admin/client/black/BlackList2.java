package com.mozat.morange.admin.client.black;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.ListDataProvider;

public class BlackList2 implements EntryPoint {
	private final BlackListServiceAsync service = GWT.create(BlackListService.class);
	private CellTable<BlackListRowData> cellTable = new CellTable<BlackListRowData>();
	private int page = 1;
	private int size = 20;
	
	@Override
	public void onModuleLoad() {
		RootPanel root = RootPanel.get("centerDiv");
		root.clear();
		
		VerticalPanel parent = new VerticalPanel();
		parent.setSize("100%", "100%");
		root.add(parent);
		
		final FlexTable flexTable = new FlexTable();
		flexTable.setStyleName((String) null);
		flexTable.setText(0, 0, "monetId:");
		
		final TextBox monetIdTextBox = new TextBox();
		monetIdTextBox.setMaxLength(12);
		monetIdTextBox.setWidth("60px");
		flexTable.setWidget(0, 1, monetIdTextBox);
		
		flexTable.setText(0, 2, "reason:");
		
		final TextBox reasonTextBox = new TextBox();
		reasonTextBox.setWidth("100px");
		flexTable.setWidget(0, 3, reasonTextBox);
		
		flexTable.setText(0, 4, "expire date:");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		flexTable.setWidget(0, 5, verticalPanel);
		
		final RadioButton radioButton = new RadioButton("expire", "never expires");
		verticalPanel.add(radioButton);
		radioButton.setValue(true);
		
		final RadioButton radioButton1 = new RadioButton("expire", "select date");
		verticalPanel.add(radioButton1);
		
		Button saveButton = new Button("save");
		flexTable.setWidget(0, 6, saveButton);
		
		parent.add(flexTable);
		
		final DatePicker datePicker = new DatePicker();
		datePicker.setValue(new Date());
		flexTable.setWidget(1, 0, datePicker);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 7);
		flexTable.getFlexCellFormatter().setVisible(1, 0, false);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		radioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flexTable.getFlexCellFormatter().setVisible(1, 0, false);
			}
		});
		
		radioButton1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flexTable.getFlexCellFormatter().setVisible(1, 0, true);
			}
		});
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int monetId = 0;
				try {
					monetId = Integer.parseInt(monetIdTextBox.getText());
				} catch (NumberFormatException e) {
					Window.alert("wrong monetid");
					return;
				}
				String reason = reasonTextBox.getText();
				Date expire = null;
				if (radioButton1.getValue()) {
					expire = datePicker.getValue();
				}
				if (expire!=null && expire.getTime()<=System.currentTimeMillis()) {
					Window.alert("Wrong expire date");
					return;
				}
				
				JSONObject json = new JSONObject();
				json.put("monetId", new JSONNumber(monetId));
				json.put("reason", new JSONString(reason));
				String date = "";
				if (expire!=null)
					date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(expire);
				json.put("expire", new JSONString(date));
				
				service.add(json.toString(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						monetIdTextBox.setValue("");
						reasonTextBox.setValue("");
						flexTable.getFlexCellFormatter().setVisible(1, 0, false);
						getList(page, size);
						Window.alert("add blacklist success");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("add black list failed");						
					}
				});
			}
		});
		
		Column<BlackListRowData, Number> monetColumn = new Column<BlackListRowData, Number>(new NumberCell()) {
			@Override
			public Number getValue(BlackListRowData object) {
				return object.getMonetId();
			}
		};
		cellTable.addColumn(monetColumn, "monetid");
		
		TextColumn<BlackListRowData> reasonColumn = new TextColumn<BlackListRowData>() {
			@Override
			public String getValue(BlackListRowData object) {
				return object.getReason();
			}
		};
		cellTable.addColumn(reasonColumn, "reason");
		
		Column<BlackListRowData, Date> expireColum = new Column<BlackListRowData, Date>(new DateCell()) {
			@Override
			public Date getValue(BlackListRowData object) {
				return object.getExpire();
			}
		};
		cellTable.addColumn(expireColum, "expire date");
		
		Column<BlackListRowData, String> opColumn = new Column<BlackListRowData, String>(new ButtonCell()) {
			@Override
			public String getValue(BlackListRowData object) {
				return "remove";
			}
		};
		cellTable.addColumn(opColumn, "");
		opColumn.setFieldUpdater(new FieldUpdater<BlackListRowData, String>() {
			@Override
			public void update(int index, BlackListRowData object, String value) {
				service.delete(object.getMonetId(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("remove blacklist failed");
					}

					@Override
					public void onSuccess(String result) {
						getList(page, size);
						Window.alert("remove blacklist success");
					}
				});
			}
		});
		getList(page, size);
		parent.add(cellTable);
	}
	
	private void getList(int page, int size) {
		/*service.list(page, size, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("get blacklist failed");
			}

			@Override
			public void onSuccess(String result) {
				showList(result);
			}
		});*/
	}
	
	private void showList(String json) {
		JSONValue v = JSONParser.parseStrict(json);
		System.out.println("BlackList showList , json = "+ json);
		JSONObject jo = v.isObject();
		page = (int)jo.get("page").isNumber().doubleValue();
		size = (int)jo.get("size").isNumber().doubleValue();
		JSONArray array = jo.get("list").isArray();
		ListDataProvider<BlackListRowData> provider = new ListDataProvider<BlackListRowData>();
		provider.addDataDisplay(cellTable);
		List<BlackListRowData> list = provider.getList();
		for (int i=0; i<array.size(); i++) {
			JSONObject obj = array.get(i).isObject();
			int monetId = (int)obj.get("monetId").isNumber().doubleValue();
			String reason = obj.get("reason").isString().stringValue();
			String expire = obj.get("expire").isString().stringValue();
			BlackListRowData data = new BlackListRowData();
			data.setMonetId(monetId);
			data.setReason(reason);
			data.setExpire(expire);
			list.add(data);
		}
	}
}
