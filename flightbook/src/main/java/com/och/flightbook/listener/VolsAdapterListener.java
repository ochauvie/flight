package com.och.flightbook.listener;

import com.och.flightbook.model.Vol;

public interface VolsAdapterListener {

    void onClickDate(Vol item, int position);

    void onClickName(Vol item, int position);

	void onClickVol(Vol item, int position);
	
	void onClickDelete(Vol item, int position);

}
