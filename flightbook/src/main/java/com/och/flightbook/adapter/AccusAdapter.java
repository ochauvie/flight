package com.och.flightbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.och.flightbook.R;
import com.och.flightbook.listener.AccuAdapterListener;
import com.och.flightbook.model.Accu;

import java.util.ArrayList;
import java.util.List;

public class AccusAdapter extends BaseAdapter{

	private List<Accu> accus;
	private LayoutInflater mInflater;
	private ArrayList<AccuAdapterListener> mListListener = new ArrayList<>();

	public AccusAdapter(Context context, List<Accu> accusList) {
		  accus = accusList;
		  mInflater = LayoutInflater.from(context);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(AccuAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerName(Accu item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickName(item, position);
	    }
	}

    private void sendListenerType(Accu item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickType(item, position);
        }
    }

    private void sendListenerToUpdate(Accu item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickToUpdate(item, position);
        }
    }

	private void sendListenerToDelete(Accu item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToDelete(item, position);
	    }
	}

    private void sendListenerNbElements(Accu item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickNbElements(item, position);
        }
    }
	
	@Override
	public int getCount() {
		if (accus!=null) {
			return accus.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (accus!=null) {
			return accus.get(position);
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
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_accu, parent, false);
		} else {
		    layoutItem = (RelativeLayout) convertView;
		}

        Accu currentAccu = accus.get(position);

		//(2) : Recuperation des TextView de notre layout
		TextView tv_name = (TextView)layoutItem.findViewById(R.id.nom);
        TextView tv_type = (TextView)layoutItem.findViewById(R.id.type);
        TextView tv_nbElements = (TextView)layoutItem.findViewById(R.id.nbElements);
        TextView tv_capacite = (TextView)layoutItem.findViewById(R.id.capacite);
        TextView tv_tauxDecharge = (TextView)layoutItem.findViewById(R.id.tauxDecharge);
        TextView tv_marque = (TextView)layoutItem.findViewById(R.id.marque);

		ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteAccu);
        ImageButton bUpdate = (ImageButton)layoutItem.findViewById(R.id.updateAccu);
		        
		//(3) : Renseignement des valeurs
		tv_name.setText(currentAccu.getNom());
        tv_type.setText(currentAccu.getType().name());
        tv_type.setTextColor(Accu.getColor(currentAccu.getType().name()));
        tv_nbElements.setText(String.valueOf(currentAccu.getNbElements()));

        // Affichage des colonnes suivants l'orientation de la page
        if (tv_capacite!=null) {
            tv_capacite.setText(String.valueOf(currentAccu.getCapacite()));
            tv_tauxDecharge.setText(String.valueOf(currentAccu.getTauxDecharge()));
            tv_marque.setText(currentAccu.getMarque());
        }


		// On memorise la position  dans le composant textview
		tv_name.setTag(position);
        tv_type.setTag(position);
        tv_nbElements.setTag(position);
		bDelete.setTag(position);
        bUpdate.setTag(position);
		  
		  // On ajoute un listener sur name
		  tv_name.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position de l'accu"
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView
					sendListenerName(accus.get(position), position);
				}
			        	
			});


        tv_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le type, on recupere la position de l'accu"
                Integer position = (Integer)v.getTag();

                //On previent les listeners qu'il y a eu un clic sur le TextView
                sendListenerType(accus.get(position), position);
            }

        });

        tv_nbElements.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le nb d'élément, on recupere la position de l'accu"
                Integer position = (Integer)v.getTag();

                //On previent les listeners qu'il y a eu un clic sur le TextView
                sendListenerNbElements(accus.get(position), position);
            }

        });

		  bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Lorsque l'on clique sur le nom, on recupere la position
					Integer position = (Integer)v.getTag();
							
					//On previent les listeners qu'il y a eu un clic sur le TextView
					sendListenerToDelete(accus.get(position), position);
				}
			        	
			});

        // Update
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le nom, on recupere la position
                Integer position = (Integer)v.getTag();

                //On previent les listeners qu'il y a eu un clic sur le TextView
                sendListenerToUpdate(accus.get(position), position);
            }

        });


        //On retourne l'item cree.
		  return layoutItem;
	}

}

