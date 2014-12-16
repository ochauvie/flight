package com.flightbook.listener;


import com.flightbook.model.ChecklistItem;

public interface ChecklistAdapterListener {
	
	public void onClickItem(ChecklistItem item, int position);
	

}
