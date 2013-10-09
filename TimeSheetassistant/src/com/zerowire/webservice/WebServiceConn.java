package com.zerowire.webservice;

import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;

public class WebServiceConn {

	private Context context;
	private SharedPreferences sp;

	/**
	 * 设置显示错误提示
	 */
	@SuppressWarnings("unused")
	private boolean showMsg = true;
	
	private int timeOut = 60 * 1000;

	public WebServiceConn(Context context) {
		this.context = context;
//		this.sp = context.getSharedPreferences(context.getPackageName() + ".syncCofig", Context.MODE_PRIVATE);
	}
	
	public void setTimeOut(int timeOut){
		this.timeOut = timeOut;
	}
	

	
	/**
	 * 利用KSCOP获取webservice服务器端数据
	 * @param wsAttribute
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public Object getSoapObject(WebServiceAttribute wsAttribute,List<WebAttrKV> list) throws IOException {
		Object result = null;
		Object soapObject = null;
		SoapObject request = new SoapObject(wsAttribute.getNameSpace(), wsAttribute.getMethodName());
		if (list != null) {
			for (WebAttrKV kv : list) {// 遍历list，向服务器传入参数
				request.addProperty(kv.getKey(), kv.getValue());
			}
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet= false;
        envelope.bodyOut = request;

        new MarshalBase64().register(envelope);

        String serviceURL = wsAttribute.getRealURL();
        HttpTransportSE ht = new HttpTransportSE(serviceURL, timeOut);
        try {
        	System.out.println(serviceURL);
			ht.call(wsAttribute.getSoapAction(), envelope);
			soapObject = envelope.bodyIn;
			//*************
			//System.out.println(soapObject);
			//不能转换为SoapObject
			if (! (soapObject instanceof SoapObject)) {
				soapObject = null;
			}
			result = soapObject;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取webservice的URL地址
	 * @param wsAttribute
	 * @return
	 */
//	private WebServiceAttribute getRealURL(WebServiceAttribute wsAttribute){
//		String serviceURL = wsAttribute.getServiceURL();
//        if (!serviceURL.startsWith("http")) { //URL不是http类型，修改为http类型
//        	String defaultIp = ConfigSync.getIpDefault;
//        	String wsAddress = sp.getString(context.getString(R.string.settings_key_ws_address), defaultIp);
//        	serviceURL = "http://" + wsAddress + serviceURL;
//        }
//        wsAttribute.setRealURL(serviceURL);
//        return wsAttribute;
//	}
	/**
	 * 设置是否显示错误提示 Toast 
	 * @param showMsg <br><code>true</code>:显示<br><code>false</code>:不显示
	 */
	public void setShowMsg(boolean showMsg){
		this.showMsg = showMsg;
	}
}
