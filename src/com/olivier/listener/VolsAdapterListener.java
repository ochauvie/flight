package com.olivier.listener;

import com.olivier.model.Vol;

public interface VolsAdapterListener {
	
	public void onClickName(Vol item, int position);
	
	public void onClickDelete(Vol item, int position);

}
