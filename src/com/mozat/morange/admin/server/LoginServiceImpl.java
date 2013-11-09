package com.mozat.morange.admin.server;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mozat.morange.admin.client.LoginService;
import com.mozat.morange.util.DBResultSet;
import com.mozat.morange.util.Global;

public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	private static final long serialVersionUID = 470542591324533525L;

	@Override
	public int login(String userName, String password) {
		if("test".equals(Global.getFlag())){
			return 3;
		}
		String sql = "select limit  from login where userName = ? and psw = ?";
		DBResultSet rs = Global.getLogindb().execSQLQuery(sql, new Object[]{userName,password},true);
		int limit = 0;
		try {
			if(rs.next()) {
					limit= rs.getInt("limit");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return limit;
		}
		
		
		return limit;
	}

	@Override
	public void setServer(int server) {
		Global.setServer(server);
	}

	@Override
	public int getServer() {
		return Global.getDbList().length;
	}
	
}
