package com.carnetvol.listener;

import com.carnetvol.model.Aeronef;

public interface AeronefAdapterListener {
	
	public void onClickName(Aeronef item, int position);
	
	public void onClickType(Aeronef item, int position);
	
	public void onClickNameToDelete(Aeronef item, int position);

}
