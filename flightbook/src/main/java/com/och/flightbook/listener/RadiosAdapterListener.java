package com.och.flightbook.listener;


import com.och.flightbook.model.Radio;

public interface RadiosAdapterListener {
	
	public void onClickName(Radio item, int position);
	
	public void onClickToDelete(Radio item, int position);

}
