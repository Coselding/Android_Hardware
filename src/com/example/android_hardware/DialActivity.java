package com.example.android_hardware;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialActivity extends Activity {

	private EditText editText;
	private Button button;
	private String number;

	private EditText editText2, editText3;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial);
		button = (Button) this.findViewById(R.id.button);
		editText = (EditText) this.findViewById(R.id.number);

		button2 = (Button) this.findViewById(R.id.button1);
		editText2 = (EditText) this.findViewById(R.id.sms_num);
		editText3 = (EditText) this.findViewById(R.id.sms_content);
		setTitle("电话短信");

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number = editText.getText().toString();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);// 启动拨号界面
				intent.setData(Uri.parse("tel:" + number));// 拨号界面需要传入拨号号码
				startActivity(intent);
			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number = editText2.getText().toString();
				String content = editText3.getText().toString();
				SmsManager manager = SmsManager.getDefault();
				ArrayList<String> texts = manager.divideMessage(content);// 短信过长自动划分为多条短信
				for (String text : texts) {// 发出所有短信
					manager.sendTextMessage(number, null, text, null, null);
				}
				Toast.makeText(DialActivity.this, R.string.sendsuccess,
						Toast.LENGTH_SHORT).show();
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
