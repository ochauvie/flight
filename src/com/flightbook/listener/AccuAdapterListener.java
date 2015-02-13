package com.flightbook.listener;

import com.flightbook.model.Accu;

public interface AccuAdapterListener {
	
	public void onClickName(Accu item, int position);

    public void onClickType(Accu item, int position);

    public void onClickNbElements(Accu item, int position);

	public void onClickToDelete(Accu item, int position);

    public void onClickToUpdate(Accu item, int position);

}
