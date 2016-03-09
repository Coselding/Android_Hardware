package com.example.android_hardware;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoActivity extends Activity implements OnClickListener {

	private MediaPlayer player;// 播放类
	private SurfaceView surfaceView;// 视频显示控件
	private String path;// 视频路径
	private Button play, pause, stop, reset;
	private int position;// 记录视频播放位置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vedio);
		play = (Button) this.findViewById(R.id.play);
		pause = (Button) this.findViewById(R.id.pause);
		stop = (Button) this.findViewById(R.id.stop);
		reset = (Button) this.findViewById(R.id.reset);
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
		setTitle("视频播放");

		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 将视频文件流得到的数据直接推送到SurfaceView中，不维护缓存
		surfaceView.getHolder().setFixedSize(320, 240);// 设置视频匹配宽高
		surfaceView.getHolder().setKeepScreenOn(true);// 设置屏幕常亮
		// 当SurfaceView所在的Activity离开前台时，SurfaceView会被摧毁，在OnResume方法之后，Surface会被重新创建
		surfaceView.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {// SurfaceView销毁
				if (player.isPlaying()) {
					position = player.getCurrentPosition();
					player.stop();
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {// SurfaceView创建
				if (position > 0 && path != null) {
					playVedio(position);
					position = 0;
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}
		});

		player = new MediaPlayer();
		path = Environment.getExternalStorageDirectory().getPath() + "/1.mp4";
		// path = Environment.getExternalStorageDirectory().getPath() +
		// "/1.mp4";

		play.setOnClickListener(this);
		pause.setOnClickListener(this);
		stop.setOnClickListener(this);
		reset.setOnClickListener(this);
	}

	private void playVedio(final int position) {
		try {
			player.reset();
			player.setDataSource(path);// 设置视频文件路径
			player.setDisplay(surfaceView.getHolder());// 播放器将视频媒体推送到SurfaceView
			player.prepare();
			player.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {// 数据初始化完毕，开始播放
					player.start();
					player.seekTo(position);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {// 销毁媒体类
		player.release();
		player = null;
		super.onDestroy();
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

	private boolean pause_;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			playVedio(0);
			break;
		case R.id.pause:
			if (player.isPlaying()) {
				pause_ = true;
				player.pause();
			} else {
				if (pause_)
					player.start();
			}
			break;
		case R.id.stop:
			if (player.isPlaying())
				player.stop();
			break;
		case R.id.reset:
			playVedio(0);
			break;

		default:
			break;
		}
	}
}
