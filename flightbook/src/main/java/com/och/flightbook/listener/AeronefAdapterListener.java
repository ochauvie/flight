package com.och.flightbook.listener;

import com.och.flightbook.model.Aeronef;

public interface AeronefAdapterListener {
	
	void onClickName(Aeronef item, int position);
	
	void onClickType(Aeronef item, int position);

    void onClickUpdate(Aeronef item, int position);
	
	void onClickNameToDelete(Aeronef item, int position);

}
