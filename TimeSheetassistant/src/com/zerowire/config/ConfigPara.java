package com.zerowire.config;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class ConfigPara {

	public ConfigPara() {
		// TODO Auto-generated constructor stub
	}
    public static boolean IsAtMain = true;
    public static boolean BGClickAble = true;
    /*更换头像还是背景 false：更换背景*/
    public static boolean head_clicked = false; 
    /*配置文件*/
    public static SharedPreferences.Editor editor;
	public static final String PRENAME = "Config";
	public static SharedPreferences sharedPreferences ;
	/*照片缩小比例*/
	public static final int SCALE = 5;
	/*用来标识请求照相功能的activity*/   
	public static final int CAMERA_WITH_DATA = 3023;  
	/*用来标识请求gallery的activity*/   
	public static final int PHOTO_PICKED_WITH_DATA = 3021; 
    /*用来标识请求选择添加项目的activity*/
    public static final int PROJECT_INFO = 100; 
    public static boolean SettingToMain =false;    
    // Sync service URL
 	public static String ws_SyncServiceURL = "/SyncServices/services/SyncDB?wsdl";
 	
 	// Host Address
 	public static String hostIP = "cloudhand.hand-china.com";
 	
 	public static Bitmap headBitmap = null;
 	public static Bitmap backGroundBitmap = null;

}
