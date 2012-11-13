package com.olivier.listener;


import com.olivier.model.ChecklistItem;

public interface ChecklistAdapterListener {
	
	public void onClickItem(ChecklistItem item, int position);
	

}
