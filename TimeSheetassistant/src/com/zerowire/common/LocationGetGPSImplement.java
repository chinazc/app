package com.zerowire.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationGetGPSImplement {

	private MyLocationListenner mListenner = new MyLocationListenner();
	private LocationClient locationClient = null;
	private GPSLocation gpsLocation = null;
	public OnGPSReceived mGPSReceived = null;
	public interface OnGPSReceived {
		void onGPSReceived(GPSLocation senderGPSLocation);
	}
	private Dialog myDialog;

	/**
	 * private LocationGetGPSImplement mLocationGetGPSImplement = null;
	 * mLocationGetGPSImplement = new LocationGetGPSImplement(this,"?????????????????????...",new OnGPSReceived() {
					@Override
					public void onGPSReceived(GPSLocation senderGPSLocation) {
						// TODO Auto-generated method stub
						// Log.i(">>>>> ", String.valueOf(senderGPSLocation.getLongitude()));
						// Log.i(">>>>>  ",String.valueOf(senderGPSLocation.getLatitude()));
						StartProc(senderGPSLocation);
					}
				});
	 * @param context
	 */
	public LocationGetGPSImplement(Context context, String senderMSG, OnGPSReceived senderEvent) {
		mGPSReceived = senderEvent;
		
		locationClient = new LocationClient(context);
		locationClient.registerLocationListener(mListenner);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll"); 
		option.setScanSpan(10000);	//???????????????????????????1??????????????????;????????????1??????????????????
		option.setPriority(LocationClientOption.NetWorkFirst); // ?????????
		locationClient.setLocOption(option);
		locationClient.start();
		
		myDialog = ProgressDialog.show(context, null, senderMSG);
		if (locationClient.isStarted())
			locationClient.requestLocation();
		
	}

	public class GPSLocation implements java.io.Serializable {

		private static final long serialVersionUID = 0x1109;
		
		public double longitude;
		public double latitude;
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
	}
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			
			gpsLocation = null;
			if (location != null) {
				gpsLocation = new GPSLocation();
				gpsLocation.setLongitude(location.getLongitude());
				gpsLocation.setLatitude(location.getLatitude());
			}
			
			locationClient.stop();
			mGPSReceived.onGPSReceived(gpsLocation);
			myDialog.dismiss();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	
}
