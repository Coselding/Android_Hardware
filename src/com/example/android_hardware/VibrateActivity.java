package com.example.android_hardware;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VibrateActivity extends Activity implements OnClickListener {

	private static final long[] pattern = { 200, 300, 400, 600 };// 分别是停，震，停，震的时间毫秒
	private Button startButton, stopButton, regularButton;

	private Vibrator vibrator;// 震动器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vibrate);
		startButton = (Button) this.findViewById(R.id.vibrate_start);
		stopButton = (Button) this.findViewById(R.id.vibrate_stop);
		regularButton = (Button) this.findViewById(R.id.vibrate_regular);
		setTitle("震动器");

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);// 得到震动器Service

		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		regularButton.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		vibrator.cancel();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vibrate_start:
			vibrator.vibrate(500);// 设置震动时间
			break;
		case R.id.vibrate_stop:
			vibrator.vibrate(3000);
			break;
		case R.id.vibrate_regular:// 设置振动模式
			// 第二个参数isRepeat，1表示重复震动，-1表示只震动一次
			vibrator.vibrate(pattern, 1);
			break;
		default:
			break;
		}
	}
}
