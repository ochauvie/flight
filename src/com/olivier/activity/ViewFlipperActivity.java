package com.olivier.activity;

import com.olivier.R;
import com.olivier.anim.MyFlipperAnimation;

import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;


public class ViewFlipperActivity extends Activity implements OnTouchListener {
	
	private float downXValue;
	private MyFlipperAnimation myFlipperAnimation;
	ViewFlipper viewFlipper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_flipper);

		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		myFlipperAnimation = new MyFlipperAnimation();
		
        LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
        layMain.setOnTouchListener((OnTouchListener) this);
        
        

		
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		 // Get the action that was done on this touch event
        switch (arg1.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP: {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();            

                // going backwards: pushing stuff to the right
                if (downXValue < currentX) {
                	// Get a reference to the ViewFlipper
                	viewFlipper.setInAnimation(myFlipperAnimation.inFromRightAnimation());
                	viewFlipper.setOutAnimation(myFlipperAnimation.outToLeftAnimation());
                	viewFlipper.showNext();
                	/*
                	Intent myIntent = new Intent(v.getContext(), VolsActivity.class);
                    startActivityForResult(myIntent, 0);
                    */
                }

                // going forwards: pushing stuff to the left
                if (downXValue > currentX) {
                	viewFlipper.setInAnimation(myFlipperAnimation.inFromLeftAnimation());
                	viewFlipper.setOutAnimation(myFlipperAnimation.outToRightAnimation());
                	viewFlipper.showPrevious();  
                	/*
                	Intent myIntent = new Intent(v.getContext(), MeteoActivity.class);
                    startActivityForResult(myIntent, 0);
                    */
                }
                break;
            }
        }

        // if you return false, these actions will not be recorded
        return true;
	}
}