package com.zerowire.global;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings.Secure;

import com.zerowire.remotelogic.*;

/**
 * 全局引用
 * 
 * @author Jason.Wang
 * @see com.zerowire.remotelogic
 *      {@code Global.RemoteLogic().MethodName(xxx,...);}
 */
public class Global {
	private static class GlobalRemoteLogicHolder {
		public static RemoteLogic instance = new RemoteLogic();
	}

	/**
	 * 逻辑模块,远程数据调用 Jason
	 * 
	 * @return
	 */
	public static RemoteLogic RemoteLogic() {
		return GlobalRemoteLogicHolder.instance;
	}

	private static SharedPreferences setting = null;

	/**
	 * 全局设置,读取缓存数据
	 * 
	 * @return
	 */
	public static SharedPreferences getSetting() {
		return setting;
	}

	/**
	 * 配置文件句柄设置
	 * 
	 * @param Setting
	 */
	public static void setSetting(SharedPreferences Setting) {
		setting = Setting;
	}

	/**
	 * 拦截错误消息统一提示
	 * 
	 * @param senderType
	 *            消息类型,尚未扩展,默认填0
	 * @return
	 */
	public static String getExceptionMsg(int senderType) {
		return "无法连接服务器,\n请检查网络是否有异常情况.";
	}

	/*
	 * 获取硬件编号
	 */
	public static String getHardwareNumber(Context context) {
		String hardwareNumber = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return hardwareNumber;
	}

	/*
	 * 获取推送编号
	 */
	public static String getPushNumber(Context context) {
		String pushNumber = "1234";
		return pushNumber;
	}

	/*
	 * 硬件型号
	 */
	public static String getHardwareModel(Context context) {
		String HardwareModel = Build.MODEL;
		return HardwareModel;
	}

	/*
	 * 操作系统信息
	 */
	public static String getOperatingSysInfo(Context context) {
		String phoneInfo = "Product: " + Build.PRODUCT;
		phoneInfo += ", CPU_ABI: " + Build.CPU_ABI;
		phoneInfo += ", TAGS: " + Build.TAGS;
		phoneInfo += ", VERSION_CODES.BASE: " + Build.VERSION_CODES.BASE;
		phoneInfo += ", MODEL: " + Build.MODEL;
		phoneInfo += ", SDK: " + Build.VERSION.SDK;
		phoneInfo += ", VERSION.RELEASE: " + Build.VERSION.RELEASE;
		phoneInfo += ", DEVICE: " + Build.DEVICE;
		phoneInfo += ", DISPLAY: " + Build.DISPLAY;
		phoneInfo += ", BRAND: " + Build.BRAND;
		phoneInfo += ", BOARD: " + Build.BOARD;
		phoneInfo += ", FINGERPRINT: " + Build.FINGERPRINT;
		phoneInfo += ", ID: " + Build.ID;
		phoneInfo += ", MANUFACTURER: " + Build.MANUFACTURER;
		phoneInfo += ", USER: " + Build.USER;
		return phoneInfo;
	}

	/*
	 * BufferedHttpEntity buffHttpEntity = new BufferedHttpEntity(entity);
			Header myHeader = buffHttpEntity.getContentType();
			InputStream is = buffHttpEntity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				senderSB.append(line);
			}
			br.close();
			is.close();
	 * */
	
	/*
	 * private ProgressDialog myDialog; private void SetData() { // TODO
	 * Auto-generated method stub myDialog =
	 * ProgressDialog.show(SiteMessageList.this, "信息加载", "正在加载数据信息,请稍后!"); new
	 * Thread() { public void run(){ try { // add code... ... // send message
	 * Message message = new Message(); message.what = 1109;
	 * handler.sendMessage(message); } catch(Exception ex) {} finally {
	 * myDialog.dismiss(); } } }.start(); }
	 * 
	 * Handler handler = new Handler() { // TODO Auto-generated method stub
	 * public void handleMessage(Message msg) { UpdateUI();
	 * super.handleMessage(msg); } };
	 * 
	 * private void UpdateUI() { // TODO Auto-generated method stub // do
	 * something you want... }
	 */

}