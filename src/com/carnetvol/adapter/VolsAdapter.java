package com.carnetvol.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.carnetvol.R;
import com.carnetvol.listener.VolsAdapterListener;
import com.carnetvol.model.Aeronef;
import com.carnetvol.model.Vol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VolsAdapter extends BaseAdapter {

	private List<Vol> vols;
	private Context mContext;
	private LayoutInflater mInflater;
	private TextView tv_date, tv_name, tv_vol, tv_moteur;
	private ImageButton bDelete;
	
	//Contient la liste des listeners
	private ArrayList<VolsAdapterListener> mListListener = new ArrayList<VolsAdapterListener>();
	
	public VolsAdapter(Context context, List<Vol> volsList) {
		  mContext = context;
		  vols = volsList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(VolsAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerToNote(Vol item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickVol(item, position);
	    }
	}
	
	private void sendListenerToDelete(Vol item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickDelete(item, position);
	    }
	}

    private void sendListenerToFilterByMachine(Vol item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickName(item, position);
        }
    }

    private void sendListenerToFilterByDate(Vol item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickDate(item, position);
        }
    }


    @Override
	public int getCount() {
		if (vols!=null) {
			return vols.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (vols!=null) {
			return vols.get(position);
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
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_vols, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Recuperation des TextView de notre layout
		  tv_date = (TextView)layoutItem.findViewById(R.id.date);
		  tv_name = (TextView)layoutItem.findViewById(R.id.name);
		  tv_vol = (TextView)layoutItem.findViewById(R.id.tempsVol);
		  tv_moteur = (TextView)layoutItem.findViewById(R.id.tempsMoteur);
		  bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteVol);
		        
		  //(3) : Renseignement des valeurs
		  Vol flight = vols.get(position);
		  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
		  String sDate = sdf.format(flight.getDateVol());
		  tv_date.setText(sDate);
		  tv_name.setText(flight.getAeronef());
		  tv_vol.setText(String.valueOf(flight.getMinutesVol()));
		  String sSecondsMoteur = String.valueOf(flight.getSecondsMoteur());
		  if (flight.getSecondsMoteur()<10) {
			  sSecondsMoteur = "0" + sSecondsMoteur;
		  }
		  tv_moteur.setText(String.valueOf(flight.getMinutesMoteur()) + ":" + sSecondsMoteur);
		  
		  //(4) Changement de la couleur du fond de notre item
		  tv_name.setTextColor(Aeronef.getColor(flight.getType()));

		  
		//On memorise la position du vol dans le composant textview
          tv_date.setTag(position);
          tv_name.setTag(position);
          tv_vol.setTag(position);
		  bDelete.setTag(position);
		  
		  //On ajoute un listener
           tv_vol.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le temps de vol, on recupere la position
					Integer position = (Integer)v.getTag();
					
					//On previent les listeners qu'il y a eu un clic
					sendListenerToNote(vols.get(position), position);
				}
			});
		  
		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de Aeronef"
					Integer position = (Integer)v.getTag();
							
					//On pr�vient les listeners qu'il y a eu un clic sur le bouton
					sendListenerToDelete(vols.get(position), position);
				}
			        	
			});

          tv_name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   //Lorsque l'on clique sur nom, on recupere la position
                   Integer position = (Integer)v.getTag();

                   //On previent les listeners qu'il y a eu un clic
                   sendListenerToFilterByMachine(vols.get(position), position);
                }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur nom, on recupere la position
                Integer position = (Integer)v.getTag();

                //On previent les listeners qu'il y a eu un clic
                sendListenerToFilterByDate(vols.get(position), position);
            }
        });
		  
		  
		  //On retourne l'item cree.
		  return layoutItem;
	}

}