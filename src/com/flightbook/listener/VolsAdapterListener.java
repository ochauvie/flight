package com.flightbook.listener;

import com.flightbook.model.Vol;

public interface VolsAdapterListener {

    public void onClickName(Vol item, int position);

	public void onClickVol(Vol item, int position);
	
	public void onClickDelete(Vol item, int position);

}
