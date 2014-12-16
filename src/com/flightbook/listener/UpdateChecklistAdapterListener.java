package com.flightbook.listener;


import com.flightbook.model.ChecklistItem;

public interface UpdateChecklistAdapterListener {
	
	public void onClickToDelete(ChecklistItem item, int position);

}
