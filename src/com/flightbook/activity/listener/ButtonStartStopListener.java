package com.flightbook.activity.listener;

import com.flightbook.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class ButtonStartStopListener implements OnClickListener {

	
	private Activity activity;
	
	
	public ButtonStartStopListener(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		changeEtatChronometre(v);
	}
	
	private void changeEtatChronometre(View v) {
		
		if (v.getId() == R.id.button1) {
			Button button1 = (Button) v;
			TextView textView1 = (TextView) activity.findViewById(R.id.textView1);
			Chronometer chronometer1 = (Chronometer) activity.findViewById(R.id.chronometer1);
			if ("Stop".equals(button1.getText())) {
				chronometer1.stop();
	    		textView1.setText("Stop !");
	    		button1.setText("Start");
	    	} else if ("Start".equals(button1.getText())) {
	    		chronometer1.start();
	    		textView1.setText("Start !");
	    		button1.setText("Stop");
	    	}	
		}

    }
}
