package com.carnetvol.adapter;

import java.util.ArrayList;
import java.util.List;

import com.carnetvol.R;
import com.carnetvol.listener.AeronefAdapterListener;
import com.carnetvol.model.Aeronef;

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

    private boolean isLanscape = false;

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
		  //(1) : Reutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item a partir du  layout XML "personne_layout.xml"
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_hangar, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Recuperation des TextView de notre layout
		  TextView tv_type = (TextView)layoutItem.findViewById(R.id.type);
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.name);
          TextView tv_envergure = (TextView)layoutItem.findViewById(R.id.envergure);
          TextView tv_poids = (TextView)layoutItem.findViewById(R.id.poids);

          if (tv_envergure!=null && tv_poids!=null) {
              isLanscape = true;
          }
          ImageButton bUpdate = (ImageButton)layoutItem.findViewById(R.id.updateAeronef);
		  ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteAeronef);
		        
		  //(3) : Renseignement des valeurs       
		  //tv_type.setText("(" + aeronefs.get(position).getType() + ")");
          tv_type.setText(aeronefs.get(position).getType());
		  tv_name.setText(aeronefs.get(position).getName());
          if (isLanscape) {
              tv_envergure.setText(String.valueOf(aeronefs.get(position).getWingSpan()) + " m");
              tv_poids.setText(String.valueOf(aeronefs.get(position).getWeight()) + " kg");
          }

		  
		  //(4) Changement de la couleur du fond de notre item
		  String type = aeronefs.get(position).getType();
		  tv_name.setTextColor(Aeronef.getColor(type));
		  
		  // On memorise la position de l'aeronef dans le composant textview
		  tv_name.setTag(position);
		  tv_type.setTag(position);
          bUpdate.setTag(position);
		  bDelete.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(aeronefs.get(position), position);
				}
			        	
			});
		  

		  bUpdate.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le type, on recupere la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_type".
					sendListenerType(aeronefs.get(position), position);
				}
			        	
			});

		  
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(aeronefs.get(position), position);
				}
			        	
			});
		  
		  
		  //On retourne l'item cree.
		  return layoutItem;
	}

}
