package com.zerowire.timesheetassistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.zerowire.common.XMLUtils;
import com.zerowire.config.ConfigPara;
import com.zerowire.entity.EmployeeBean;
import com.zerowire.entity.UserInfoBean;
import com.zerowire.global.Global;
import com.zerowire.lockscreen.LockScreenActivity;
import com.zerowire.remotelogic.RemoteLogic;

public class LoginActivity extends Activity {

	
	private Button bt_login;
	private EditText et_user, et_passWord;
	private String user, password;
	private boolean IsPicLock ,retVal =true;
	UserInfoBean userInfo;
	private ProgressDialog progressDialog = null;
	Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:

				Intent intent = new Intent();
				if (IsPicLock) {
					intent.setClass(LoginActivity.this,LockScreenActivity.class);
				} else {
					intent.setClass(LoginActivity.this,MainActivity.class);
				}	
				startActivity(intent);	
				
				if (progressDialog!=null) {
				progressDialog.cancel();
			}
				
				EmployeeBean[] employee = Global.RemoteLogic().getServiceEmployee(et_user.getText().toString());
				System.out.println("employee = " + employee.toString());
				if(employee != null){
					StaticAll.employeeBean =employee;
				}
				finish();
				break;
            case 1:
            	if (progressDialog!=null) {
					progressDialog.cancel();
				}
				et_user.requestFocus();
				et_user.setError("�û��������벻��ȷ");
				break;

			default:
				break;
			}
			return false;
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		userInfo = new UserInfoBean();
		ConfigPara.sharedPreferences = getSharedPreferences(ConfigPara.PRENAME,
				0);
		ConfigPara.editor = ConfigPara.sharedPreferences.edit();
		IsPicLock = ConfigPara.sharedPreferences.getBoolean("IsPicLock", false);
		et_user = (EditText) findViewById(R.id.et_user);
		et_passWord = (EditText) findViewById(R.id.et_password);
		et_user.setText("12224");
		et_passWord.setText("123");
		user = et_user.getText().toString();
		password = et_passWord.getText().toString();
		bt_login = (Button) findViewById(R.id.bt_login);
		
		bt_login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
//				user = et_user.getText().toString();
//				password = et_passWord.getText().toString();
				if (user.equals("")) {
					et_user.requestFocus();
					et_user.setError("�������û���");
				} else if (password.equals("")) {
					et_passWord.requestFocus();
					et_passWord.setError("����������");

				} else {
					
					userInfo.setUsername(et_user.getText().toString());
					userInfo.setPwd(et_passWord.getText().toString());
					showProgressDialog();
					Thread thread = new Thread(new ForLogin());
			        thread.start();
				}
			}
		});

	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("���ڵ�½......");
		progressDialog.setIndeterminate(false);
		progressDialog.show();
	}

	@Override
	protected void onDestroy() {
		
		finish();
		super.onDestroy();
	}
	
	 private class ForLogin implements Runnable {
		@Override
		public void run() {
		
			// �Ñ������C OK
			retVal = Global.RemoteLogic().getServiceLogin(getApplicationContext(), et_user.getText().toString(), et_passWord.getText().toString());
			System.out.printf("aaaa=====",retVal);

//			retVal = true;
		if (retVal) {
			//add by frank ��¼�ɹ��Ժ� ��emplyeeId ���ó�ȫ�ֱ���
			StaticAll.emplyeeId = et_user.getText().toString();
			handler.sendEmptyMessage(0);
			// �ˆT��Ϣ�@ȡ OK

		} else {
			handler.sendEmptyMessage(1);
		} 	
    
		
//			 // ��ȡ�ͻ��˰汾��Ϣ
//			String retVal = Global.RemoteLogic().getClientVersion();
//			Object bObject= retVal;
			
			//������ĿID��ȡ��Ŀ���address��Ϣ
//			ProjectInfoBean[] retVal = Global.RemoteLogic().getServiceProjectAddressInfo("11800");
			
			//�Ƿ��л�Ʊ����
//			boolean retVal1 = Global.RemoteLogic().getHasTecketAllowance("11800","123");

			//flyback�б��ȡ
//			FlyBackBean[] retVal2 = Global.RemoteLogic().getFlyTicketList("11800","123");
			
			
			//��дTimeSheet
//			boolean retVal = Global.RemoteLogic().getResultOfWriteTimeSheet("3901","1356","0","1","0","Y","1","116.30814954222","40.056885091681");
			

			//���û�����Ϣ
//			boolean retVal = Global.RemoteLogic().setEmployeeBaseInfo("12224","15961769176","1");
						
//			String a = "s";		
						
			// �ˆT��Ϣ�@ȡ OK
//			EmployeeBean[] employee = Global.RemoteLogic().getServiceEmployee(et_user.getText().toString());
//			handler.sendEmptyMessage(0);
//			System.out.println("employee = " + employee);
			
			
			

			
			// ������Ŀ���ƻ����ڵأ�����Ա�����ڵ���Ŀ�б�
//			ProjectInfoBean[] retVal2 = Global.RemoteLogic().getServiceProjectsSearch("BPM","11800");
		
			
			

			
//			//��Ŀ�ص㡪���༭
//			LocationInfoBean locationInfoBean = new LocationInfoBean();
//			locationInfoBean.setEmployee_id("1150");
//			locationInfoBean.setLatitude("121.505157");
//			locationInfoBean.setLongitude("31.266667");
//			locationInfoBean.setProject_id("13102");
//			locationInfoBean.setRange("4");		
//			String retVal = Global.RemoteLogic().getEditLocation(locationInfoBean);
			
			
			
			
			
			
//			if (retVal != null) {
//				Log.i(">>>",">>> "+ retVal.length());
//			} else {
//				Log.i(">>>",">>> ����������");
//			}
			
			
		}		 
	 }
}
