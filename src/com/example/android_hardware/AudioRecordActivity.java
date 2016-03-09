package com.example.android_hardware;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AudioRecordActivity extends Activity {

	private Button recordButton, playButton;
	private MediaRecorder mRecorder;// 录音机对象
	private MediaPlayer mediaPlayer;// 播音器
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiorecord);
		recordButton = (Button) this.findViewById(R.id.record);
		playButton = (Button) this.findViewById(R.id.play);
		setTitle("录音播音");

		flag = 0;
		mRecorder = new MediaRecorder();// 实例化
		// 设置音频参数
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置录音文件路径
		mRecorder.setOutputFile(Environment.getExternalStorageDirectory()
				.getPath() + "/1.mp3");
		FileDescriptor descriptor = new FileDescriptor();

		// mRecorder.setOutputFile(descriptor);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频编码
		recordButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag == 0) {// 开始录音
					try {
						mRecorder.prepare();
					} catch (IOException e) {
						Log.e("MainActivity", "prepare() failed");
					}
					mRecorder.start();
					recordButton.setText("结束");
					flag = 1;
				} else {// 结束录音
					flag = 0;
					mRecorder.stop();
					recordButton.setText("开始");
				}
			}
		});

		playButton.setOnClickListener(new View.OnClickListener() {// 播放录音

					@Override
					public void onClick(View v) {
						mediaPlayer = new MediaPlayer();
						mediaPlayer
								.setAudioStreamType(AudioManager.STREAM_MUSIC);
						try {
							mediaPlayer.setDataSource(getApplicationContext(),
									Uri.fromFile(new File(Environment
											.getExternalStorageDirectory()
											.getPath()
											+ "/1.mp3")));
							mediaPlayer.prepare();
						} catch (IllegalArgumentException | SecurityException
								| IllegalStateException | IOException e) {
							e.printStackTrace();
						}

						mediaPlayer.start();
						// mediaPlayer.stop();
						// mediaPlayer.release();
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
