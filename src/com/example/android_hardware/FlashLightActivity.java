package com.example.android_hardware;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FlashLightActivity extends Activity {

	private static final int TIME = 200;

	private Camera mCamera;
	private Parameters parameters;// Camera参数
	private long mExitTime = 0;
	private ToggleButton mTbtnLight, mTbtnFlashlight;
	public static final int OPEN_LIGHT = 0x0010;// 开启闪光灯
	public static final int CLOSE_LIGHT = 0x0020;// 关闭闪光灯
	private FlightThread flightThread;// 闪光计时线程
	public Handler mHandler = new Handler() {// UI线程中，负责接收Message控制闪光灯开关
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OPEN_LIGHT:
				openLight();
				break;
			case CLOSE_LIGHT:
				closeLight();
				break;
			case 1000:

				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashlight);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置屏幕常亮
		setTitle("闪光灯");

		initValue();// 初始化
		flightThread = new FlightThread();
		flightThread.start();// 开启后台计时线程
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initValue() {
		mCamera = Camera.open();// 得到照相机对象
		mTbtnLight = (ToggleButton) findViewById(R.id.button1);
		mTbtnFlashlight = (ToggleButton) findViewById(R.id.button2);
		parameters = mCamera.getParameters();

		mTbtnLight.setSelected(false);
		mTbtnFlashlight.setSelected(false);
		mTbtnLight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ToggleButton tButton = (ToggleButton) v;
				if (tButton.isChecked()) {// 闪光灯开关
					openLight();
				} else {
					closeLight();
				}
			}
		});
		mTbtnFlashlight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				ToggleButton tButton = (ToggleButton) v;
				if (tButton.isChecked()) {// 设置true，让内层计时循环开始
					// 开始循环
					isStart = true;
				} else {
					isStart = false;
					closeLight();
				}
			}
		});
	}

	/**
	 * 打开手电
	 */
	private void openLight() {
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 闪光灯模式
		mCamera.setParameters(parameters);
		mCamera.startPreview();// 摄像头初始化
	}

	/**
	 * 关闭手电
	 */
	private void closeLight() {
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭闪光灯参数
		mCamera.setParameters(parameters);
	}

	boolean isStart = false;
	int i = 0;

	class FlightThread extends Thread {

		@Override
		public void run() {
			while (true) {// 外层死循环
				while (isStart) {// 内层计时死循环
					if (i++ % 2 == 0) {// 控制闪光灯开关
						mHandler.obtainMessage(OPEN_LIGHT).sendToTarget();
					} else {
						mHandler.obtainMessage(CLOSE_LIGHT).sendToTarget();
					}

					try {
						Thread.sleep(TIME);// 倒计时数字
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 按两次返回键退出
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// 这里穿插一个很简单的双击退出的功能，有点意思
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		mCamera.release();
		flightThread.interrupt();
		super.onDestroy();
	}
}
