package com.zerowire.shake;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MapActivity;
import com.zerowire.config.ConfigPara;
import com.zerowire.global.Global;
import com.zerowire.shake.ShakeListener.OnShakeListener;
import com.zerowire.timesheetassistant.MainActivity;
import com.zerowire.timesheetassistant.PersonalActivity;
import com.zerowire.timesheetassistant.R;
import com.zerowire.timesheetassistant.SettingActivity;

public class ShakeActivity extends MapActivity  implements LocationListener
{
	 /**
     * 摇一摇监听
     */
    private ShakeListener mShakeListener = null;

    /**
     * 重力感应仪
     */
    private Vibrator mVibrator;

    /**
     * 摇一摇动画上图标
     */
    private RelativeLayout mImgUp;

    /**
     * 摇一摇动画下图标
     */
    private RelativeLayout mImgDn;

    /**
     * 摇一摇标题栏
     */
    private RelativeLayout mTitle;

    /**
     * 摇一摇要到的好友
     */
    private SlidingDrawer mDrawer;

    /**
     * 抽屉
     */
    private Button mDrawerBtn;
    
    private ListView listView;
    private double latitude=0.0;  
    private double longitude =0.0; 
    
    private double latitudes[];
    private double longitudes[];
    private String names[],project_name[],depart[];
    ArrayList<HashMap<String, Object>> listItem;
    HashMap<String, Object> map = null;
    SimpleAdapter listItemAdapter = null;
    

    private BMapManager mapManager;  
    private MKLocationManager mLocationManager = null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shake_activity);
        ConfigPara.sharedPreferences = getSharedPreferences(ConfigPara.PRENAME, 0);
        ConfigPara.editor = ConfigPara.sharedPreferences.edit();
//        getLocation();
        
        listItem = new ArrayList<HashMap<String, Object>>();
        // 项目经纬度
        latitudes = new double[]{35.23423,30.23435,15.32556,36.25678,39.20854};
        longitudes = new double[]{17.23423,30.23435,15.32556,36.25678,17.23455};
        names = new String[]{"张三","李四","王五","赵六","牛七"};
        project_name = new String[]{"张三2","李四2","王五2","赵六2","牛七2"};
        depart = new String[]{"张三3","李四3","王五3","赵六3","牛七3"};
		//查找附近同事
//		boolean retVal = Global.RemoteLogic().getNearbyColleague("31.216511942366","121.60330856744","12224");
        mVibrator = (Vibrator) getApplication().getSystemService(
                VIBRATOR_SERVICE);
        mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
        mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
        mTitle = (RelativeLayout) findViewById(R.id.shake_title_bar);
        mDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
        mDrawerBtn = (Button) findViewById(R.id.handle);
        listView = (ListView) findViewById(R.id.lv_near_people);

        listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.listview_item_shake,
				new String[] { "name", "project_name","depart" },
				new int[] { R.id.name, R.id.project_name,R.id.depart });
        /* 设定SlidingDrawer被开启的事件处理 */
        mDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
        {
            public void onDrawerOpened()
            {
                mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.shake_report_dragger_down));
                TranslateAnimation titleup = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, -1.0f);
                titleup.setDuration(200);
                titleup.setFillAfter(true);
                mTitle.startAnimation(titleup);
            }
        });
        /* 设定SlidingDrawer被关闭的事件处理 */
        mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
        {
            public void onDrawerClosed()
            {
                mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.shake_report_dragger_up));
                TranslateAnimation titledn = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                titledn.setDuration(200);
                titledn.setFillAfter(false);
                mTitle.startAnimation(titledn);
            }
        });

        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new OnShakeListener()
        {
            public void onShake()
            {
                startAnim(); // 开始 摇一摇手掌动画
                mShakeListener.stop();
                startVibrato(); // 开始 震动
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                    	ArrayList<String> data = new ArrayList<String>();
                    	data = getDateAndIndex();
                    	if (data!=null) {
                    		listView.setAdapter(listItemAdapter);
                    		mDrawer.animateOpen();
                    		
						}else {
							Toast.makeText(getApplicationContext(),
	                                "抱歉，附近没有项目！", 10).show();
						}
                        mVibrator.cancel();
                        mShakeListener.start();
                    }
                }, 2000);
            }
        });
        
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShakeActivity.this,PersonalActivity.class);
				intent.putExtra("id", Integer.parseInt(getDateAndIndex().get(arg2)));
				startActivity(intent);
			}
		});
    }

     public ArrayList<String>  getDateAndIndex(){
    	 listItem.clear();
    	 // 获取的经纬度
    	 latitude = Double.valueOf(ConfigPara.sharedPreferences.getString("latitude", "0.0"));
         longitude = Double.valueOf(ConfigPara.sharedPreferences.getString("longitude", "0.0"));
        // 手机经纬度
         latitude = 37.20854;
         longitude = 15.23455;
    	 int cd = latitudes.length;
    	 ArrayList<String> list = new ArrayList<String>();
    	 for (int i = 0; i < latitudes.length; i++) {
    		
     		double a = latitudes[i]-latitude;
     		a=Math.abs(a);
     		double b = longitudes[i]-longitude;
     		b=Math.abs(b);
				if (a<5&&b<5) {
					map = new HashMap<String, Object>();
					map.put("name", names[i]);
					map.put("project_name", project_name[i]);
					map.put("depart", depart[i]);
					listItem.add(map);
					list.add(String.valueOf(i));
				}
				
			}
    	 
    	 
		return list;
    	 
     }
    public void startAnim()
    { // 定义摇一摇动画动画
        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        mytranslateanimup0.setDuration(1000);
        TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        mytranslateanimup1.setDuration(1000);
        mytranslateanimup1.setStartOffset(1000);
        animup.addAnimation(mytranslateanimup0);
        animup.addAnimation(mytranslateanimup1);
        mImgUp.startAnimation(animup);

        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        mytranslateanimdn0.setDuration(1000);
        TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        mytranslateanimdn1.setDuration(1000);
        mytranslateanimdn1.setStartOffset(1000);
        animdn.addAnimation(mytranslateanimdn0);
        animdn.addAnimation(mytranslateanimdn1);
        mImgDn.startAnimation(animdn);
    }

    public void startVibrato()
    { // 定义震动
        mVibrator.vibrate(new long[]{500, 200, 500, 200}, -1); // 第一个｛｝里面是节奏数组，
                                                               // 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
    }

    public void shake_activity_back(View v)
    { // 标题栏 返回按钮
    	finish();
    	overridePendingTransition(R.anim.short_in_from_left,  R.anim.long_out_from_left);
//        this.finish();
    }

    public void getLocation() {
    	   // 初始化MapActivity  
        mapManager = new BMapManager(getApplication());  
        // init方法的第一个参数需填入申请的API Key  
        mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);  
        super.initMapActivity(mapManager);  
  
        mLocationManager = mapManager.getLocationManager();  
        // 注册位置更新事件  
        mLocationManager.requestLocationUpdates(this);  
        // 使用GPS定位  
        mLocationManager.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);  
    }
    
    @Override  
    protected boolean isRouteDisplayed() {  
        return false;  
    }  
  
 
  
    @Override  
    protected void onPause() {  
        if (mapManager != null) {  
            mapManager.stop();  
        }  
        super.onPause();  
    }  
  
    @Override  
    protected void onResume() {  
        if (mapManager != null) {  
            mapManager.start();  
        }  
        super.onResume();  
    }  
  
    /** 
     * 根据MyLocationOverlay配置的属性确定是否在地图上显示当前位置 
     */  
    @Override  
    protected boolean isLocationDisplayed() {  
        return false;  
    }  
  
    /** 
     * 当位置发生变化时触发此方法 
     *  
     * @param location 当前位置 
     */  
    public void onLocationChanged(Location location) {  
        if (location != null) {  
            // 显示定位结果  
            longitude = location.getLongitude();  
            latitude = location.getLatitude(); 
            ConfigPara.editor.putString("latitude", Double.toString(latitude));
            ConfigPara.editor.putString("longitude", Double.toString(longitude));
            
            ConfigPara.editor.commit();

        }  
    }  

    	
    @Override
    protected void onDestroy()
    {
    	super.onDestroy(); 
        if (mShakeListener != null)
        {
            mShakeListener.stop();
        }
        if (mapManager != null) {  
            mapManager.destroy();  
            mapManager = null;  
        }  
        mLocationManager = null; 
       
    }


}
