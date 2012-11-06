package com.olivier.adapter;

import java.util.ArrayList;
import java.util.List;

import com.olivier.R;
import com.olivier.listener.AeronefAdapterListener;
import com.olivier.model.Aeronef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AeronefsAdapter extends BaseAdapter{

	private List<Aeronef> aeronefs;
	private Context mContext;
	private LayoutInflater mInflater;
	//Contient la liste des listeners
	private ArrayList<AeronefAdapterListener> mListListener = new ArrayList<AeronefAdapterListener>();
	
	public AeronefsAdapter(Context context, List<Aeronef> aeronefsList) {
		  mContext = context;
		  aeronefs = aeronefsList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(AeronefAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerName(Aeronef item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
	    }
	}
	
	private void sendListenerType(Aeronef item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickType(item, position);
	    }
	}
	
	
	private void sendListenerToDelete(Aeronef item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickNameToDelete(item, position);
	    }
	}
	
	@Override
	public int getCount() {
		if (aeronefs!=null) {
			return aeronefs.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (aeronefs!=null) {
			return aeronefs.get(position);
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
		  //(1) : Réutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item à partir du  layout XML "personne_layout.xml"
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_hangar, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Récupération des TextView de notre layout      
		  TextView tv_type = (TextView)layoutItem.findViewById(R.id.type);
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.name);
		  ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteAeronef);
		        
		  //(3) : Renseignement des valeurs       
		  tv_type.setText("(" + aeronefs.get(position).getType() + ")");
		  tv_name.setText(aeronefs.get(position).getName());
		  
		  //(4) Changement de la couleur du fond de notre item
		  String type = aeronefs.get(position).getType();
		  tv_name.setTextColor(Aeronef.getColor(type));
		  
		  // On mémorise la position de l'aeronef dans le composant textview
		  tv_name.setTag(position);
		  tv_type.setTag(position);
		  bDelete.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on récupère la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On prévient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(aeronefs.get(position), position);
				}
			        	
			});
		  
		  // On ajoute un listener sur type
		  tv_type.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le type, on récupère la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On prévient les listeners qu'il y a eu un clic sur le TextView "tv_type".
					sendListenerType(aeronefs.get(position), position);
				}
			        	
			});
		  
		  
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on récupère la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On prévient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(aeronefs.get(position), position);
				}
			        	
			});
		  
		  
		  //On retourne l'item créé.
		  return layoutItem;
	}

}
