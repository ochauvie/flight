package com.och.flightbook.listener;


import com.och.flightbook.model.Checklist;

public interface ChecklistsAdapterListener {
	
	void onClickName(Checklist item, int position);
	
	void onClickToDelete(Checklist item, int position);
	
	void onClickToUpdate(Checklist item, int position);

}
