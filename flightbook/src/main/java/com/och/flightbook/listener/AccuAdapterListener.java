package com.och.flightbook.listener;

import com.och.flightbook.model.Accu;

public interface AccuAdapterListener {
	
	void onClickName(Accu item, int position);

    void onClickType(Accu item, int position);

    void onClickNbElements(Accu item, int position);

	void onClickToDelete(Accu item, int position);

    void onClickToUpdate(Accu item, int position);

}
