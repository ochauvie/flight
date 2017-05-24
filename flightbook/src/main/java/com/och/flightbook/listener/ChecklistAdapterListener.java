package com.och.flightbook.listener;


import com.och.flightbook.model.ChecklistItem;

public interface ChecklistAdapterListener {
	
	public void onClickItem(ChecklistItem item, int position);
	

}
