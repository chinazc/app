package com.zerowire.timesheetassistant;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.zerowire.entity.LocationInfoBean;
import com.zerowire.entity.ProjectInfoBean;
import com.zerowire.global.Global;

public class ProjectListActivity extends Activity {

	private String names[], project_names[], departs[];
	private ArrayList<ProjectInfoBean> project;
	private String latitudes[];
	private String longitudes[];
	private String keyWord;
	private ListView listView;
	private ImageButton bt_back;
	private EditText search;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private HashMap<String, Object> map;
	ArrayList<String> projectNameList = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.project_list);

		bt_back = (ImageButton) findViewById(R.id.right_btn);
		listView = (ListView) findViewById(R.id.project_list);
		search = (EditText) findViewById(R.id.search);
		project =new ArrayList<ProjectInfoBean>();
		listItem = new ArrayList<HashMap<String, Object>>();
		
		adapter = new SimpleAdapter(ProjectListActivity.this, listItem, R.layout.project_list_item,
				new String[] { "projectName" }, new int[] { R.id.pro_name });
		listView.setAdapter(adapter);
		//add by frank 10.09
		search.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH)
						&& event != null) {
					//System.out.println("search" + event.getAction());
					System.out.println("StaticAll.emplyeeId = " + StaticAll.emplyeeId);
					ProjectInfoBean[] retVal2 = Global.RemoteLogic().getServiceProjectsSearch(search.getText().toString(),StaticAll.emplyeeId);
					System.out.println("search retVal2 = " + retVal2[0].getFullName());
					if(retVal2.length >0){
						for (int i =0 ;i<retVal2.length;i++){
							
							project.add(retVal2[i]);
						}
					}
//					project_names=new String[proj_names.size()];
					for (int i = 0; i < project.size(); i++) {
						map = new HashMap<String, Object>();
						map.put("projectName", project.get(i).getFullName());
						listItem.add(map);
					}
				}
				
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		
		bt_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProjectListActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});

		
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//add by frank 10.09
				ProjectInfoBean bean=project.get(arg2);
				//判断项目是否存在
				LocationInfoBean locationInfoBean= new LocationInfoBean();
				locationInfoBean.setEmployee_id(StaticAll.emplyeeId);
				locationInfoBean.setLatitude(bean.getLatitude());
				locationInfoBean.setLongitude(bean.getLongitude());
				locationInfoBean.setProject_id(bean.getProjectId());
				locationInfoBean.setRange("4");
				boolean retVal = Global.RemoteLogic().getExistLocation(locationInfoBean);
				System.out.println("1234   retVal" + retVal);
				if(!retVal){
					//如果存在出现警告
//					Toast.makeText(ProjectListActivity.this, "项目信息已经存在，不能重复添加",
//							Toast.LENGTH_SHORT).show();
					AlertDialog.Builder builder = new Builder(ProjectListActivity.this);
					builder.setMessage("项目信息已经存在，不能重复添加");
					builder.setPositiveButton("确认", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
					builder.create().show();
				}else{
					//上传添加项目地，不返回空串说明上传成功。
					String result = Global.RemoteLogic().getCreateLocation(locationInfoBean);
					if(!result. equals("")){
//						Toast.makeText(ProjectListActivity.this, "添加失败,请确认您的项目角色能够添加项目地点",
//								Toast.LENGTH_SHORT).show();
						AlertDialog.Builder builder = new Builder(ProjectListActivity.this);
						builder.setMessage("添加失败,请确认您的项目角色能够添加项目地点");
						builder.setPositiveButton("确认", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								finish();
							}
						});
						builder.create().show();
					}
				}
				
				
			}
		});
	}
}

