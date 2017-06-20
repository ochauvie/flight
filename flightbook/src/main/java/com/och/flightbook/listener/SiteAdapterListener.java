package com.och.flightbook.listener;

import com.och.flightbook.model.Site;

public interface SiteAdapterListener {
	
	void onClickName(Site item, int position);

	void onClickNameToDelete(Site item, int position);

    void onClickToUpdate(Site item, int position);

}
