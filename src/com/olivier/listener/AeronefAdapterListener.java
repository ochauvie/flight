package com.olivier.listener;

import com.olivier.model.Aeronef;

public interface AeronefAdapterListener {
	
	public void onClickName(Aeronef item, int position);
	
	public void onClickNameToDelete(Aeronef item, int position);

}
