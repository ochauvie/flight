package com.olivier.listener;


import com.olivier.model.ChecklistItem;

public interface UpdateChecklistAdapterListener {
	
	public void onClickToDelete(ChecklistItem item, int position);

}
