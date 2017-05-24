package com.och.flightbook.adapter;

import java.util.List;

import com.och.flightbook.R;
import com.och.flightbook.model.ChecklistItem;
import com.och.flightbook.speech.TtsProviderFactory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChecklistAdapter extends BaseAdapter{

	private List<ChecklistItem> items;
	private Context mContext;
	private LayoutInflater mInflater;
	private ChecklistAdapter adapter;
	private TtsProviderFactory ttsProviderImpl; 
	private ViewGroup mParent;
	
	public ChecklistAdapter(Context context, List<ChecklistItem> itemsList) {
		  mContext = context;
		  items = itemsList;
		  mInflater = LayoutInflater.from(mContext);
		  adapter = this;
		  ttsProviderImpl = TtsProviderFactory.getInstance();
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
		mParent = parent; 
		RelativeLayout layoutItem;
		
		  //(1) : Reutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item a partir du  layout XML
		    layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_checklist_detail, parent, false);
		  } else {
		  	layoutItem = (RelativeLayout) convertView;
		  }
		  
		  
		  //(2) : Recuperation des TextView de notre layout
		  CheckBox checkBox = (CheckBox)layoutItem.findViewById(R.id.checkBox1);
		        
		  //(3) : Renseignement des valeurs       
		  checkBox.setText(items.get(position).getAction().toUpperCase());
		  ChecklistItem item = items.get(position);
		  if (item.isChecked()) {
			  checkBox.setTextColor(Color.GREEN);
			  checkBox.setChecked(true);
		  } else {
			  checkBox.setTextColor(Color.RED);
			  checkBox.setChecked(false);
		  }
		  
		  // On memorise la position de l'aeronef dans le composant
		  checkBox.setTag(position);
		  
		  // On ajoute un listener 
		  checkBox.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					//CheckBox cb = (CheckBox) v;
					Integer position = (Integer)v.getTag();
					ChecklistItem item = items.get(position);
					if (item.isChecked()) {
						for (int i=position; i<items.size(); i++) {
							items.get(i).setChecked(false);
						}
					} else {
						boolean isOk = true;
						for (int i=0; i<position; i++) {
							ChecklistItem it = items.get(i);
							if (!it.isChecked()) {
								isOk = false;
								break;
							}
						}
						if (isOk) {
							items.get(position).setChecked(true);
							ttsProviderImpl.say(items.get(position).getAction());
						} else {
							Toast.makeText(v.getContext(), R.string.checklist_item_not_check , Toast.LENGTH_LONG ).show();
							ttsProviderImpl.say(v.getContext().getString(R.string.checklist_becarfull));
						}	
					}
					adapter.notifyDataSetChanged();
					
					boolean isOk = true;
					for (int i=0; i<items.size(); i++) {
						if (!items.get(i).isChecked()) {
							isOk = false;
							break;
						}
					}
					if (isOk) {
						mParent.setBackgroundColor(Color.GRAY);
						ttsProviderImpl.addToSay(v.getContext().getString(R.string.checklist_teminated));
					} else {
						mParent.setBackgroundColor(Color.WHITE);
					}
				}
			        	
			});
		  
		  return layoutItem;
	}

}
