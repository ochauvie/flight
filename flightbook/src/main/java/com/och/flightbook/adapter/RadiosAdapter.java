package com.och.flightbook.adapter;

import java.util.ArrayList;
import java.util.List;

import com.och.flightbook.R;
import com.och.flightbook.listener.RadiosAdapterListener;
import com.och.flightbook.model.Radio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RadiosAdapter extends BaseAdapter{

	private List<Radio> radios;
	private LayoutInflater mInflater;
	private ArrayList<RadiosAdapterListener> mListListener = new ArrayList<>();
	
	public RadiosAdapter(Context context, List<Radio> radiosList) {
		  radios = radiosList;
		  mInflater = LayoutInflater.from(context);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(RadiosAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerName(Radio item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
	    }
	}
	
	private void sendListenerToDelete(Radio item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToDelete(item, position);
	    }
	}

	
	
	@Override
	public int getCount() {
		if (radios!=null) {
			return radios.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (radios!=null) {
			return radios.get(position);
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
		  	//Initialisation de notre item � partir du  layout XML 
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_radio, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Recuperation des TextView de notre layout
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.radioName);
		  ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteRadio);
		        
		  //(3) : Renseignement des valeurs       
		  tv_name.setText(radios.get(position).getName());
		  
		  
		  // On m�morise la position de l'aeronef dans le composant textview
		  tv_name.setTag(position);
		  bDelete.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on r�cup�re la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(radios.get(position), position);
				}
			        	
			});
		  
		  
		  
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(radios.get(position), position);
				}
			        	
			});
		  
		  
		  //On retourne l'item cr��.
		  return layoutItem;
	}

}
