package com.flightbook.listener;

import com.flightbook.model.Site;

public interface SiteAdapterListener {
	
	public void onClickName(Site item, int position);

	public void onClickNameToDelete(Site item, int position);

    public void onClickToUpdate(Site item, int position);

}
