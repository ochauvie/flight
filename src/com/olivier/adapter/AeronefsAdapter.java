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
import android.widget.LinearLayout;
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
	
	private void sendListener(Aeronef item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
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
		LinearLayout layoutItem;
		  //(1) : R�utilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item � partir du  layout XML "personne_layout.xml"
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.activity_item_hangar, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
		  
		  //(2) : R�cup�ration des TextView de notre layout      
		  TextView tv_type = (TextView)layoutItem.findViewById(R.id.type);
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.name);
		        
		  //(3) : Renseignement des valeurs       
		  tv_type.setText("(" + aeronefs.get(position).getType() + ")");
		  tv_name.setText(aeronefs.get(position).getName());
		  
		  //(4) Changement de la couleur du fond de notre item
		  String type = aeronefs.get(position).getType();
		  tv_name.setTextColor(Aeronef.getColor(type));
		  
		//On m�morise la position de la "Personne" dans le composant textview
		  tv_name.setTag(position);
		  //On ajoute un listener
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on r�cup�re la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListener(aeronefs.get(position), position);
				}
			        	
			});
		  
		  //On retourne l'item cr��.
		  return layoutItem;
	}

}
