package com.zerowire.timesheetassistant;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zerowire.common.RoundImage;
import com.zerowire.config.ConfigPara;
import com.zerowire.global.Global;
import com.zerowire.helper.MyMoveView;
import com.zerowire.shake.ShakeActivity;
//���������
public class NavigationBarActivity extends Activity  {
	public TextView tvName,position,sign;
	public ImageView setting,head;
	public TextView gotohome,shake ;
	public LinearLayout ll_sliding;
	public TextView versionInfo,useHelp,suggestionBack,aboutMe,checkUpdate;
	public static MainActivity mainActivity;
	public View view_naviga;
	public MyMoveView moveView;
	public ScrollView scrollview;
	public LinearLayout linearLayout;
	public NavigationBarActivity(MainActivity mainActivity,MyMoveView moveView){
		this.mainActivity = mainActivity;
		this.moveView = moveView;
		
		init();
	}
	
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.sliding_menu);  
 
    } 
	
	private void init(){


		view_naviga = LayoutInflater.from(mainActivity).inflate(R.layout.sliding_menu, null);
		linearLayout = (LinearLayout)view_naviga.findViewById(R.id.linear);
		tvName =(TextView)view_naviga.findViewById(R.id.name);
		position =(TextView)view_naviga.findViewById(R.id.position);
		sign =(TextView)view_naviga.findViewById(R.id.sign);
		LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(
		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		sp_params.width = (int) (sp_params.width*(0.63f));                 
//   linearLayout.setLayoutParams(sp_params);
        ll_sliding = (LinearLayout) view_naviga.findViewById(R.id.ll_sliding);
        scrollview = (ScrollView) view_naviga.findViewById(R.id.scrollview);
		versionInfo = (TextView)view_naviga.findViewById(R.id.version_info);
		useHelp = (TextView)view_naviga.findViewById(R.id.help);
		suggestionBack = (TextView)view_naviga.findViewById(R.id.suggestion);
		aboutMe = (TextView)view_naviga.findViewById(R.id.about_me);
		checkUpdate = (TextView)view_naviga.findViewById(R.id.update);
		head = (ImageView)view_naviga.findViewById(R.id.slid_head);
        gotohome =(TextView)view_naviga.findViewById(R.id.timesheet);
        shake = (TextView)view_naviga.findViewById(R.id.shake);
        
        //addby frank 设置姓名，职位，签名
        tvName.setText(StaticAll.employeeBean[0].getName());
   	 	position.setText(StaticAll.employeeBean[0].getPositionName());
   	 	sign.setText(StaticAll.employeeBean[0].getSignature());
   	 	

	}
	
//	public void setWidth(int w) { 
//		// ������������������������������ ���������������������������������fill_parent������������������������������������������������ 
//		LayoutParams p = view_naviga.getLayoutParams();
//		p.width = w;
//		view_naviga.setLayoutParams(p);
//		
//		ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
//		params.width = w;
//		linearLayout.setLayoutParams(params);
//	}

	public View getView() {
		return view_naviga;
	}
}
