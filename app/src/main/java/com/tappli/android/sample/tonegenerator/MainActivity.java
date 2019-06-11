package com.tappli.android.sample.tonegenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ToneGenerator mToneGenerator;

	private ArrayAdapter<NameAndValue> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setUp();
	}

	/**
	 * 初期設定
	 */
	private void setUp() {
		mToneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);

		mAdapter = new ArrayAdapter<NameAndValue>(this, android.R.layout.simple_list_item_1);
		List<NameAndValue> list = getToneList();
		for (NameAndValue nv : list) {
			mAdapter.add(nv);
		}

		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NameAndValue item = mAdapter.getItem(position);
				mToneGenerator.startTone(item.value, 1000);
			}
		});
	}

	/**
	 * TONEのリストを取得
	 * 
	 * @return
	 */
	private List<NameAndValue> getToneList() {
		List<NameAndValue> list = new ArrayList<MainActivity.NameAndValue>();

		try {
			Class<?> cls = Class.forName("android.media.ToneGenerator");
			Field[] fields = cls.getFields();
			for (Field field : fields) {
				String name = null;
				name = field.getName();
				if (name == null || !name.startsWith("TONE_")) {
					continue;
				}

				Object object = field.get(null);
				if (object instanceof Integer) {
					list.add(new NameAndValue(name, (Integer) object));
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static class NameAndValue {
		String name;
		int value;

		NameAndValue(String n, int v) {
			name = n;
			value = v;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
