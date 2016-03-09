package com.example.android_hardware;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android_hardware.util.BrightnessTools;

public class BrightnessActivity extends Activity {

	private static final String TAG = "BrightnessActivity";

	private EditText timeEditText;
	private Button button, closeButton;

	private PowerManager powerManager;
	private Activity activity = this;// 获取本身Activity对象
	private ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brightness);
		timeEditText = (EditText) this.findViewById(R.id.time);
		timeEditText.setEnabled(true);
		button = (Button) this.findViewById(R.id.button);
		closeButton = (Button) this.findViewById(R.id.close_screen);
		setTitle("屏幕操作");

		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		resolver = getBaseContext().getContentResolver();
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int brightness = Integer.valueOf(timeEditText.getText()
						.toString());
				BrightnessTools.setBrightness(activity, brightness);// 设置屏幕亮度
				// BrightnessTools.saveBrightness(resolver, 0);
			}
		});

		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// powerManager.goToSleep(100);
				// 保持屏幕常亮
				PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
						PowerManager.FULL_WAKE_LOCK
								| PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
				wakeLock.acquire();

				// 撤销屏幕常亮
				// if (wakeLock != null) {
				// wakeLock.release();}
			}
		});
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
}
