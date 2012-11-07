package com.olivier.adapter;

import java.util.ArrayList;
import java.util.List;

import com.olivier.R;
import com.olivier.listener.RadiosAdapterListener;
import com.olivier.model.Potar;
import com.olivier.model.Radio;
import com.olivier.model.Switch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RadioAdapter extends BaseAdapter{

	private Radio radio;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	
	public RadioAdapter(Context context, Radio rad) {
		  mContext = context;
		  radio = rad;
		  mInflater = LayoutInflater.from(mContext);
		}
	
		
	
	@Override
	public int getCount() {
		if (radio!=null) {
			int s = radio.getSwitchs().size() + radio.getPotars().size(); 
			return s;
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (radio!=null) {
			
			if (position<radio.getSwitchs().size()) {
				Switch sw = radio.getSwitchs().get(position);
				return sw;
			} else {
				Potar potar = radio.getPotars().get(position-radio.getSwitchs().size());
				return potar;
			}
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
		  //(1) : R�utilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item � partir du  layout XML 
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_switch_potar, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : R�cup�ration des TextView de notre layout      
		  TextView tv_name = (TextView)layoutItem.findViewById(R.id.name);
		  	tv_name.setTextColor(Color.RED);
		  TextView tv_action = (TextView)layoutItem.findViewById(R.id.action);
		  	tv_action.setTextColor(Color.GREEN);
		  TextView tv_up = (TextView)layoutItem.findViewById(R.id.up);
		  TextView tv_center = (TextView)layoutItem.findViewById(R.id.center);
		  TextView tv_down = (TextView)layoutItem.findViewById(R.id.down);
		        
		  //(3) : Renseignement des valeurs       
		  if (position<radio.getSwitchs().size()) {
			  tv_name.setText("Inter " + radio.getSwitchs().get(position).getName());
			  tv_action.setText(radio.getSwitchs().get(position).getAction());
			  tv_action.setTextColor(Color.GREEN);
			  tv_up.setText("UP: " + radio.getSwitchs().get(position).getUp());
			  tv_center.setText("CENTER: " + radio.getSwitchs().get(position).getCenter());
			  tv_down.setText("DOWN: " + radio.getSwitchs().get(position).getDown());
		  } else {
			  tv_name.setText("Potar " + radio.getPotars().get(position-radio.getSwitchs().size()).getName());
			  tv_action.setText(radio.getPotars().get(position-radio.getSwitchs().size()).getAction());
			  tv_up.setText("UP: " + radio.getPotars().get(position-radio.getSwitchs().size()).getUp());
			  tv_center.setText("CENTER: " + radio.getPotars().get(position-radio.getSwitchs().size()).getCenter());
			  tv_down.setText("DOWN: " + radio.getPotars().get(position-radio.getSwitchs().size()).getDown());
		  }
		  
		  
		  // On m�morise la position de l'aeronef dans le composant textview
		  tv_name.setTag(position);
		  
		  
		  // On ajoute un listener sur name
		  /*
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on r�cup�re la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerName(radios.get(position), position);
				}
			        	
			});
		  */
		  
		  
		  
		  //On retourne l'item cr��.
		  return layoutItem;
	}

}
