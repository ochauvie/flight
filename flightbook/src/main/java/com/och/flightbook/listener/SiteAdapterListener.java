package com.och.flightbook.listener;

import com.och.flightbook.model.Site;

public interface SiteAdapterListener {
	
	public void onClickName(Site item, int position);

	public void onClickNameToDelete(Site item, int position);

    public void onClickToUpdate(Site item, int position);

}
