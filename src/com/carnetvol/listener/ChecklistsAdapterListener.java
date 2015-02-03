package com.carnetvol.listener;


import com.carnetvol.model.Checklist;

public interface ChecklistsAdapterListener {
	
	public void onClickName(Checklist item, int position);
	
	public void onClickToDelete(Checklist item, int position);
	
	public void onClickToUpdate(Checklist item, int position);

}
