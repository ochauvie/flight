package com.olivier.activity;

import com.olivier.R;
import com.olivier.activity.listener.ButtonResetListener;
import com.olivier.activity.listener.ButtonStartStopListener;
import com.olivier.service.GpsService;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


public class OlivierActivity extends Activity {

	TextView textView1;
	Chronometer chronometer1;
	Button button1;
	Button button2;
	Button button3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olivier);
        
        textView1 = (TextView) findViewById(R.id.textView1);
        
        chronometer1 = (Chronometer) findViewById(R.id.chronometer1);
        chronometer1.setBase(SystemClock.elapsedRealtime());
        chronometer1.start();
        
        // Start - Stop
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new ButtonStartStopListener(this));
        
        
        // Reset
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new ButtonResetListener(this));
        
        // Start service
        button3 = (Button) findViewById(R.id.button3);
        button3.setEnabled(false);
        button3.setOnClickListener( new View.OnClickListener() {
        	public void onClick(View actuelView) {
        		startService(new Intent(OlivierActivity.this, GpsService.class));
        	}
        });
        
        
        
    }

  
    
    
}
