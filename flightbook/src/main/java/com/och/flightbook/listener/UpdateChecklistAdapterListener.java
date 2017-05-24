package com.och.flightbook.listener;


import com.och.flightbook.model.ChecklistItem;

public interface UpdateChecklistAdapterListener {
	
	public void onClickToDelete(ChecklistItem item, int position);

}
