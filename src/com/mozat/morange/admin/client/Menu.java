package com.mozat.morange.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.mozat.morange.admin.client.activity.ActivityList;
import com.mozat.morange.admin.client.activity.ActivityPrice;
import com.mozat.morange.admin.client.activity.ActivityPriceList;
import com.mozat.morange.admin.client.activity.ActivitySetting;
import com.mozat.morange.admin.client.black.Black;
import com.mozat.morange.admin.client.black.BlackList;
import com.mozat.morange.admin.client.fisher.Fisher;
import com.mozat.morange.admin.client.fisher.Fisher_info;
import com.mozat.morange.admin.client.reward.Reward;
import com.mozat.morange.admin.client.reward.RewardList;

public class Menu implements EntryPoint {
	ListBox selectBox = null;
	public static DialogBox dialogbox = null;
	private final LoginServiceAsync service = GWT.create(LoginService.class);

	@Override
	public void onModuleLoad() {

		login();
		// 服务器
		Element select = DOM.getElementById("server");
		selectBox = ListBox.wrap(select);
		setServerSelect();
		DOM.sinkEvents(select, Event.ONCHANGE);
		DOM.setEventListener(select, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCHANGE) {
					int index = selectBox.getSelectedIndex();
					String server = selectBox.getValue(index);
					int selectServer = Integer.parseInt(server);
					setServer(selectServer);
					RootPanel content = RootPanel.get("content");
					content.clear();
					FlowPanel div = new FlowPanel();
					div.getElement().addClassName("alert fade in");
					Button btn = new Button("x");
					btn.getElement().addClassName("close");
					btn.getElement().setAttribute("data-dismiss", "alert");
					Label label = new Label("你选择了" + (Integer.parseInt(server) + 1) + "服");
					div.add(btn);
					div.add(label);
					
					//content.getElement().insertBefore(div.getElement(), content.getElement().getChild(0));
					content.add(div);
					Element crumb = DOM.getElementById("crumb");
					Node node2 = null;

					if (1 < crumb.getChildCount()) {
						node2 = crumb.getChild(1);
						Element li2 = DOM.createElement("li");
						li2.setInnerHTML("<a >server" + (Integer.parseInt(server) + 1) + "</a> <span class='divider'>/</span>");
						crumb.removeChild(node2);
						crumb.insertAfter(li2, crumb.getChild(0));
					}
				}
			}
		});
		// 玩家账号
		Element user = DOM.getElementById("user");
		DOM.sinkEvents(user, Event.ONCLICK);
		DOM.setEventListener(user, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("玩家信息", "玩家账号");
					Fisher user = new Fisher();
					addComposite(user);
				}

			}
		});
		// 玩家账户
		Element fisher = DOM.getElementById("fisher");
		DOM.sinkEvents(fisher, Event.ONCLICK);
		DOM.setEventListener(fisher, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("玩家信息", "玩家账户");
					Fisher_info fisher = new Fisher_info();
					addComposite(fisher);
				}
			}
		});
		// 活动设置
		Element activity = DOM.getElementById("activitySetting");
		DOM.sinkEvents(activity, Event.ONCLICK);
		DOM.setEventListener(activity, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				setCrumb("活动信息", "活动设置");
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					ActivitySetting activity = new ActivitySetting();
					addComposite(activity);
				}
			}
		});
		// 活动列表
		Element Activitylist = DOM.getElementById("activityList");
		DOM.sinkEvents(Activitylist, Event.ONCLICK);
		DOM.setEventListener(Activitylist, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("活动信息", "活动列表");
					ActivityList activityList = new ActivityList();
					addComposite(activityList);
				}
			}
		});
		// 配置奖励
		Element activityPrice = DOM.getElementById("activityPrice");
		DOM.sinkEvents(activityPrice, Event.ONCLICK);
		DOM.setEventListener(activityPrice, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("活动信息", "配置奖励");
					ActivityPrice activityPrice = new ActivityPrice();
					addComposite(activityPrice);
				}
			}
		});
		// 配置列表
		Element activityPriceList = DOM.getElementById("activityPriceList");
		DOM.sinkEvents(activityPriceList, Event.ONCLICK);
		DOM.setEventListener(activityPriceList, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("活动信息", "配置列表");
					ActivityPriceList activityPriceList = new ActivityPriceList();
					addComposite(activityPriceList);
				}
			}
		});
		// 手动发奖
		Element reward = DOM.getElementById("reward");
		DOM.sinkEvents(reward, Event.ONCLICK);
		DOM.setEventListener(reward, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("活动奖励", "手动发奖");
					Reward reward = new Reward();
					addComposite(reward);
				}
			}
		});
		// 奖励列表
		Element rewardList = DOM.getElementById("rewardList");
		DOM.sinkEvents(rewardList, Event.ONCLICK);
		DOM.setEventListener(rewardList, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("活动奖励", "奖励历史");
					RewardList rewardList = new RewardList();
					addComposite(rewardList);
				}
			}
		});
		// 禁止用户
		Element black = DOM.getElementById("black");
		DOM.sinkEvents(black, Event.ONCLICK);
		DOM.setEventListener(black, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("禁止用户", "black用户");
					Black black = new Black();
					addComposite(black);
				}
			}
		});

		// 禁止列表
		Element blacklist = DOM.getElementById("blackList");
		DOM.sinkEvents(blacklist, Event.ONCLICK);
		DOM.setEventListener(blacklist, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("禁止用户", "禁止列表");
					BlackList black = new BlackList();
					addComposite(black);
				}
			}
		});
		// 竞技场开放
		Element areanOpen = DOM.getElementById("areanOpen");
		DOM.sinkEvents(areanOpen, Event.ONCLICK);
		DOM.setEventListener(areanOpen, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("开放功能", "竞技场开放");
					Arean fo = new Arean();
					addComposite(fo);
				}
			}
		});

		// 装备开放
		Element equimentOpen = DOM.getElementById("equimentOpen");
		DOM.sinkEvents(equimentOpen, Event.ONCLICK);
		DOM.setEventListener(equimentOpen, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("开放功能", "装备开放");
					EquimentOpen fo = new EquimentOpen();
					addComposite(fo);
				}
			}
		});
		// 邀请好友
		Element friend = DOM.getElementById("friend");
		DOM.sinkEvents(friend, Event.ONCLICK);
		DOM.setEventListener(friend, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("开放功能", "邀请好友");
					Friend fo = new Friend();
					addComposite(fo);
				}
			}
		});
		// 打怪时间设置
		Element tribeMonter = DOM.getElementById("tribeMonter");
		DOM.sinkEvents(tribeMonter, Event.ONCLICK);
		DOM.setEventListener(tribeMonter, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("开放功能", "打怪时间设置");
					tribeMonter fo = new tribeMonter();
					addComposite(fo);
				}
			}
		});
		// 装备抽奖
		Element equipmentLuckyDraw = DOM.getElementById("equipmentLuckyDraw");
		DOM.sinkEvents(equipmentLuckyDraw, Event.ONCLICK);
		DOM.setEventListener(equipmentLuckyDraw, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("开放功能", "装备抽奖");
					EquipmentLuckyDraw fo = new EquipmentLuckyDraw();
					addComposite(fo);
				}
			}
		});
		// 功能设置
				Element functionOpen = DOM.getElementById("functionOpen");
				DOM.sinkEvents(functionOpen, Event.ONCLICK);
				DOM.setEventListener(functionOpen, new EventListener() {
					@Override
					public void onBrowserEvent(Event event) {
						if (DOM.eventGetType(event) == Event.ONCLICK) {
							setCrumb("開放功能", "其他功能");
							FunctionOpen fo = new FunctionOpen();
							addComposite(fo);
						}
					}
				});
		// 世界打怪设置
		Element worldMonster = DOM.getElementById("worldMonster");
		DOM.sinkEvents(worldMonster, Event.ONCLICK);
		DOM.setEventListener(worldMonster, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("功能设置", "世界打怪设置");
					WorldMonter fo = new WorldMonter();
					addComposite(fo);
				}
			}
		});
		// 装备打折
		Element equipmentDiscount = DOM.getElementById("equipmentDiscount");
		DOM.sinkEvents(equipmentDiscount, Event.ONCLICK);
		DOM.setEventListener(equipmentDiscount, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("功能设置", "打折设置");
					EquipmentDiscount fo = new EquipmentDiscount();
					addComposite(fo);
				}
			}
		});
		// 皇冠设置
		Element huangSet = DOM.getElementById("huangSet");
		DOM.sinkEvents(huangSet, Event.ONCLICK);
		DOM.setEventListener(huangSet, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("功能设置", "皇冠设置");
					Cronk fo = new Cronk();
					addComposite(fo);
				}
			}
		});
		// 捕鱼活动设置
		Element fishConf = DOM.getElementById("fishConf");
		DOM.sinkEvents(fishConf, Event.ONCLICK);
		DOM.setEventListener(fishConf, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONCLICK) {
					setCrumb("功能设置", "捕鱼活动设置");
					FisheActivityConf fo = new FisheActivityConf();
					addComposite(fo);
				}
			}
		});
	}

	private void setServerSelect() {
		service.getServer(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer serverCount) {
				for (int j = 0; j < serverCount; j++) {
					selectBox.addItem("server" + (j + 1), j + "");
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("获取服务器失败");
			}
		});
	}

	protected void setServer(int selectServer) {
		service.setServer(selectServer, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void arg0) {

			}

			@Override
			public void onFailure(Throwable t) {
				Window.alert("换服失败");
			}
		});
	}

	// 设置面包屑
	private void setCrumb(String item1, String item2) {
		Element crumb = DOM.getElementById("crumb");
		Node node3 = null;
		Node node2 = null;
		if (4 == crumb.getChildCount()) {
			node3 = crumb.getChild(3);
			crumb.removeChild(node3);
		}
		if (3 == crumb.getChildCount()) {
			node2 = crumb.getChild(2);
			crumb.removeChild(node2);
		}

		Element li2 = DOM.createElement("li");
		li2.setInnerHTML("<a >" + item1 + "</a> <span class='divider'>/</span>");
		Element li3 = DOM.createElement("li");
		li3.addClassName("active");
		li3.setInnerText(item2);
		crumb.appendChild(li2);
		crumb.appendChild(li3);
	}

	private void addComposite(Composite com) {
		RootPanel content = RootPanel.get("content");
		content.clear();
		content.add(com);
	}

	private void login() {
		dialogbox = new DialogBox(false);
		Login loginPage = new Login();
		dialogbox.add(loginPage);
		dialogbox.center();
		dialogbox.show();
	}

	protected static void setUserInfo(String userName) {
		Element ul = DOM.getElementById("indexPage");
		ul.removeChild(ul.getLastChild());
		Element li = DOM.createElement("li");
		li.setInnerHTML("<a href='#'>你好，" + userName + "</a>");
		ul.appendChild(li);
	}

}
