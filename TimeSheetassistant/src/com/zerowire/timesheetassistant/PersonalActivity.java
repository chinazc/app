package com.zerowire.timesheetassistant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;

public class PersonalActivity extends MapActivity {

	int id;
	TextView project_name, telephone, email;
	ImageView iv_head, setting;
	RelativeLayout relativeLayout;
	ScrollView scroll_pop;
	LinearLayout lin_pop;
	BMapManager mBMapMan = null;// 地图管理者
	MapView bMapView;// 视图
	LocationListener locationlistener;// 位置动作监听器
	MyLocationOverlay mLocationOverlay;// 地图覆盖物
	boolean headHasClick = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_info);

		String[] project_names = new String[] { "张三2", "李四2", "王五2", "赵六2",
				"牛七2" };
		project_name = (TextView) findViewById(R.id.project_name);
		bMapView = (MapView) findViewById(R.id.bmapView1);// 找到控件视图
		iv_head = (ImageView) findViewById(R.id.iv_head1);
		setting = (ImageView) findViewById(R.id.iv_menu1);
		relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
		// scroll_pop = (ScrollView) findViewById(R.id.scroll_pop);
		// scroll_pop.setVisibility(View.GONE);
		lin_pop = (LinearLayout) findViewById(R.id.lin_pop);
		lin_pop.setVisibility(View.GONE);
		telephone = (TextView) findViewById(R.id.telephone1);
		email = (TextView) findViewById(R.id.email1);

		Intent intent = this.getIntent();
		id = intent.getIntExtra("id", -1);
		if (id != -1) {
			project_name.setText(project_names[id]);
		}
		mBMapMan = new BMapManager(getApplication());// 创建百度地图管理者
		mBMapMan.init("01331AFA954E7E300428A5F0C9C829E0E16F87A3", null);// 第一个参数就是你申请的key码
		super.initMapActivity(mBMapMan);// 初始化百度地图
		// 显示交通路线
		bMapView.setTraffic(true);
		// 显示卫星图
		bMapView.setSatellite(false);
		bMapView.setBuiltInZoomControls(true);// 设置启动内置的缩放控件

		MapController mMapController = bMapView.getController();// 得到bMapView的控制权，

		// 给定一个经纬度构造一个GeoPoint ，单位是微度（度*1E6）
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(12);// 设置地图zoom级别
		// 获取当前位置的覆盖物
		mLocationOverlay = new MyLocationOverlay(this, bMapView);
		// 添加定位覆盖物
		bMapView.getOverlays().add(mLocationOverlay);

		// 定义位置监听器
		locationlistener = new LocationListener() {

			public void onLocationChanged(Location location) {
				// 当位置改变时，获取当前经纬度
				if (location != null) {
					// 获取位置经纬度
					GeoPoint pt = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					// 将视图中心定位到所在经纬度
					bMapView.getController().animateTo(pt);
				}
			}

		};

		iv_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!headHasClick) {
					lin_pop.setVisibility(View.VISIBLE);
					telephone.setText("13800138000");
					telephone.setTextColor(Color.BLUE);
					telephone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//设下划线
					telephone.getPaint().setAntiAlias(true);// 抗锯齿
					email.setText("123456789@qq.com");
					email.setTextColor(Color.BLUE);
					email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//设下划线
					email.getPaint().setAntiAlias(true);// 抗锯齿
					headHasClick = true;
				} else {
					lin_pop.setVisibility(View.GONE);
					headHasClick = false;
				}

			}
		});
		setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonalActivity.this,
						SettingActivity.class);
				startActivity(intent);
			}
		});
		telephone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setAction("android.intent.action.DIAL");
				intent.setData(Uri.parse("tel:"
						+ telephone.getText().toString()));
				startActivity(intent);
			}
		});
		email.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent email = new Intent(android.content.Intent.ACTION_SEND);
//			     email.setType("plain/text");
//			     String[] emailReciver = new String[]{"xxxx@qq.com","yyy@xx.com"};
//
//			     String  emailSubject = "从问道分享来的文章";
//			     String emailBody = internetpath;
//			     //设置邮件默认地址
//			     email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
//			     //设置邮件默认标题
//			     email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
//			     //设置要默认发送的内容
//			     email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
//			     //调用系统的邮件系统
//			     startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
				
				
//				String string = email.getText().toString();
//				Intent intent = new Intent(Intent.ACTION_SEND);
//				String[] tos = { email.getText().toString() }; //send to someone															
//				String[] ccs = { "root@www.linuxidc.com" }; // 抄送
//				intent.putExtra(Intent.EXTRA_EMAIL, tos);
//				intent.putExtra(Intent.EXTRA_CC, ccs);
//				intent.putExtra(Intent.EXTRA_TEXT, "body");
//				intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
////				Uri uri = Uri.parse("file:///sdcard/mysong.mp3"); // 附件地址
////				intent.putExtra(Intent.EXTRA_STREAM, uri);
//				intent.setType("audio/mp3");
//				intent.setType("message/rfc882");
//				Intent.createChooser(intent, "Choose Email Client");
//				startActivity(intent);
				
				 Intent data=new Intent(Intent.ACTION_SENDTO);  
			        data.setData(Uri.parse("mailto:qq10000@qq.com"));  
			        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");  
			        data.putExtra(Intent.EXTRA_TEXT, "这是内容");  
			        startActivity(data);  
			}
		});
	}

	protected void onResume() {
		// 获取位置管理者，视图根据位置监听更新位置
		mBMapMan.getLocationManager().requestLocationUpdates(locationlistener);
		// 打开定位图标
		mLocationOverlay.enableMyLocation();
		// 打开指南针
		mLocationOverlay.enableCompass();
		// 启动管理着
		mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
