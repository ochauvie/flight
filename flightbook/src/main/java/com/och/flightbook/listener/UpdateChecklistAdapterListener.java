package com.och.flightbook.listener;


import com.och.flightbook.model.ChecklistItem;

public interface UpdateChecklistAdapterListener {
	
	void onClickToDelete(ChecklistItem item, int position);

}
