package com.example.android_hardware.sensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android_hardware.R;

public class SensorActivity extends Activity implements
		android.hardware.SensorEventListener, View.OnClickListener {

	private static final String TAG = "SensorActivity";
	private SensorManager sensorManager;// 感应器管理器
	private boolean status;// 感应器状态
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		sensorManager = (SensorManager) this
				.getSystemService(Context.SENSOR_SERVICE);// 获取感应器管理器
		setTitle("传感器");

		// 初始化对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SensorActivity.this);
		builder.setTitle("感应器测试");
		builder.setMessage("init...");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sensorManager.unregisterListener(SensorActivity.this);// 关闭感应器监听
				dialog.dismiss();
			}
		}).setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					sensorManager.unregisterListener(SensorActivity.this);// 关闭感应器监听
					dialog.dismiss();
				}
				return false;
			}
		});
		dialog = builder.create();

		this.findViewById(R.id.sensor_light).setOnClickListener(this);
		this.findViewById(R.id.sensor_gravity).setOnClickListener(this);
		this.findViewById(R.id.sensor_presure).setOnClickListener(this);
		this.findViewById(R.id.sensor_gycoscope).setOnClickListener(this);
		this.findViewById(R.id.sensor_linear_acceleration).setOnClickListener(
				this);
		this.findViewById(R.id.sensor_orientation).setOnClickListener(this);
		this.findViewById(R.id.sensor_acceleration).setOnClickListener(this);
		this.findViewById(R.id.sensor_proximity).setOnClickListener(this);
		this.findViewById(R.id.sensor_ambient_temperature).setOnClickListener(
				this);
		this.findViewById(R.id.sensor_game_rotation_vector).setOnClickListener(
				this);
		this.findViewById(R.id.sensor_magnetic_field).setOnClickListener(this);
		this.findViewById(R.id.sensor_step_counter).setOnClickListener(this);
		this.findViewById(R.id.sensor_temperature).setOnClickListener(this);
		this.findViewById(R.id.sensor_rotation_vector).setOnClickListener(this);
		this.findViewById(R.id.sensor_relative_humidity).setOnClickListener(
				this);
		this.findViewById(R.id.sensor_significant_motion).setOnClickListener(
				this);
		this.findViewById(R.id.sensor_step_detector).setOnClickListener(this);
	}

	public void showAlertDialog(int type) {
		status = false;
		Log.i(TAG, "type = " + type);
		Sensor sensor = sensorManager.getDefaultSensor(type);// 得到感应器对象
		if (sensor == null) {// 手机不存在此感应器
			status = false;
			dialog.setMessage("手机中无此感应器");
		} else {// 有感应器，进行监听注册
			status = sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			Log.i(TAG, "status = " + status);
			// 注册失败，启动失败
			if (!status)
				dialog.setMessage("感应器启动失败");
		}
		dialog.show();
	}

	@Override
	protected void onStop() {// 取消感应器监听
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {// 取消感应器监听
		sensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
	}

	// 感应器接收到数据回调函数
	@Override
	public void onSensorChanged(android.hardware.SensorEvent event) {
		float[] values = event.values;// 感应器数据
		int lightType = event.sensor.getType();// 感应器类型参数

		Log.i(TAG, " type = " + lightType);
		if (status) {// 感应器启动成功
			if (lightType == Sensor.TYPE_LIGHT) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("当前光的强度为：\n" + builder);
			} else if (lightType == Sensor.TYPE_GRAVITY) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("重力感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_PRESSURE) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("压力感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_GYROSCOPE) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("陀螺仪：\n" + builder);
			} else if (lightType == Sensor.TYPE_LINEAR_ACCELERATION) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("线性加速度：\n" + builder);
			} else if (lightType == Sensor.TYPE_ACCELEROMETER) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("加速度感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_ORIENTATION) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("方向感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_PROXIMITY) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("距离感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("周围温度感应：\n" + builder);
			} else if (lightType == Sensor.TYPE_GAME_ROTATION_VECTOR) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("游戏旋转向量：\n" + builder);
			} else if (lightType == Sensor.TYPE_MAGNETIC_FIELD) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("磁极感应器：\n" + builder);
			} else if (lightType == Sensor.TYPE_STEP_COUNTER) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("记步感应器：\n" + builder);
			} else if (lightType == Sensor.TYPE_ROTATION_VECTOR) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("旋转向量：\n" + builder);
			} else if (lightType == Sensor.TYPE_TEMPERATURE) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("温度感应器：\n" + builder);
			} else if (lightType == Sensor.TYPE_RELATIVE_HUMIDITY) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("湿度感应器：\n" + builder);
			} else if (lightType == Sensor.TYPE_SIGNIFICANT_MOTION) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("手势感应器：\n" + builder);
			} else if (lightType == Sensor.TYPE_STEP_DETECTOR) {
				int count = values.length;
				StringBuilder builder = new StringBuilder();
				builder.append("count = " + count + "\n");
				for (int i = 0; i < count; i++) {
					builder.append(i + " : " + values[i] + "\n");
				}
				dialog.setMessage("步数探测器感应器：\n" + builder);
			}
		} else
			dialog.setMessage("感应器启动失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sensor_light:// 光线感应器
			showAlertDialog(Sensor.TYPE_LIGHT);
			break;
		case R.id.sensor_gravity:// 重力感应器
			showAlertDialog(Sensor.TYPE_GRAVITY);
			break;
		case R.id.sensor_presure:// 压力感应器
			showAlertDialog(Sensor.TYPE_PRESSURE);
			break;
		case R.id.sensor_gycoscope:// 陀螺感应器
			showAlertDialog(Sensor.TYPE_GYROSCOPE);
			break;
		case R.id.sensor_linear_acceleration:// 线性加速度
			showAlertDialog(Sensor.TYPE_LINEAR_ACCELERATION);
			break;
		case R.id.sensor_orientation:// 方向感应器
			showAlertDialog(Sensor.TYPE_ORIENTATION);
			break;
		case R.id.sensor_acceleration:// 加速度感应器
			showAlertDialog(Sensor.TYPE_ACCELEROMETER);
			break;
		case R.id.sensor_proximity:// 距离感应器
			showAlertDialog(Sensor.TYPE_PROXIMITY);
			break;
		case R.id.sensor_ambient_temperature:// 周围温度感应器
			showAlertDialog(Sensor.TYPE_AMBIENT_TEMPERATURE);
			break;
		case R.id.sensor_game_rotation_vector:// 游戏旋转矢量
			showAlertDialog(Sensor.TYPE_GAME_ROTATION_VECTOR);
			break;
		case R.id.sensor_magnetic_field:// 磁极感应器
			showAlertDialog(Sensor.TYPE_MAGNETIC_FIELD);
			break;
		case R.id.sensor_step_counter:// 步数记录器
			showAlertDialog(Sensor.TYPE_STEP_COUNTER);
			break;
		case R.id.sensor_temperature:// 温度感应器
			showAlertDialog(Sensor.TYPE_TEMPERATURE);
			break;
		case R.id.sensor_rotation_vector:// 旋转矢量
			showAlertDialog(Sensor.TYPE_ROTATION_VECTOR);
			break;
		case R.id.sensor_relative_humidity:// 湿度感应器
			showAlertDialog(Sensor.TYPE_RELATIVE_HUMIDITY);
			break;
		case R.id.sensor_significant_motion:// 手势感应器
			showAlertDialog(Sensor.TYPE_SIGNIFICANT_MOTION);
			break;
		case R.id.sensor_step_detector:// 步数检测器
			showAlertDialog(Sensor.TYPE_STEP_DETECTOR);
			break;
		default:
			break;
		}
	}
}
