package com.och.flightbook.listener;

import com.och.flightbook.model.Aeronef;

public interface AeronefAdapterListener {
	
	public void onClickName(Aeronef item, int position);
	
	public void onClickType(Aeronef item, int position);

    public void onClickUpdate(Aeronef item, int position);
	
	public void onClickNameToDelete(Aeronef item, int position);

}
