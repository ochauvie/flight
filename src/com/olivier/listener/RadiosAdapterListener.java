package com.olivier.listener;


import com.olivier.model.Radio;

public interface RadiosAdapterListener {
	
	public void onClickName(Radio item, int position);
	
	public void onClickToDelete(Radio item, int position);

}
