package com.och.flightbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.och.flightbook.R;
import com.och.flightbook.listener.SiteAdapterListener;
import com.och.flightbook.model.Site;

import java.util.ArrayList;
import java.util.List;

public class SitesAdapter extends BaseAdapter{

	private List<Site> sites;
	private Context mContext;
	private LayoutInflater mInflater;
	//Contient la liste des listeners
	private ArrayList<SiteAdapterListener> mListListener = new ArrayList<SiteAdapterListener>();

	public SitesAdapter(Context context, List<Site> sitesList) {
		  mContext = context;
		  sites = sitesList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(SiteAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerName(Site item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
	    }
	}

    private void sendListenerToUpdate(Site item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickToUpdate(item, position);
        }
    }

	private void sendListenerToDelete(Site item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickNameToDelete(item, position);
	    }
	}
	
	@Override
	public int getCount() {
		if (sites!=null) {
			return sites.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (sites!=null) {
			return sites.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout layoutItem;
		//(1) : Reutilisation des layouts
		if (convertView == null) {
			//Initialisation de notre item a partir du  layout XML
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_site, parent, false);
		} else {
		    layoutItem = (RelativeLayout) convertView;
		}
		  
		//(2) : Recuperation des TextView de notre layout
		TextView tv_name = (TextView)layoutItem.findViewById(R.id.name);
		ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteSite);
        ImageButton bUpdate = (ImageButton)layoutItem.findViewById(R.id.updateSite);


		//(3) : Renseignement des valeurs
		tv_name.setText(sites.get(position).getName());

        // Site par défault en rouge
        if (1==sites.get(position).getIsDefault()) {
            tv_name.setTextColor(Color.RED);
        }

		// On memorise la position  dans le composant textview
		tv_name.setTag(position);
		bDelete.setTag(position);
        bUpdate.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Site"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(sites.get(position), position);
				}
			        	
			});
		  

		  
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Site"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(sites.get(position), position);
				}
			        	
			});

        // Update
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le nom, on recupere la position
                Integer position = (Integer)v.getTag();

                //On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
                sendListenerToUpdate(sites.get(position), position);
            }

        });


        //On retourne l'item cree.
		  return layoutItem;
	}

}
