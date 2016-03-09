package com.example.android_hardware;

import java.util.ArrayList;
import java.util.Iterator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GPSLocationActivity extends Activity {

	private static final String TAG = "GPSLocationActivity";

	private EditText locationEditText;
	private TextView statusTextView, satelliteTextView, deviceTextView;
	private LocationManager locationManager;// 位置信息管理器

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpslocation);
		locationEditText = (EditText) findViewById(R.id.location);
		statusTextView = (TextView) findViewById(R.id.status);
		satelliteTextView = (TextView) findViewById(R.id.satellite);
		deviceTextView = (TextView) findViewById(R.id.device_status);

		setTitle("GPS位置信息");

		// 获取位置信息管理器
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 判断GPS是否正常启动
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
			// 返回开启GPS导航设置界面
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			return;
		}

		// 为获取地理位置信息时设置查询条件
		String bestProvider = locationManager.getBestProvider(getCriteria(),
				true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
		Location location = locationManager.getLastKnownLocation(bestProvider);
		updateView(location);
		getGPSStatus();
		// 监听状态
		locationManager.addGpsStatusListener(listener);
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 1秒更新一次，或最小位移变化超过1米更新一次；
		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 1, locationListener);
	}

	// 获取GPS状态
	public void getGPSStatus() {
		// GpsStatus，GPS状态信息，上面在卫星状态变化时，我们就用到了GpsStatus。
		// 实例化
		GpsStatus gpsStatus = locationManager.getGpsStatus(null); // 获取当前状态
		// 获取默认最大卫星数
		int maxSatellites = gpsStatus.getMaxSatellites();
		// 获取第一次定位时间（启动到第一次定位）
		int costTime = gpsStatus.getTimeToFirstFix();
		// 获取卫星
		Iterable<GpsSatellite> iterable = gpsStatus.getSatellites();
		// 一般再次转换成Iterator
		Iterator<GpsSatellite> itrator = iterable.iterator();

		StringBuilder builder = new StringBuilder();
		builder.append("最大卫星数：" + maxSatellites + "\n");
		builder.append("第一次定位时间：" + costTime + "\n");
		statusTextView.setText(builder);
	}

	// 获取GPS连接卫星的状态
	public void getGpsSatellite() {
		// 获取卫星
		GpsStatus gpsStatus = locationManager.getGpsStatus(null);
		Iterable<GpsSatellite> iterable = gpsStatus.getSatellites();
		// 再次转换成Iterator
		Iterator<GpsSatellite> itrator = iterable.iterator();
		// 通过遍历重新整理为ArrayList
		ArrayList<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();
		int count = 0;
		int maxSatellites = gpsStatus.getMaxSatellites();
		while (itrator.hasNext() && count < maxSatellites) {
			GpsSatellite satellite = itrator.next();
			satelliteList.add(satellite);
			count++;
		}
		System.out.println("总共搜索到" + count + "颗卫星");

		StringBuilder builder = new StringBuilder();
		builder.append("搜索卫星：" + count + "\n");
		// 输出卫星信息
		for (int i = 0; i < satelliteList.size(); i++) {
			// 卫星的方位角，浮点型数据
			System.out.println(satelliteList.get(i).getAzimuth());
			// 卫星的高度，浮点型数据
			System.out.println(satelliteList.get(i).getElevation());
			// 卫星的伪随机噪声码，整形数据
			System.out.println(satelliteList.get(i).getPrn());
			// 卫星的信噪比，浮点型数据
			System.out.println(satelliteList.get(i).getSnr());
			// 卫星是否有年历表，布尔型数据
			System.out.println(satelliteList.get(i).hasAlmanac());
			// 卫星是否有星历表，布尔型数据
			System.out.println(satelliteList.get(i).hasEphemeris());
			// 卫星是否被用于近期的GPS修正计算
			System.out.println(satelliteList.get(i).hasAlmanac());

			builder.append("卫星参数： 方位角：" + satelliteList.get(i).getAzimuth()
					+ " 高度：" + satelliteList.get(i).getElevation() + " 伪随机噪声码："
					+ satelliteList.get(i).getPrn() + " 信噪比："
					+ satelliteList.get(i).getSnr() + "\n");
		}
		satelliteTextView.setText(builder);
	}

	// 位置监听
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			updateView(location);
			Log.i(TAG, "时间：" + location.getTime());
			Log.i(TAG, "经度：" + location.getLongitude());
			Log.i(TAG, "纬度：" + location.getLatitude());
			Log.i(TAG, "海拔：" + location.getAltitude());
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "当前GPS状态为可见状态");
				deviceTextView.setText("当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "当前GPS状态为服务区外状态");
				deviceTextView.setText("当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "当前GPS状态为暂停服务状态");
				deviceTextView.setText("当前GPS状态为暂停服务状态");
				break;
			}
			getGPSStatus();
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = locationManager.getLastKnownLocation(provider);
			updateView(location);
			getGPSStatus();
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
			updateView(null);
		}
	};

	// 状态监听
	private GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			// 第一次定位
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i(TAG, "第一次定位");
				deviceTextView.setText("第一次定位");
				getGPSStatus();
				getGpsSatellite();
				break;
			// 卫星状态改变
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "卫星状态改变");
				deviceTextView.setText("卫星状态改变");
				// 获取当前状态
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				// 获取卫星颗数的默认最大值
				int maxSatellites = gpsStatus.getMaxSatellites();
				// 创建一个迭代器保存所有卫星
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("搜索到：" + count + "颗卫星");
				getGpsSatellite();
				break;
			// 定位启动
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "定位启动");
				deviceTextView.setText("定位启动");
				getGPSStatus();
				break;
			// 定位结束
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "定位结束");
				deviceTextView.setText("定位结束");
				break;
			}
		};
	};

	/**
	 * 实时更新文本内容
	 * 
	 * @param location
	 */
	private void updateView(Location location) {
		if (location != null) {
			locationEditText.setText("设备位置信息\n\n经度：");
			locationEditText.append(String.valueOf(location.getLongitude()));
			locationEditText.append("\n纬度：");
			locationEditText.append(String.valueOf(location.getLatitude()));
		} else {
			// 清空EditText对象
			locationEditText.getEditableText().clear();
		}
	}

	/**
	 * 返回查询条件
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(false);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}
}
