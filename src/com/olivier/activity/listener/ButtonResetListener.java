package com.olivier.activity.listener;

import com.olivier.R;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.TextView;

public class ButtonResetListener implements OnClickListener {

	
	private Activity activity;
	
	
	public ButtonResetListener(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button2) {
			TextView textView1 = (TextView) activity.findViewById(R.id.textView1);
			Chronometer chronometer1 = (Chronometer) activity.findViewById(R.id.chronometer1);
			chronometer1.setBase(SystemClock.elapsedRealtime());
			textView1.setText("Reset !");
		}

    }
}
