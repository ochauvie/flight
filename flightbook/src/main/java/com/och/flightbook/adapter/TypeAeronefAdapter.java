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
import com.och.flightbook.listener.TypeAeronefListener;
import com.och.flightbook.model.TypeAeronef;

import java.util.ArrayList;
import java.util.List;

public class TypeAeronefAdapter extends BaseAdapter {

	private List<TypeAeronef> types;
	private LayoutInflater mInflater;

	//Contient la liste des listeners
	private ArrayList<TypeAeronefListener> mListListener = new ArrayList<>();

	public TypeAeronefAdapter(Context context, List<TypeAeronef> typesList) {
		types = typesList;
		  mInflater = LayoutInflater.from(context);
		}
	
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(TypeAeronefListener aListener) {
	    mListListener.add(aListener);
	}
	

	private void sendListenerToDelete(TypeAeronef item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickDelete(item, position);
	    }
	}

    @Override
	public int getCount() {
		if (types!=null) {
			return types.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (types!=null) {
			return types.get(position);
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
		TextView tv_name;
		ImageButton bDelete;

		if (convertView == null) {
		  layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_item_typeaeronef, parent, false);
		} else {
			layoutItem = (RelativeLayout) convertView;
		}

		tv_name = (TextView)layoutItem.findViewById(R.id.name);
		bDelete = (ImageButton)layoutItem.findViewById(R.id.delete);

		if (tv_name != null && bDelete != null) {

			TypeAeronef typeAeronef = types.get(position);
			tv_name.setText(typeAeronef.getLabel());

			tv_name.setTag(position);
			bDelete.setTag(position);

			bDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Integer position = (Integer) v.getTag();
					sendListenerToDelete(types.get(position), position);
				}

			});
		}

		return layoutItem;
	}

}
