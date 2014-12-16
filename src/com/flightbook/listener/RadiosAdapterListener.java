package com.flightbook.listener;


import com.flightbook.model.Radio;

public interface RadiosAdapterListener {
	
	public void onClickName(Radio item, int position);
	
	public void onClickToDelete(Radio item, int position);

}
