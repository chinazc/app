package com.zerowire.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import android.content.Context;

import com.zerowire.entity.UserInfoBean;
import com.zerowire.global.Global;
import com.zerowire.timesheetassistant.R;

public class TransputWS {
	private Context context;
	private UserInfoBean loginInfo;
		
	public TransputWS(Context context) {
		this.context = context;	}
    public boolean Login(UserInfoBean loginInfo){
    	boolean flag = true;
    	this.loginInfo = loginInfo;
		WebServiceAttribute wsAttribute = new WebServiceAttribute();
//		wsAttribute.setNameSpace(context.getString(R.string.ws_targetNamespace));
//		wsAttribute.setServiceURL(ConfigPara.ws_SyncServiceURL);
//		wsAttribute.setMethodName(context.getString(R.string.ws_method_uploadFile));
		wsAttribute.setNameSpace(context.getString(R.string.ws_targetNamespace));
		wsAttribute.setServiceURL("http://cloudhand.hand-china.com/TimesheetAssistant-war/rest/employee");
		wsAttribute.setMethodName(context.getString(R.string.ws_method_uploadFile));
		
		List<WebAttrKV> list = new ArrayList<WebAttrKV>();
		
		WebAttrKV webAttrKV = null;

			webAttrKV = new WebAttrKV();
			webAttrKV.setKey("args0");
			webAttrKV.setValue(loginInfo.getUsername());
			list.add(webAttrKV);
			
			webAttrKV = new WebAttrKV();
			webAttrKV.setKey("args1");
			webAttrKV.setValue(loginInfo.getPwd());
			list.add(webAttrKV);
			
			webAttrKV = new WebAttrKV();
			webAttrKV.setKey("args2");
			webAttrKV.setValue(Global.getHardwareModel(context));
			list.add(webAttrKV);
			webAttrKV = new WebAttrKV();
			webAttrKV.setKey("args3");
			webAttrKV.setValue(Global.getHardwareNumber(context));
			list.add(webAttrKV);
			webAttrKV = new WebAttrKV();
			webAttrKV.setKey("args4");
			webAttrKV.setValue(Global.getOperatingSysInfo(context));
			list.add(webAttrKV);
		
		    WebServiceConn webConn = new WebServiceConn(context);
		    webConn.setTimeOut(10 * 60 * 1000);
		    SoapObject result = null;
			try {
				result = (SoapObject) webConn.getSoapObject(wsAttribute, list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (result != null) { //结果不为null
			Object obj = result.getProperty("return");
			if (obj != null) {  //不为空则转为int类型赋给flag
				flag = Boolean.valueOf(obj.toString());
			}
		}
		
		return flag;
    	
    }
}
