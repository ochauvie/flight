package com.och.flightbook.listener;

import com.och.flightbook.model.Vol;

public interface VolsAdapterListener {

    public void onClickDate(Vol item, int position);

    public void onClickName(Vol item, int position);

	public void onClickVol(Vol item, int position);
	
	public void onClickDelete(Vol item, int position);

}
