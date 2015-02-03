package com.carnetvol.listener;


import com.carnetvol.model.Radio;

public interface RadiosAdapterListener {
	
	public void onClickName(Radio item, int position);
	
	public void onClickToDelete(Radio item, int position);

}
