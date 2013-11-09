package com.mozat.morange.admin.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Page extends Composite {
	public HorizontalPanel hPanel = new HorizontalPanel();
	public Element li1,li2,li3,li4,li5,ul;
	public TextBox tb  = null;
	public Page(){
		ul = DOM.createElement("ul");
		ul.addClassName("pager");
		li1 = DOM.createElement("li");
		li1.setInnerHTML("<a href='#' id='prePage'><label>&larr;Previous</label></a>");
		li2 = DOM.createElement("li");
		li2.setInnerHTML("<a href='#' id ='nextPage'>Next&rarr;</a>");
		li3 = DOM.createElement("li");
		li3.setInnerHTML("<a ><Label>1/1</Label></a>");
		
		li4 = DOM.createElement("li");
		li4.setInnerHTML("<a ></a>");
		tb = new TextBox();
		tb.getElement().addClassName("span1");
		li4.getChild(0).appendChild(tb.getElement());
		
		li5 = DOM.createElement("li");
		li5.setInnerHTML("<a ><Button id='pageClick'>Go</Button></a>");
		
		ul.appendChild(li1);
		ul.appendChild(li3);
		ul.appendChild(li4);
		ul.appendChild(li5);
		ul.appendChild(li2);
		
		hPanel.getElement().addClassName("offset2");
		hPanel.getElement().appendChild(ul);
	}
}
