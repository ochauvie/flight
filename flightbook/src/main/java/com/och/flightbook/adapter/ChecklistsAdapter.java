package com.och.flightbook.adapter;

import java.util.ArrayList;
import java.util.List;

import com.och.flightbook.R;
import com.och.flightbook.listener.ChecklistsAdapterListener;
import com.och.flightbook.model.Checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChecklistsAdapter extends BaseAdapter{

	private List<Checklist> checklists;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<ChecklistsAdapterListener> mListListener = new ArrayList<ChecklistsAdapterListener>();
	
	public ChecklistsAdapter(Context context, List<Checklist> checklistsList) {
		  mContext = context;
		  checklists = checklistsList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(ChecklistsAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerName(Checklist item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
	    }
	}
	
	private void sendListenerToDelete(Checklist item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToDelete(item, position);
	    }
	}

	private void sendListenerToUpdate(Checklist item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToUpdate(item, position);
	    }
	}
	
	@Override
	public int getCount() {
		if (checklists!=null) {
			return checklists.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (checklists!=null) {
			return checklists.get(position);
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
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_checklist, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Recuperation des TextView de notre layout
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.checklistName);
		  ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteChecklist);
		  ImageButton bUpdate = (ImageButton)layoutItem.findViewById(R.id.updateChecklist);
		        
		  //(3) : Renseignement des valeurs       
		  tv_name.setText(checklists.get(position).getName());
		  
		  // On memorise la position
		  tv_name.setTag(position);
		  bDelete.setTag(position);
		  bUpdate.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(checklists.get(position), position);
				}
			        	
			});
		  
		  
		  // Delete
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(checklists.get(position), position);
				}
			        	
			});
		  
		  // Update
		  bUpdate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToUpdate(checklists.get(position), position);
				}
			        	
			});
		  
		  
		  //On retourne l'item cree.
		  return layoutItem;
	}

}
