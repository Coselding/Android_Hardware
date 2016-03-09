package com.example.android_hardware;

import org.apache.http.client.RedirectException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LEDLampActivity extends Activity implements OnClickListener {

	private static final int ID_LED = 19871103;
	private Notification notification;
	private NotificationManager nm;

	private Button redbButton, greenButton, bluebButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ledlamp);
		redbButton = (Button) this.findViewById(R.id.led_red);
		greenButton = (Button) this.findViewById(R.id.led_green);
		bluebButton = (Button) this.findViewById(R.id.led_blue);

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.ledARGB = 0x0000FF; // 这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
		notification.ledOnMS = 100;
		notification.ledOffMS = 100;
		notification.flags = Notification.FLAG_SHOW_LIGHTS;
		nm.notify(ID_LED, notification);

		redbButton.setOnClickListener(this);
		greenButton.setOnClickListener(this);
		bluebButton.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (notification != null)
			nm.cancel(ID_LED);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.led_red:
			notification.ledARGB = 0xFF0000;
			nm.notify(ID_LED, notification);
			break;
		case R.id.led_green:
			notification.ledARGB = 0x00FF00;
			nm.notify(ID_LED, notification);
			break;
		case R.id.led_blue:
			notification.ledARGB = 0x0000FF;
			nm.notify(ID_LED, notification);
			break;
		default:
			break;
		}
	}
}
