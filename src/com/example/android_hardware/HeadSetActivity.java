package com.example.android_hardware;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class HeadSetActivity extends Activity {

	private BroadcastReceiver receiver = new HeadSetPlugListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_headset);

		AudioManager audioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		boolean isHeadsetOn = audioManager.isWiredHeadsetOn();
		setTitle("-->>" + isHeadsetOn);

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.HEADSET_PLUG");
		// 或者使用Intent.ACTION_HEADSET_PLUG
		registerReceiver(receiver, filter);

	}

	private class HeadSetPlugListenner extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("state")) {
				if (intent.getIntExtra("state", 2) == 0) {
					// 拔出
					Toast.makeText(HeadSetActivity.this, "耳机拔出", 1).show();
				} else if (intent.getIntExtra("state", 2) == 1) {
					// 插入
					Toast.makeText(HeadSetActivity.this, "耳机插入", 1).show();
				}
			}
		}
	}

	public class MediaButtonReceiver extends BroadcastReceiver {
		private static final String TAG1 = "MediaButtonReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获得Action
			String intentAction = intent.getAction();
			// 获得KeyEvent对象
			KeyEvent keyEvent = (KeyEvent) intent
					.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			Log.i(TAG1, "Action ---->" + intentAction + "  KeyEvent----->"
					+ keyEvent.toString());
			if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
				// 获得按键字节码
				int keyCode = keyEvent.getKeyCode();
				// 按下 / 松开 按钮
				int keyAction = keyEvent.getAction();
				// 获得事件的时间
				long downtime = keyEvent.getEventTime();

				// 获取按键码 keyCode
				StringBuilder sb = new StringBuilder();
				// 这些都是可能的按键码 ， 打印出来用户按下的键
				if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
					sb.append("KEYCODE_MEDIA_NEXT");
				}
				// 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
				// KEYCODE_MEDIA_PLAY_PAUSE
				if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
					sb.append("KEYCODE_MEDIA_PLAY_PAUSE");
				}
				if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
					sb.append("KEYCODE_HEADSETHOOK");
				}
				if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
					sb.append("KEYCODE_MEDIA_PREVIOUS");
				}
				if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
					sb.append("KEYCODE_MEDIA_STOP");
				}
				// 输出点击的按键码
				Log.i(TAG1, sb.toString());
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
