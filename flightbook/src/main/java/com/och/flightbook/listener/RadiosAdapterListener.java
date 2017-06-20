package com.och.flightbook.listener;


import com.och.flightbook.model.Radio;

public interface RadiosAdapterListener {
	
	void onClickName(Radio item, int position);
	
	void onClickToDelete(Radio item, int position);

}
