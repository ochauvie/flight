package com.olivier.listener;


import com.olivier.model.Checklist;

public interface ChecklistsAdapterListener {
	
	public void onClickName(Checklist item, int position);
	
	public void onClickToDelete(Checklist item, int position);
	
	public void onClickToUpdate(Checklist item, int position);

}
