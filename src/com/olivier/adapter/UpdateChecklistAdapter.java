package com.olivier.adapter;

import java.util.ArrayList;
import java.util.List;

import com.olivier.R;
import com.olivier.listener.UpdateChecklistAdapterListener;
import com.olivier.model.ChecklistItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class UpdateChecklistAdapter extends BaseAdapter {

	private List<ChecklistItem> items;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<UpdateChecklistAdapterListener> mListListener = new ArrayList<UpdateChecklistAdapterListener>();
	
	public UpdateChecklistAdapter(Context context, List<ChecklistItem> itemsList) {
		  mContext = context;
		  items = itemsList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	
	public void addListener(UpdateChecklistAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerToDelete(ChecklistItem item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToDelete(item, position);
	    }
	}
	
	@Override
	public int getCount() {
		if (items!=null) {
			return items.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (items!=null) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// TODO
		// TODO  http://vikaskanani.wordpress.com/2011/07/27/android-focusable-edittext-inside-listview/
		// TODO
		
		RelativeLayout layoutItem;
		  //(1) : Réutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item à partir du  layout XML 
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_checklist_detail_update, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Récupération des TextView de notre layout      
		  EditText order = (EditText)layoutItem.findViewById(R.id.itemOrder);
		  EditText action = (EditText)layoutItem.findViewById(R.id.itemAction);
		  ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.itemDelete);
		  if (position==0) {
			  bDelete.setVisibility(View.GONE);
		  }
		        
		  //(3) : Renseignement des valeurs
		  ChecklistItem item = items.get(position);
		  action.setText(item.getAction());
		  order.setText(String.valueOf(item.getOrder()));
		  
		  // On mémorise la position de l'aeronef dans le composant textview
		  order.setTag(position);
		  action.setTag(position);
		  bDelete.setTag(position);
		  
		  // On ajoute un listener sur name
		  bDelete.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on récupère la position
					Integer position = (Integer)v.getTag();
							
					//On prévient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(items.get(position), position);
				
				}
			        	
			});
		  
		  //On retourne l'item créé.
		  return layoutItem;
	}

}
