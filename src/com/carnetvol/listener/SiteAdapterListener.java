package com.carnetvol.listener;

import com.carnetvol.model.Site;

public interface SiteAdapterListener {
	
	public void onClickName(Site item, int position);

	public void onClickNameToDelete(Site item, int position);

    public void onClickToUpdate(Site item, int position);

}
