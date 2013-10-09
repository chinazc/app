package com.zerowire.timesheetassistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zerowire.config.ConfigPara;
import com.zerowire.global.Global;
import com.zerowire.helper.ImageTools;
import com.zerowire.helper.MyMoveView;
import com.zerowire.lockscreen.LockScreenActivity;

public class SettingActivity extends Activity {
private ImageView iv_head;
private ImageButton ib_menu;
private EditText et_Name,et_Post,et_Email,et_Phone,et_Birthday,et_Sign;
private ToggleButton toggle;
private Button logout,btnEdit;
private LayoutParams params;
static View changeHead=null;
private Button take_photo,select_photo,cancle;
private TextView menu_title;
private RelativeLayout relativeLayout;
private MainActivity mainActivity;
private boolean isEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		mainActivity = new MainActivity(this);
		init();
		initBackGround();
		Clicklistener();
	}
 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	     finish();
		super.onDestroy();
		
	}
 
	private void initBackGround() {
		// TODO Auto-generated method stub
		String path = ConfigPara.sharedPreferences.getString("headbitmap", "");
		if (path!=null&&!path.equals("")) {
//			Bitmap bitmap = BitmapFactory.decodeFile(path);
//			Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / ConfigPara.SCALE, bitmap.getHeight() / ConfigPara.SCALE);
			//������Bitmap������������������������������������������������������������out of memory������
//			bitmap.recycle();			
			iv_head.setBackgroundDrawable(new BitmapDrawable(ConfigPara.headBitmap));
		}
	}
private void init(){
	 
	ConfigPara.sharedPreferences = getSharedPreferences(ConfigPara.PRENAME, 0);
	ConfigPara.editor = ConfigPara.sharedPreferences.edit();
	 params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);  
		params.layoutAnimationParameters = null;
		ib_menu = (ImageButton)findViewById(R.id.ib_menu);
		btnEdit=(Button)findViewById(R.id.btn_edit);
	 iv_head = (ImageView)findViewById(R.id.iv_head);
	 et_Name = (EditText)findViewById(R.id.name);
	 et_Post = (EditText)findViewById(R.id.post);
	 et_Email = (EditText)findViewById(R.id.email);
	 et_Phone = (EditText)findViewById(R.id.telephone);
	 et_Birthday = (EditText)findViewById(R.id.birthday);
	 et_Sign = (EditText)findViewById(R.id.sign);
	 	 
	 et_Name.setEnabled(false);
	 et_Post.setEnabled(false);
	 et_Email.setEnabled(false);
	 et_Phone.setEnabled(false);
	 et_Birthday.setEnabled(false);
	 et_Sign.setEnabled(false);
	 
	 et_Name.setText(StaticAll.employeeBean[0].getName());
	 et_Post.setText(StaticAll.employeeBean[0].getPositionName());
	 et_Email.setText(StaticAll.employeeBean[0].getEmail());
	 et_Phone.setText(StaticAll.employeeBean[0].getPhoneNumber());
	 //et_Birthday.setText(StaticAll.employeeBean[0].get());
	 

	 toggle = (ToggleButton)findViewById(R.id.toggle);
	 logout = (Button)findViewById(R.id.logout);
	 relativeLayout = (RelativeLayout)findViewById(R.id.relative);
	 
	 boolean IsPicLock = ConfigPara.sharedPreferences.getBoolean("IsPicLock", false);
	 toggle.setChecked(IsPicLock);
	 changeHead = View.inflate(this, R.layout.menu_layout, null);
		take_photo = (Button)changeHead.findViewById(R.id.take_photo);
		select_photo = (Button)changeHead.findViewById(R.id.select_photo);
		cancle = (Button)changeHead.findViewById(R.id.cancle);
		menu_title = (TextView)changeHead.findViewById(R.id.tv_title);
 }
 private void Clicklistener(){
	 
	 ib_menu.setOnClickListener(new ImageView.OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			overridePendingTransition(R.anim.short_in_from_left,  R.anim.long_out_from_left);
		}
	});
	 //edit by Frank
	 btnEdit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(!isEdit){
				btnEdit.setText("完成");
				et_Phone.setEnabled(true);
				et_Sign.setEnabled(true);
				isEdit=true;
			}else{
				btnEdit.setText("编辑");
				et_Phone.setEnabled(false);
				et_Sign.setEnabled(false);
				isEdit=false;
				boolean retVal = Global.RemoteLogic().setEmployeeBaseInfo(StaticAll.employeeBean[0].getEmployeeId(),
						et_Phone.getText().toString(),et_Sign.getText().toString());
				if(!retVal){
					Toast.makeText(SettingActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
	});
    toggle.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if (isChecked) {				
				ConfigPara.editor.putBoolean("IsPicLock", true);
				ConfigPara.editor.putBoolean("IsSetPicLock", true);
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, LockScreenActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.short_in_from_right,  R.anim.long_out_to_left);				finish();
			}else {
				//������������������
				ConfigPara.editor.putBoolean("IsPicLock", false);
			}
			ConfigPara.editor.commit();
		}
	});
    
	iv_head.setOnClickListener(new ImageView.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_head.setClickable(false);
				ConfigPara.head_clicked=true;
				menu_title.setText("���������������");
				relativeLayout.addView(changeHead, params);
			}
		});
	
	take_photo.setOnClickListener(new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String status=Environment.getExternalStorageState();  
            if(status.equals(Environment.MEDIA_MOUNTED)){//���������������SD���       	
            	Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
				//���������������������������SD���������image.jpg������������������������������������������������������������������
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, ConfigPara.CAMERA_WITH_DATA);
            } else{  
            	mainActivity.showToast("���������SD���");  
            }   
		}
	});
	select_photo.setOnClickListener(new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, ConfigPara.PHOTO_PICKED_WITH_DATA);
		}
	});
	cancle.setOnClickListener(new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			relativeLayout.removeView(changeHead);
			iv_head.setClickable(true);
		}
	});
	 
 }
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
     if (resultCode != RESULT_OK)  
         return;  
     switch (requestCode) {  
         case ConfigPara.PHOTO_PICKED_WITH_DATA: {// ������Gallery���������
//
//         	ContentResolver resolver = getContentResolver();
//				//���������������������������
//				Uri originalUri = data.getData(); 
//	            try {
//	            	//������ContentProvider������URI������������������
//					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//					if (photo != null) {
//						//������������������������������������������������������������������������������������������������Bitmap���������������
//						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / ConfigPara.SCALE, photo.getHeight() / ConfigPara.SCALE);
//						//������������������������������������������out of memory������������
//						photo.recycle();							
//						relativeLayout.removeView(changeHead); 
//						iv_head.setBackgroundDrawable(new BitmapDrawable(smallBitmap));  
//						iv_head.setClickable(true);
//						ConfigPara.editor.putString("headbitmap",getPath(originalUri));
//						ConfigPara.editor.commit(); 
//					}
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}  
//				break;
        	 ContentResolver resolver = getContentResolver();
 			// 照片的原始资源地址
 			Uri originalUri = data.getData();
 			try {
 				// 使用ContentProvider通过URI获取原始图片
 				Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
 						originalUri);
 				if (photo != null) {
 					// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
 					Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
 							photo.getWidth() / ConfigPara.SCALE,
 							photo.getHeight() / ConfigPara.SCALE);
 					// 释放原始图片占用的内存，防止out of memory异常发生
 					photo.recycle();
 					relativeLayout.removeView(changeHead); // 去掉背景选择的view

 					iv_head.setBackgroundDrawable(new BitmapDrawable(
 							smallBitmap)); // 设置背景
 					iv_head.setClickable(true);
 					ConfigPara.editor.putString("headbitmap",getPath(originalUri));
					ConfigPara.editor.commit(); 

 					// 上传头像图片
 					String logId = UUID.randomUUID().toString();
 					String FilePath = getPath(originalUri);
 					boolean retVal = Global.RemoteLogic().getUploadAvatarImage(StaticAll.employeeBean[0].getEmployeeId(), logId, logId, FilePath);
 					if(!retVal){
 						Toast.makeText(SettingActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
 					}

 				}
 			} catch (FileNotFoundException e) { 
 				e.printStackTrace();
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 			break;
			
         }  
         case ConfigPara.CAMERA_WITH_DATA: {// ������������������������,��������������������������������������������� 

//				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
//				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / ConfigPara.SCALE, bitmap.getHeight() / ConfigPara.SCALE);
//				//������Bitmap������������������������������������������������������������out of memory������
//				bitmap.recycle();
//				relativeLayout.removeView(changeHead); 
//				//������������������������������������������������������������
//				iv_head.setBackgroundDrawable(new BitmapDrawable(newBitmap));
//				ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
//				ConfigPara.editor.putString("headbitmap", Environment.getExternalStorageDirectory()+"/image.jpg");
//				ConfigPara.editor.commit();
//				break;
				

				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()
						/ ConfigPara.SCALE, bitmap.getHeight() / ConfigPara.SCALE);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				relativeLayout.removeView(changeHead);

				// 将处理过的图片显示在界面上，并保存到本地
				iv_head.setBackgroundDrawable(new BitmapDrawable(newBitmap));
				iv_head.setClickable(true);
				// leftMenuView.ll_sliding.setClickable(true);
				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(), String
						.valueOf(System.currentTimeMillis()));
				ConfigPara.editor.putString("bgbitmap",Environment.getExternalStorageDirectory() + "/image.jpg");
				ConfigPara.editor.commit();
				String i_head = ConfigPara.sharedPreferences.getString("bgbitmap", "");

				String logId = UUID.randomUUID().toString();
				boolean retVal = Global.RemoteLogic().getUploadAvatarImage(StaticAll.employeeBean[0].getEmployeeId(), logId, logId, i_head);
//				String aString = head;
//				String bString = aString;
				break;

         }
     }
 }
	public  String getPath(Uri uri) {
		 // ���url���������������������
       String []imgs={MediaStore.Images.Media.DATA};//���������URI���������������������  
       Cursor cursor=this.managedQuery(uri, imgs, null, null, null);  
       int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
       cursor.moveToFirst();  
       String img_url=cursor.getString(index);
		return cursor.getString(index); 
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 1, 0, "编辑");
//		return super.onCreateOptionsMenu(menu);
//	}
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case 1:
//			et_Phone.setEnabled(true);
//			et_Sign.setEnabled(true);
//			break;
//
//		default:
//			break;
//		}
//			
//		return super.onOptionsItemSelected(item);
//	}
}
