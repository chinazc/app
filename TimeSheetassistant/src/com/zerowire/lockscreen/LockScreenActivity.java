package com.zerowire.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerowire.timesheetassistant.R;
import com.zerowire.timesheetassistant.SettingActivity;

public class LockScreenActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	public static final String PRENAME = "Config";
	private TextView forgetpassword, count;
	private LinearLayout piclock;
	private LinearLayout layout;
	private Button cancle, goon;
	private View view;
	//每次画手势的最后一次手势setPicPassword，点击确定按钮后给passWord，确定每次次手势密码
	//即在点击确定前的那次手势为手势密码
	private String setPicPassword1, setPicPassword2,passWord1,passWord2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);
		init();
		ClickListener();
		sharedPreferences = getSharedPreferences(PRENAME, 0);
		editor = sharedPreferences.edit();
		//是否在设置手势密码
		boolean IsSetPicLock = sharedPreferences.getBoolean("IsSetPicLock",
				false);
		setPicPassword1 = sharedPreferences.getString(
				"setPicPassword1", "");
		setPicPassword2 = sharedPreferences.getString(
				"setPicPassword2", "");
		passWord1 = sharedPreferences.getString(
				"password1", "");
		passWord2 = sharedPreferences.getString(
				"password2", "");
		layout.addView(new NinePointView(this));
		if (IsSetPicLock) {
			forgetpassword.setVisibility(View.GONE);
			piclock.setVisibility(View.VISIBLE);
			if (!passWord1.equals("")) {
				count.setText("(第二次)");
				goon.setText("确认");				
			}else {
				count.setText("(第一次)");
				goon.setText("继续"); 
			}

		} else {
			forgetpassword.setVisibility(View.VISIBLE);
			piclock.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.finish();
		super.onDestroy();
		 
	}

	private void init() {
		count = (TextView) findViewById(R.id.count);
		forgetpassword = (TextView) findViewById(R.id.forgetpassword);
		piclock = (LinearLayout) findViewById(R.id.piclock);
		layout = (LinearLayout) findViewById(R.id.layout);

		cancle = (Button) findViewById(R.id.cancle);
		goon = (Button) findViewById(R.id.goon);

	}

	private void ClickListener() {
		forgetpassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 忘记密码
			}
		});

		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 确认密码，并进行再次确认
				editor.putString("setPicPassword1", "");
				editor.putString("setPicPassword2", "");
				editor.putString("password1", "");
				editor.putString("password2", "");
				editor.putBoolean("IsSetPicLock", false);
				editor.putBoolean("IsPicLock", false);
				editor.putString("picRightPassword", "");
				editor.commit();
				Intent intent = new Intent();
				intent.setClass(LockScreenActivity.this, SettingActivity.class);
				startActivity(intent);
				finish();
			}
		});

		goon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 取消设置或更改密码

				if (goon.getText().equals("继续")) {
					//第一次画完密码
					setPicPassword1 = sharedPreferences.getString(
							"setPicPassword1", "");
					editor.putString("password1", setPicPassword1);
					editor.commit();
					Intent intent = new Intent();
					intent.setClass(LockScreenActivity.this, LockScreenActivity.class);
					startActivity(intent);
					finish();
				}else {
					editor.putString("password2", setPicPassword2);
					editor.commit();
					
					String aString = passWord1;
					setPicPassword2 = sharedPreferences.getString(
							"setPicPassword2", "");
					String bString = passWord2;
					if (passWord1.equals(setPicPassword2)) {
						//两次画的密码一样
            			editor.putString("picRightPassword", passWord1.toString());
            			editor.putString("setPicPassword1", "");
        				editor.putString("setPicPassword2", "");
        				editor.putString("password1", "");
        				editor.putString("password2", "");
						editor.putBoolean("IsSetPicLock", false);
						editor.commit();
						Intent intent = new Intent();
						intent.setClass(LockScreenActivity.this, SettingActivity.class);
						startActivity(intent);
						goon.setText("继续");
						finish();
					}else {
						//两次画的密码不一样
						goon.setText("继续");
						count.setText("(第一次)");
						editor.putString("picRightPassword","");
            			editor.putString("setPicPassword1", "");
        				editor.putString("setPicPassword2", "");
        				editor.putString("password1", "");
        				editor.putString("password2", "");
        				editor.commit();
        				Toast.makeText(getApplicationContext(), "两次输入密码不一致\n        请重新输入", Toast.LENGTH_SHORT).show();
//					    Message message = handler.obtainMessage();
//					    message.what = 1;
//					    handler.sendMessage(message);
					}
					
				}
				
			}
		});
	}
//    private Handler handler = new Handler(){
//    	public void handleMessage(Message msg) {
//			int what = msg.what;
//			if (what==1) {
//				Toast.makeText(getApplicationContext(), "两次输入密码不一致/n请重新输入", Toast.LENGTH_SHORT).show();
//			}
//		}
//    };
}
