package com.zerowire.timesheetassistant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerowire.common.LocationGetGPSImplement;
import com.zerowire.common.XMLUtils;
import com.zerowire.common.LocationGetGPSImplement.OnGPSReceived;
import com.zerowire.common.RoundImage;
import com.zerowire.common.LocationGetGPSImplement.GPSLocation;
import com.zerowire.config.ConfigPara;
import com.zerowire.entity.EmployeeBean;
import com.zerowire.entity.ProjectInfoBean;
import com.zerowire.global.Global;
import com.zerowire.helper.ImageTools;
import com.zerowire.helper.MyMoveView;
import com.zerowire.helper.PageViewController;
import com.zerowire.shake.ShakeActivity;

public class MainActivity extends Activity {
	public int index_page;
	public String[] paojectid_array;
	public String project_id, emplyee_id;
	public String str_b, str_j;
	public CheckBox buzhuBtn, jintieBtn;
	public String longitude, latitude;
	private GestureDetector gestureScanner;
	public TextView gotohome, shake;
	public ImageView iv_menu, iv_head, bg_click, head;
	private TextView tv_projectName, tv_role, menu_title;
	private Button bt_confirm, take_photo, select_photo, cancle;
	private RelativeLayout relative_bg, main;
	static View changeBackGround = null;
	/* 拍照的照片存储位置 */
	private Context context;
	private MyMoveView moveView;
	private NavigationBarActivity leftMenuView;
	public boolean isScroll = true;// 是否可以滚动
	public View view;// 主界面视图
	private Activity activity;
	private ProgressDialog coverDialog = null;
	// private Dialog nullDialog = null;

	// /////////////////////////////////////////////////////////////////////
	private ArrayList<View> pageViews;
	private PageViewController pageView;
	private boolean eventLocked, ViewLocked = false;
	private static final String MAP_URL_ROOT = "file:///android_asset/googlemap_offline_v3/";
	private static final String MAP_URL = "googlemap_offline_v3/index.html";
	private View vAdd;

	private ViewPager mViewPager;// 多页面滑动切换效果
	private ArrayList<View> views;

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:

				ProjectInfoBean[] projectInfoBeans = (ProjectInfoBean[]) msg.obj;
				if (projectInfoBeans == null) {
					break;
				}

				for (int i = 0; i < projectInfoBeans.length; i++) {
					if (projectInfoBeans[i].getHandProject().equals("1")
							&& i != 2) {
						LayoutInflater inflater = LayoutInflater
								.from(MainActivity.this);
						View v1 = inflater.inflate(R.layout.pageview_item_2,
								null);
						final ImageView myimageView = (ImageView) v1
								.findViewById(R.id.imageView);
						myimageView.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.main_background));
						tv_projectName = (TextView) v1
								.findViewById(R.id.project_name);
						tv_role = (TextView) v1.findViewById(R.id.role);
						bt_confirm = (Button) v1.findViewById(R.id.confirm);
						buzhuBtn = (CheckBox) v1.findViewById(R.id.buzhubtn);
						jintieBtn = (CheckBox) v1.findViewById(R.id.jintiebtn);

						// project_id = projectInfoBeans[i].getProjectId();
						// System.out.println("aaa =project_id == " +
						// project_id);

						// add by frank 把paojectid 存在 paojectid_array 里
						emplyee_id = StaticAll.emplyeeId;
						paojectid_array[i] = projectInfoBeans[i].getProjectId();
						// add by frank 10.08
						buzhuBtn.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								System.out.println("内部项目中填写 buzhuBtn");
								if (isChecked) {
									str_b = "1";
									System.out.println("补助选中");
								} else {
									str_b = "0";
									System.out.println("补助未选中");
								}
							}
						});
						jintieBtn
								.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(
											CompoundButton buttonView,
											boolean isChecked) {
										// TODO Auto-generated method stub
										System.out.println("内部项目中填写 jintieBtn");
										if (isChecked) {
											str_j = "Y";
											System.out.println("津贴选中");
										} else {
											str_j = "N";
											System.out.println("津贴未选中");
										}
									}
								});

						String Caption = projectInfoBeans[i].getFullName();
						String Latitude = projectInfoBeans[i].getLatitude();
						String Longitude = projectInfoBeans[i].getLongitude();
						String Depart = projectInfoBeans[i].getRoleName();
						System.out.println("qqqq==qq " + Caption);
						tv_projectName.setText(Caption);
						tv_role.setText(Depart);
						pageViews.add(0, v1);
						pageView.setPageViews(pageViews);
						pageView.SetPageViewPosition(0);

					} else {

						LayoutInflater inflater = LayoutInflater
								.from(MainActivity.this);
						View v1 = inflater.inflate(R.layout.pageview_item_1,
								null);
						final WebView myWebView = (WebView) v1
								.findViewById(R.id.webView1);
						tv_projectName = (TextView) v1
								.findViewById(R.id.project_name);
						tv_role = (TextView) v1.findViewById(R.id.role);
						bt_confirm = (Button) v1.findViewById(R.id.confirm);
						buzhuBtn = (CheckBox) v1.findViewById(R.id.buzhubtn);
						jintieBtn = (CheckBox) v1.findViewById(R.id.jintiebtn);
						// add by frank 10.08
						buzhuBtn.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								System.out.println("地图项目中填写 buzhuBtn");
								if (isChecked) {
									str_b = "1";
									System.out.println("补助选中");
								} else {
									str_b = "0";
									System.out.println("补助未选中");
								}
							}
						});
						jintieBtn
								.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(
											CompoundButton buttonView,
											boolean isChecked) {
										// TODO Auto-generated method stub
										System.out.println("地图项目中填写 jintieBtn");
										if (isChecked) {
											str_j = "Y";
											System.out.println("津贴选中");
										} else {
											str_j = "N";
											System.out.println("津贴未选中");
										}
									}
								});
						myWebView
								.setOnTouchListener(new View.OnTouchListener() {
									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										// TODO Auto-generated method stub
										if (eventLocked) {
											return true;
										} else {
											return false;
										}
									}
								});

						bt_confirm
								.setOnClickListener(new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

									}
								});
						myWebView.getSettings().setJavaScriptEnabled(true);
						String Caption = projectInfoBeans[i].getFullName();
						String Latitude = projectInfoBeans[i].getLatitude();
						String Longitude = projectInfoBeans[i].getLongitude();
						String Depart = projectInfoBeans[i].getRoleName();
						Latitude = "23.147491";
						Longitude = "113.331298";
						tv_projectName.setText(Caption);
						tv_role.setText(Depart);
						String Zoom = "8";
						StringBuilder myHtmlString = new StringBuilder();
						SetMapParams(myHtmlString, Caption, Latitude,
								Longitude, Zoom);
						myWebView.loadDataWithBaseURL(MAP_URL_ROOT,
								myHtmlString.toString(), "text/html", "UTF-8",
								null);
						pageViews.add(0, v1);
						pageView.setPageViews(pageViews);
						pageView.SetPageViewPosition(0);
					}
					bt_confirm.setOnClickListener(new Button.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							System.out.println("点击 confirmbtn 按钮 ");
							// 填写TimeSheet
							// add by frank 设置timesheet填写的 值
							String str = bt_confirm.getText().toString();

							if (str.equals("提交")) {
								// project_id = paojectid_array [index_page];
								// System.out.println("pppp == project_id = " +
								// project_id);
								// add by frank 10.08
								System.out
										.println("补助 " + str_b + "津贴" + str_j);
								if ((str_b.equals("") && str_j.equals(""))
										|| (str_b.equals("0") && str_j
												.equals("N"))) {
									dialog();// 点击提交按钮，如果未填写timeSheet，提示警告。
								} else {
									// add by frank 10.08
									if (str_b.equals("")) { // 只点击一个按钮的情况下提交，另一个按钮设置默认值。
										str_b = "0";
									} else if (str_j.equals("")) {
										str_j = "N";
									}
									// ( projectId, resourceId, addressId,
									// isOffBase, ticketAllowance,
									// projectAllowanceFlag, clientFeedInfo,
									// longitude, latitude)
									boolean retVal = Global.RemoteLogic()
											.getResultOfWriteTimeSheet(
													project_id, emplyee_id,
													"0", str_b, "0", str_j,
													"1", longitude, latitude);
									System.out.println("confirmResult ="
											+ retVal);
									if (retVal) {
										XMLUtils.toXml(null, emplyee_id);// 提交数据保存在文件中
										for (int i = 0; i < pageViews.size(); i++) {
											View view = (View) pageViews.get(i);
											Button btn = (Button) view
													.findViewById(R.id.confirm);
											btn.setText("重新提交");
										}
									}
								}
							} else {
								// add by frank 10.08
								if (str_b.equals("0") && str_j.equals("N")) {
									dialog();// 先选中然后全部取消选中
								} else {
									showAlertViewDialog();
								}
							}
						}
					});
				}
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
		this.context = this;
		paojectid_array = new String[30];

		// add by frank 获得经纬度
		LocationGetGPSImplement mLocationGetGPSImplement = null;
		mLocationGetGPSImplement = new LocationGetGPSImplement(this, "获取位置信息",
				new OnGPSReceived() {
					@Override
					public void onGPSReceived(GPSLocation senderGPSLocation) {
						Log.i("纬度>>>>> ", String.valueOf(senderGPSLocation
								.getLongitude()));
						Log.i("经度>>>>>  ",
								String.valueOf(senderGPSLocation.getLatitude()));
						longitude = String.valueOf(senderGPSLocation
								.getLongitude());
						latitude = String.valueOf(senderGPSLocation
								.getLatitude());
					}
				});
		init();

		// add by frank
		str_b = "";
		str_j = "";
		index_page = 0;

		// if (ConfigPara.SettingToMain) {
		// initSliding();
		// OnClickLinsenter();
		// }else {
		initMap();
		initSliding();
		initSharedPreferences();
		initBackGround();
		OnClickLinsenter();
		// }

	}

	// add by frank 10.08
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		// builder.setTitle("提示");
		builder.setMessage("TimeSheet未填写");
		builder.setPositiveButton("确认", new Dialog.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

	// add by frank 10.08
	protected void showAlertViewDialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("覆盖上次提交内容");
		builder.setPositiveButton("确认", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Global.RemoteLogic().getResultOfWriteTimeSheet(project_id,
						emplyee_id, "0", str_b, "0", str_j, "1", longitude,
						latitude);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
		// AlertDialog.Builder builder = new Builder(MainActivity.this);
		// //builder.setTitle("提示");
		// builder.setMessage("覆盖上次提交内容");
		// builder.setPositiveButton("确认", new Dialog.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int which) {
		// Global.RemoteLogic().getResultOfWriteTimeSheet(project_id,emplyee_id,"0",str_b,"0",str_j,"1",longitude,latitude);
		// dialog.dismiss();
		// }
		//
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// builder.create().show();
	}

	// private void showAlertViewDialog() {
	// coverDialog = new ProgressDialog(MainActivity.this);
	// coverDialog.setIndeterminate(false);
	// coverDialog.setTitle("覆盖上次提交内容");
	// coverDialog.setButton("确定", new DialogInterface.OnClickListener(){
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// // add by frank 点击确定 重新提交一次
	// Global.RemoteLogic().getResultOfWriteTimeSheet(project_id,emplyee_id,"0",str_b,"0",str_j,"1",longitude,latitude);
	// }
	// });
	// coverDialog.show();
	// }

	private void OnClickLinsenter() {
		Intent intent = new Intent();
		// iv_menu.setOnClickListener(new ImageView.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // Auto-generated method stub
		// moveView.showHideLeftMenu();
		// ConfigPara.IsAtMain = !ConfigPara.IsAtMain;
		//
		// }
		// });
		//
		// bg_click.setOnClickListener(new RelativeLayout.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ViewLocked = true;
		// ConfigPara.head_clicked = false;
		// bg_click.setClickable(false);
		// iv_head.setClickable(false);
		// iv_menu.setClickable(false);
		// menu_title.setText("更改背景");
		// relative_bg.addView(changeBackGround);
		// }
		// });
		//
		// take_photo.setOnClickListener(new Button.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// String status = Environment.getExternalStorageState();
		// if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
		// // doTakePhoto();// 用户点击了从照相机获取
		//
		// Intent openCameraIntent = new Intent(
		// MediaStore.ACTION_IMAGE_CAPTURE);
		// Uri imageUri = Uri.fromFile(new File(Environment
		// .getExternalStorageDirectory(), "image.jpg"));
		// // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		// openCameraIntent
		// .putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		// startActivityForResult(openCameraIntent,
		// ConfigPara.CAMERA_WITH_DATA);
		//
		// } else {
		// showToast("未发现SD卡");
		// }
		// }
		// });
		// select_photo.setOnClickListener(new Button.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// String status = Environment.getExternalStorageState();
		// if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
		// // doPickPhotoFromGallery();// 从相册中去获取
		// Intent openAlbumIntent = new Intent(
		// Intent.ACTION_GET_CONTENT);
		// openAlbumIntent.setType("image/*");
		// startActivityForResult(openAlbumIntent,
		// ConfigPara.PHOTO_PICKED_WITH_DATA);
		// } else {
		// showToast("未发现SD卡");
		// }
		// }
		// });
		// cancle.setOnClickListener(new Button.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// relative_bg.removeView(changeBackGround);
		// bg_click.setClickable(true);
		// iv_head.setClickable(true);
		// iv_menu.setClickable(true);
		// ViewLocked = false;
		// Log.i("ViewLocked1", ViewLocked+"");
		// }
		// });
		//
		// leftMenuView.setting
		// .setOnClickListener(new ImageView.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setClass(MainActivity.this,
		// SettingActivity.class);
		// startActivity(intent);
		//
		// overridePendingTransition(R.anim.short_in_from_right,
		// R.anim.long_out_to_left);
		//
		// }
		// });
		leftMenuView.shake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ShakeActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.short_in_from_right,
						R.anim.long_out_to_left);

			}
		});
		leftMenuView.gotohome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveView.moveToMain(true, 0);
				ConfigPara.IsAtMain = true;
			}
		});
	}

	private void initBackGround() {
		// TODO Auto-generated method stub
		// 下载背景图片
		ConfigPara.backGroundBitmap = Global.RemoteLogic()
				.getDownloadBackgroudImage(
						(StaticAll.employeeBean[0].getBackgroundImgId()));
		if (ConfigPara.backGroundBitmap != null
				&& !ConfigPara.backGroundBitmap.equals("null")) {
			bg_click.setBackgroundDrawable(new BitmapDrawable(
					ConfigPara.backGroundBitmap));
		}
		// 下载头像
		ConfigPara.headBitmap = Global.RemoteLogic().getDownloadAvatarImage(
				StaticAll.employeeBean[0].getAvatarImgId());

		if (ConfigPara.headBitmap != null
				&& !ConfigPara.headBitmap.equals("null")) {
			Bitmap bitmap = new BitmapDrawable(ConfigPara.headBitmap)
					.getBitmap();
			iv_head.setBackgroundDrawable(new BitmapDrawable(RoundImage
					.getRoundedCornerBitmap(bitmap)));
			leftMenuView.head.setBackgroundDrawable(new BitmapDrawable(
					RoundImage.getRoundedCornerBitmap(bitmap)));
		}
	}

	public MainActivity() {
		super();
	}

	public MainActivity(Activity activity) {
		super();
		this.activity = activity;
	}

	private void initSharedPreferences() {
		ConfigPara.sharedPreferences = getSharedPreferences(ConfigPara.PRENAME,
				0);
		boolean isFirstRun = ConfigPara.sharedPreferences.getBoolean(
				"isFirstRun", true);
		ConfigPara.editor = ConfigPara.sharedPreferences.edit();
		if (isFirstRun) {
			ConfigPara.editor.putBoolean("isFirstRun", false);
			ConfigPara.editor.putString("picRightPassword", "");// 手势密码
			ConfigPara.editor.putBoolean("IsSetPicLock", false);// 是否在设置手势密码，用于判别是在登陆还是设置
			ConfigPara.editor.putBoolean("IsPicLock", false); // 手势密码是否打开
			ConfigPara.editor.commit();
		}
	}

	private void initSliding() {
		// 创建移动视图
		moveView = new MyMoveView(context);
		// 创建左边菜单视图
		leftMenuView = new NavigationBarActivity(this, moveView);
		setMyMoveView(moveView);// 赋值移动视图
		// 移动视图默认界面：界面01，
		moveView.setMainView(this, leftMenuView);
		setContentView(moveView);
		if (ConfigPara.IsAtMain) {
			moveView.moveToMain(true, 0);
		}
	}

	// /////////////////////////////////地图///////////////////////////////////////
	private void initMap() {
		eventLocked = false;
		pageViews = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		vAdd = inflater.inflate(R.layout.pageview_item_0, null);
		Button btnAdd = (Button) vAdd.findViewById(R.id.button1);
		btnAdd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ProjectListActivity.class);
				startActivityForResult(intent, 100);
			}
		});

		// pageViews.add(vAdd);
		LinearLayout pageViewPanel = (LinearLayout) view
				.findViewById(R.id.pageViewLinearLayout);
		pageView = new PageViewController(MainActivity.this, mPageChange,
				pageViewPanel, pageViews, false);
		new Thread() {
			public void run() {

				// 根据员工ID获取员工所在项目列表
				Message msg = handler
						.obtainMessage(
								0,
								Global.RemoteLogic().getServiceProjects(
										"12224", "23.147491", "113.331298"));
				handler.sendMessage(msg);
			};
		}.start();

	}

	private OnPageChangeListener mPageChange = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			eventLocked = false;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stu
			index_page = arg0;
			System.out.println("arg0 == " + arg0);
			eventLocked = true;
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			System.out.println("arg0 == " + arg0);
			eventLocked = false;
		}

	};
	private int MENU_ITEM_COUNTER;

	private void SetMapParams(StringBuilder senderSB, String senderCap,
			String senderLat, String senderLong, String senderZoom) {
		try {
			InputStream fileIS = getApplicationContext().getAssets().open(
					MAP_URL);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(fileIS));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replace("#CAPTION#", senderCap);
				line = line.replace("#LAT#", senderLat);
				line = line.replace("#LONG#", senderLong);
				line = line.replace("#ZOOM#", senderZoom);
				senderSB.append(line);
				senderSB.append('\n');
			}
			br.close();
			fileIS.close();
		} catch (IOException e) {
			// 增加异常处理
			e.printStackTrace();
		}
	}

	/**
	 * 赋值移动视图
	 * 
	 * @param myMoveView
	 */
	public void setMyMoveView(MyMoveView myMoveView) {

		this.moveView = myMoveView;
	}

	public boolean isScroll() {

		return isScroll;
	}

	public void setScroll(boolean isScroll) {

		this.isScroll = isScroll;
	}

	public View getView() {
		// System.out.println("getView");
		// System.out.println("getScrollX ==" + view.getScrollX());
		//
		// if(view.getScrollX() >0){
		// index_page ++ ;
		// System.out.println("getView  page ==" + index_page);
		// }else{
		// index_page -- ;
		// System.out.println("getView  page ==" + index_page);
		// }

		return view;
	}

	public void setView(View view) {

		this.view = view;
	}

	// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case ConfigPara.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的

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
					relative_bg.removeView(changeBackGround); // 去掉背景选择的view

					bg_click.setBackgroundDrawable(new BitmapDrawable(
							smallBitmap)); // 设置背景
					bg_click.setClickable(true);

					// 上传背景图片
					String logId = UUID.randomUUID().toString();
					String FilePath = getPath(originalUri);
					boolean retVal = Global.RemoteLogic()
							.getUploadBackgroudImage(
									StaticAll.employeeBean[0].getEmployeeId(),
									logId, logId, FilePath);
					if (!retVal) {
						Toast.makeText(MainActivity.this, "上传失败",
								Toast.LENGTH_SHORT).show();
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		}
		case ConfigPara.CAMERA_WITH_DATA: {

			// 将保存在本地的图片取出并缩小后显示在界面上
			Bitmap bitmap = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory() + "/image.jpg");
			Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()
					/ ConfigPara.SCALE, bitmap.getHeight() / ConfigPara.SCALE);
			// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
			bitmap.recycle();
			relative_bg.removeView(changeBackGround);

			// 将处理过的图片显示在界面上，并保存到本地
			bg_click.setBackgroundDrawable(new BitmapDrawable(newBitmap));
			bg_click.setClickable(true);
			// leftMenuView.ll_sliding.setClickable(true);
			ImageTools.savePhotoToSDCard(newBitmap, Environment
					.getExternalStorageDirectory().getAbsolutePath(), String
					.valueOf(System.currentTimeMillis()));
			ConfigPara.editor.putString("bgbitmap",
					Environment.getExternalStorageDirectory() + "/image.jpg");
			ConfigPara.editor.commit();
			String bg = ConfigPara.sharedPreferences.getString("bgbitmap", "");

			String logId = UUID.randomUUID().toString();
			boolean retVal = Global.RemoteLogic()
					.getUploadBackgroudImage(
							StaticAll.employeeBean[0].getEmployeeId(), logId,
							logId, bg);
			// String aString = head;
			// String bString = aString;
			break;

		}
		case ConfigPara.PROJECT_INFO: {
			// AddMap();

			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View v1 = inflater.inflate(R.layout.pageview_item_1, null);
			final WebView myWebView = (WebView) v1.findViewById(R.id.webView1);
			tv_projectName = (TextView) v1.findViewById(R.id.project_name);
			tv_role = (TextView) v1.findViewById(R.id.role);
			bt_confirm = (Button) v1.findViewById(R.id.confirm);
			myWebView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (eventLocked) {
						return true;
					} else {
						return false;
					}
				}
			});

			bt_confirm.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			myWebView.getSettings().setJavaScriptEnabled(true);

			String Caption = data.getExtras().getString("project_name");
			String Latitude = data.getExtras().getString("latitude");
			String Longitude = data.getExtras().getString("longitude");
			// String Name = data.getExtras().getString("name");
			String Depart = data.getExtras().getString("depart");
			tv_projectName.setText(Caption);
			tv_role.setText(Depart);
			String Zoom = "8";
			StringBuilder myHtmlString = new StringBuilder();
			SetMapParams(myHtmlString, Caption, Latitude, Longitude, Zoom);
			myWebView.loadDataWithBaseURL(MAP_URL_ROOT,
					myHtmlString.toString(), "text/html", "UTF-8", null);
			pageViews.add(0, v1);
			pageView.setPageViews(pageViews);
			pageView.SetPageViewPosition(0);
		}
		}
	}

	public String getPath(Uri uri) {
		// 将url转化为路径保存
		String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
		Cursor cursor = this.managedQuery(uri, imgs, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String img_url = cursor.getString(index);
		return cursor.getString(index);
	}

	private void init() {
		view = View.inflate(this, R.layout.main_layout, null);
		iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
		iv_head = (ImageView) view.findViewById(R.id.iv_head);
		bg_click = (ImageView) view.findViewById(R.id.bg);
		relative_bg = (RelativeLayout) view.findViewById(R.id.main);
		changeBackGround = View.inflate(this, R.layout.menu_layout, null);
		menu_title = (TextView) changeBackGround.findViewById(R.id.tv_title);
		// gotohome =(TextView)leftMenuView.findViewById(R.id.timesheet);
		// shake = (TextView)leftMenuView.findViewById(R.id.shake);
		// leftMenuView.gotohome.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// moveView.moveToMain(true, 0);
		// ConfigPara.IsAtMain = true;
		// }
		// });
		// leftMenuView.shake.setOnClickListener(new Button.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		changeBackGround.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (ViewLocked) {
					Log.i("ViewLocked2", ViewLocked + "");
					return true;
				} else {
					Log.i("ViewLocked3", ViewLocked + "");
					return false;
				}
			}
		});

	}

	public void GoSetting(View view) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.short_in_from_right,
				R.anim.long_out_to_left);
	}

	public void ChangeToMainOrLeft(View view) {
		moveView.showHideLeftMenu();
		ConfigPara.IsAtMain = !ConfigPara.IsAtMain;
	}

	public void ChangeBackGround(View view) {
		ViewLocked = true;
		ConfigPara.head_clicked = false;
		bg_click.setClickable(false);
		iv_head.setClickable(false);
		iv_menu.setClickable(false);
		menu_title.setText("更改背景");
		relative_bg.addView(changeBackGround);
	}

	public void SelectPhoto(View view) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			// doPickPhotoFromGallery();// 从相册中去获取
			Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent,
					ConfigPara.PHOTO_PICKED_WITH_DATA);
		} else {
			showToast("未发现SD卡");
		}
	}

	public void CanclePhotoView(View view) {
		relative_bg.removeView(changeBackGround);
		bg_click.setClickable(true);
		iv_head.setClickable(true);
		iv_menu.setClickable(true);
		ViewLocked = false;
		Log.i("ViewLocked1", ViewLocked + "");
	}

	public void TakePhoto(View view) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			// doTakePhoto();// 用户点击了从照相机获取

			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			Uri imageUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "image.jpg"));
			// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(openCameraIntent,
					ConfigPara.CAMERA_WITH_DATA);

		} else {
			showToast("未发现SD卡");
		}
	}

	public void goAllowance(View view) {

	}

	public void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/*
	 * 点击返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ConfigPara.IsAtMain == true) {
				AlertDialog isExit = new AlertDialog.Builder(this)
						.setTitle(getString(R.string.systemtip))
						.setMessage(getString(R.string.issuretoexit))
						.setPositiveButton(getString(R.string.Confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								})
						.setNegativeButton(getString(R.string.Cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create();
				isExit.show();
			} else {
				moveView.moveToMain(true, 0);
				ConfigPara.IsAtMain = true;
			}
		}

		return super.onKeyDown(keyCode, event);// add by frank 10.09
	}

	// add by frank 10.09
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "添加项目");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			System.out.println("添加项目按钮");
			Intent intent = new Intent(MainActivity.this,
					ProjectListActivity.class);
			startActivityForResult(intent, 100);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
