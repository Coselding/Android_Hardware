package com.example.android_hardware;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MusicActivity extends Activity implements OnClickListener {

	private Button playButton, pauseButton, stopButton, resetButton;
	private MediaPlayer player;// 媒体类
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vedio);
		playButton = (Button) this.findViewById(R.id.play);
		pauseButton = (Button) this.findViewById(R.id.pause);
		stopButton = (Button) this.findViewById(R.id.stop);
		resetButton = (Button) this.findViewById(R.id.reset);
		setTitle("音乐播放");

		player = new MediaPlayer();
		path = Environment.getExternalStorageDirectory().getPath() + "/1.mp3";
		// path = Environment.getExternalStorageDirectory().getPath() +
		// "/1.mp4";

		playButton.setOnClickListener(this);
		pauseButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
	}

	private void playVedio(final int position) {// 加载数据，初始化，开始播放
		try {
			player.reset();
			player.setDataSource(path);
			player.prepare();
			player.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					player.start();
					player.seekTo(position);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {// 销毁
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
