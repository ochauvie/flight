package com.carnetvol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carnetvol.R;
import com.carnetvol.listener.AccuAdapterListener;
import com.carnetvol.model.Accu;

import java.util.ArrayList;
import java.util.List;

public class AccusAdapter extends BaseAdapter{

	private List<Accu> accus;
	private Context mContext;
	private LayoutInflater mInflater;
	//Contient la liste des listeners
	private ArrayList<AccuAdapterListener> mListListener = new ArrayList<AccuAdapterListener>();

	public AccusAdapter(Context context, List<Accu> accusList) {
		  mContext = context;
		  accus = accusList;
		  mInflater = LayoutInflater.from(mContext);
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
		  
		//(2) : Recuperation des TextView de notre layout
		TextView tv_name = (TextView)layoutItem.findViewById(R.id.nom);
        TextView tv_type = (TextView)layoutItem.findViewById(R.id.type);
		ImageButton bDelete = (ImageButton)layoutItem.findViewById(R.id.deleteAccu);
        ImageButton bUpdate = (ImageButton)layoutItem.findViewById(R.id.updateAccu);
		        
		//(3) : Renseignement des valeurs
		tv_name.setText(accus.get(position).getNom());
        tv_type.setText(accus.get(position).getType().name());


		// On memorise la position  dans le composant textview
		tv_name.setTag(position);
        tv_type.setTag(position);
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
