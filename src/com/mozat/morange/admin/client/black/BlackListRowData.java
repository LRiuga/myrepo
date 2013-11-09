package com.mozat.morange.admin.client.black;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class BlackListRowData {
	private int monetId;
	private String reason;
	private Date expire;
	public int getMonetId() {
		return monetId;
	}
	public void setMonetId(int monetId) {
		this.monetId = monetId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		if (expire==null || expire.length()==0)
			this.expire = null;
		else
			this.expire = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(expire);
	}
}
