package com.example.peripheraldemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.zsmarter.device.R;

@SuppressLint("NewApi")
public class SomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Fragment fragmentToDisplay = new MainFragment();
		FragmentManager manager = getFragmentManager();
		manager.beginTransaction().replace(R.id.container, fragmentToDisplay)
				.commit();
	}
}
