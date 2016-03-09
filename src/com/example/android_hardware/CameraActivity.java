package com.example.android_hardware;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CameraActivity extends Activity implements OnClickListener {

	private View layout;// 快门对角按钮布局界面
	private Button focusButton, takeButton;
	private SurfaceView surfaceView;// 显示控件

	private Camera camera;// 摄像机

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// Activity无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 屏幕常亮
		setContentView(R.layout.activity_camera);
		focusButton = (Button) this.findViewById(R.id.autofocus);
		takeButton = (Button) this.findViewById(R.id.takepicture);
		surfaceView = (SurfaceView) this.findViewById(R.id.surface);
		layout = this.findViewById(R.id.button_layout);
		setTitle("照相");

		surfaceView.getHolder().setFixedSize(240, 320);// 设置SurfaceView大小
		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置SurfaceView不维护缓存，直接推送到屏幕
		surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
		surfaceView.getHolder().addCallback(new MyCallBack());// 设置SurfaceView状态回调监听

		focusButton.setOnClickListener(this);
		takeButton.setOnClickListener(this);
	}

	private final class MyCallBack implements Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();// 打开摄像头
			Camera.Parameters parameters = camera.getParameters();
			Log.i("CameraActivity", parameters.flatten());
			// 图片预览大小
			parameters.setPreviewSize(1920, 1088);
			// 图片预览刷新率
			parameters.setPreviewFrameRate(15);
			// 设置图片储存大小
			parameters.setPictureSize(4208, 3120);
			// 设置图片质量
			parameters.setJpegQuality(85);

			// 储存文件旋转90度
			parameters.set("rotation", 90);
			// 图片预览旋转90度
			camera.setDisplayOrientation(90);

			camera.setParameters(parameters);
			try {
				// 将摄像头得到的画面投射到SurfaceView中
				camera.setPreviewDisplay(surfaceView.getHolder());
				camera.startPreview();// 开始预览
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// SurfaceView销毁证明Activity不再前台，释放摄像头，防止占用
			if (camera != null) {
				camera.release();
				camera = null;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 触摸屏幕显示按钮
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			layout.setVisibility(View.VISIBLE);
			return true;
		}
		return super.onTouchEvent(event);
	}

	private final class MyPictureCallBack implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 保存照片
			File file = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".jpg");
			try {
				if (!file.exists())
					file.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(data);
				outputStream.close();
				camera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (camera != null) {
			switch (v.getId()) {
			case R.id.takepicture:
				// 传入三个回调接口
				// 第一个为快门键按下的监听回调
				// 第二个为拍照得到的照片原图，未压缩
				// 第三个为拍照得到的压缩的照片
				camera.takePicture(null, null, new MyPictureCallBack());
				break;
			case R.id.autofocus:
				// 传入自动聚焦结束的回调监听
				camera.autoFocus(null);
				break;
			default:
				break;
			}
		}
	}
}
