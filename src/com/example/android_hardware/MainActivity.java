package com.example.android_hardware;

import com.example.android_hardware.sensor.SensorActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button videoButton, cameraButton, audioRecordButton,
			brightnessButton, dialButton, flasgLightButton, musicButton,
			vibrateButton, sensorButton, locationButton, ledButton,
			headsetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		videoButton = (Button) this.findViewById(R.id.video_button);
		cameraButton = (Button) this.findViewById(R.id.camera_button);
		audioRecordButton = (Button) this.findViewById(R.id.audiorecord_button);
		brightnessButton = (Button) this.findViewById(R.id.brightness_button);
		dialButton = (Button) this.findViewById(R.id.dial_button);
		flasgLightButton = (Button) this.findViewById(R.id.flashlight_button);
		musicButton = (Button) this.findViewById(R.id.music_button);
		vibrateButton = (Button) this.findViewById(R.id.vibrate_button);
		sensorButton = (Button) this.findViewById(R.id.sensor_button);
		locationButton = (Button) this.findViewById(R.id.location_button);
		ledButton = (Button) this.findViewById(R.id.led_button);
		headsetButton = (Button) this.findViewById(R.id.headset_button);
		setTitle("主界面");

		videoButton.setOnClickListener(this);
		cameraButton.setOnClickListener(this);
		audioRecordButton.setOnClickListener(this);
		brightnessButton.setOnClickListener(this);
		dialButton.setOnClickListener(this);
		flasgLightButton.setOnClickListener(this);
		musicButton.setOnClickListener(this);
		vibrateButton.setOnClickListener(this);
		sensorButton.setOnClickListener(this);
		locationButton.setOnClickListener(this);
		ledButton.setOnClickListener(this);
		headsetButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_button:
			startActivity(new Intent(MainActivity.this, VideoActivity.class));
			break;
		case R.id.camera_button:
			startActivity(new Intent(MainActivity.this, CameraActivity.class));
			break;
		case R.id.audiorecord_button:
			startActivity(new Intent(MainActivity.this,
					AudioRecordActivity.class));
			break;
		case R.id.flashlight_button:
			startActivity(new Intent(MainActivity.this,
					FlashLightActivity.class));
			break;
		case R.id.dial_button:
			startActivity(new Intent(MainActivity.this, DialActivity.class));
			break;
		case R.id.brightness_button:
			startActivity(new Intent(MainActivity.this,
					BrightnessActivity.class));
			break;
		case R.id.music_button:
			startActivity(new Intent(MainActivity.this, MusicActivity.class));
			break;
		case R.id.vibrate_button:
			startActivity(new Intent(MainActivity.this, VibrateActivity.class));
			break;
		case R.id.sensor_button:
			startActivity(new Intent(MainActivity.this, SensorActivity.class));
			break;
		case R.id.location_button:
			startActivity(new Intent(MainActivity.this,
					GPSLocationActivity.class));
			break;
		case R.id.led_button:
			startActivity(new Intent(MainActivity.this, LEDLampActivity.class));
			break;
		case R.id.headset_button:
			startActivity(new Intent(MainActivity.this, HeadSetActivity.class));
			break;
		default:
			break;
		}
	}
}
