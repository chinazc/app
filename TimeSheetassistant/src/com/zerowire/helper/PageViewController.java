package com.zerowire.helper;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zerowire.timesheetassistant.R;

/**
 * Android Page View
 * 
 * 
 * @author jason.wang
 * 
 * Bug fix 2013.07.19
 * ������������������������View���������
 * 
 */
public class PageViewController {

	private Context context;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// ������������
	private ViewGroup main;
	// ������������ LinearLayout
	private ViewGroup group;
	// ������������ LinearLayout
	private ViewPager viewPager;
	private OnPageChangeListener mCallBack = null;
	private boolean showNavHint = true;

	private GuidePageAdapter pageAdapter;
	
	/**
	 * ������������������������:
	 * <code> 
	 * // ��������� 
	 * PageViewController pageView = 
	 * 		new PageViewController(Activity.this, pageChangeEvent, pageParentViewPanel,pageViews, true); 
	 * // ������������������ 
	 * pageView.setPageViews(pageViews); 
	 * // ������������������������
	 * pageView.SetPageViewPosition(0);
	 * </code>
	 * 
	 * @param senderContext
	 *            context
	 * @param senderCallBack
	 *            ������������
	 * @param senderParentView
	 *            ���������������
	 * @param senderPageViews
	 *            ������������
	 * @param showNavHint
	 *            ������������������������(������True������������)
	 */
	public PageViewController(Context senderContext,
			OnPageChangeListener senderCallBack, LinearLayout senderParentView,
			ArrayList<View> senderPageViews, boolean senderShowNavHint) {
		context = senderContext;
		mCallBack = senderCallBack;
		showNavHint = senderShowNavHint;
		LayoutInflater inflater = LayoutInflater.from(context);

		pageViews = new ArrayList<View>();
		if (senderPageViews != null) {
			pageViews = senderPageViews;
		}

		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.pageview, null);

		viewPager = (ViewPager) main.findViewById(R.id.guidePages);

		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		group.removeAllViews();
		if (showNavHint) {
			for (int i = 0; i < pageViews.size(); i++) {
				imageView = new ImageView(context);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageView.setPadding(20, 0, 20, 0);
				imageViews[i] = imageView;
				if (i == 0) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
				group.addView(imageViews[i]);
			}
		}

		senderParentView.addView(main);
		pageAdapter = new GuidePageAdapter();
		viewPager.setAdapter(pageAdapter);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	public ArrayList<View> getPageViews() {
		return pageViews;
	}

	/**
	 * ������������������������
	 * 
	 * @param senderPageViews
	 *            ������������
	 */
	public void setPageViews(ArrayList<View> senderPageViews) {
		this.pageViews = new ArrayList<View>();
		if (senderPageViews != null) {
			this.pageViews = senderPageViews;
		}
		if (showNavHint) {
			group.removeAllViews();
			imageViews = new ImageView[pageViews.size()];
			for (int i = 0; i < pageViews.size(); i++) {
				imageView = new ImageView(context);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageView.setPadding(20, 0, 20, 0);
				imageViews[i] = imageView;
//				if (i == viewPager.getCurrentItem()) {
//					imageViews[i]
//							.setBackgroundResource(R.drawable.page_indicator_focused);
//				} else {
//					imageViews[i]
//							.setBackgroundResource(R.drawable.page_indicator);
//				}
//				group.addView(imageViews[i]);
			}
		}
		viewPager.getAdapter().notifyDataSetChanged();

	}

	/**
	 * ���������������������
	 * 
	 * @param senderItem
	 *            ���������
	 */
	public void SetPageViewPosition(int senderItem) {
		viewPager.setCurrentItem(senderItem);
	}

	// ���������������������������
	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE; // super.getItemPosition(object); 
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			//Log.i(">>>",">>> " + pageViews.size() + " arg1:" + arg1);
			if (arg2 != null) {
				((ViewPager) arg0).removeView((View)arg2);
			}
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// ���������������������������������
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			if (mCallBack != null) {
				mCallBack.onPageScrollStateChanged(arg0);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			if (mCallBack != null) {
				mCallBack.onPageScrolled(arg0, arg1, arg2);
			}
		}

		@Override
		public void onPageSelected(int arg0) {
//			if (showNavHint) {
//				for (int i = 0; i < imageViews.length; i++) {
//					imageViews[arg0]
//							.setBackgroundResource(R.drawable.page_indicator_focused);
//
//					if (arg0 != i) {
//						imageViews[i]
//								.setBackgroundResource(R.drawable.page_indicator);
//					}
//				}
//			}
//			if (mCallBack != null) {
//				mCallBack.onPageSelected(arg0);
//			}
		}
	}
}