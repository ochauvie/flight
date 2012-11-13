package com.olivier.adapter;

import java.util.ArrayList;
import java.util.List;

import com.olivier.R;
import com.olivier.listener.ChecklistAdapterListener;
import com.olivier.model.ChecklistItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class ChecklistAdapter extends BaseAdapter{

	private List<ChecklistItem> items;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	public ChecklistAdapter(Context context, List<ChecklistItem> itemsList) {
		  mContext = context;
		  items = itemsList;
		  mInflater = LayoutInflater.from(mContext);
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
		RelativeLayout layoutItem;
		  //(1) : Réutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item à partir du  layout XML 
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_checklist_detail, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  //(2) : Récupération des TextView de notre layout      
		  CheckBox checkBox = (CheckBox)layoutItem.findViewById(R.id.checkBox1);
		        
		  //(3) : Renseignement des valeurs       
		  checkBox.setText(items.get(position).getAction());
		  checkBox.setTextColor(Color.RED);
		  checkBox.setChecked(false);
		  
		  // On mémorise la position de l'aeronef dans le composant textview
		  checkBox.setTag(position);
		  
		  // On ajoute un listener sur name
		  checkBox.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					if (cb.isChecked()) {
						cb.setTextColor(Color.GREEN);
					} else {
						cb.setTextColor(Color.RED);
					}
				
				}
			        	
			});
		  
		  //On retourne l'item créé.
		  return layoutItem;
	}

}
