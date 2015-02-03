package com.carnetvol.adapter;

import java.util.ArrayList;
import java.util.List;

import com.carnetvol.R;
import com.carnetvol.listener.UpdateChecklistAdapterListener;
import com.carnetvol.model.ChecklistItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

public class UpdateChecklistAdapter extends BaseAdapter {

	private List<ChecklistItem> items;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<UpdateChecklistAdapterListener> mListListener = new ArrayList<UpdateChecklistAdapterListener>();
	
	public UpdateChecklistAdapter(Context context, List<ChecklistItem> itemsList) {
		  mContext = context;
		  items = itemsList;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	
	public void addListener(UpdateChecklistAdapterListener aListener) {
	    mListListener.add(aListener);
	}
	
	private void sendListenerToDelete(ChecklistItem item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickToDelete(item, position);
	    }
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
		 ViewHolder holder;
         if (convertView == null) {
             holder = new ViewHolder();
             convertView = mInflater.inflate(R.layout.activity_item_checklist_detail_update, null);
             holder.captionOrder = (EditText) convertView.findViewById(R.id.itemOrder);
             holder.captionAction = (EditText)convertView.findViewById(R.id.itemAction);
             convertView.setTag(holder);
         } else {
             holder = (ViewHolder) convertView.getTag();
         }
         
         // Fill EditText with the value you have in data source
         	ChecklistItem item = items.get(position);
             holder.captionOrder.setText(String.valueOf(item.getOrder()));
             holder.captionOrder.setId(position);
 
             holder.captionAction.setText(item.getAction());
             holder.captionAction.setId(position);
 
          // We need to update adapter once we finish with editing
         holder.captionOrder.setOnFocusChangeListener(new OnFocusChangeListener() {
             public void onFocusChange(View v, boolean hasFocus) {
                 if (!hasFocus){
                     final int position = v.getId();
                     final EditText Caption = (EditText) v;
                     items.get(position).setOrder(Integer.valueOf(Caption.getText().toString()));
                 }
             }
         });
         
         holder.captionAction.setOnFocusChangeListener(new OnFocusChangeListener() {
             public void onFocusChange(View v, boolean hasFocus) {
                 if (!hasFocus){
                     final int position = v.getId();
                     final EditText Caption = (EditText) v;
                     items.get(position).setAction(Caption.getText().toString());
                 }
             }
         });
         
         ImageButton bDelete = (ImageButton)convertView.findViewById(R.id.itemDelete);
         bDelete.setTag(position);
	  	 if (position==0) {
	  		 bDelete.setVisibility(View.GONE);
	  	 }
	  	 
	  	 // On ajoute un listener sur name
		  bDelete.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					Integer position = (Integer)v.getTag();
					//On previent les listeners qu'il y a eu un clic sur le TextView "tv_name".
					sendListenerToDelete(items.get(position), position);
				}
			        	
			});
 
             return convertView;
         }
	
	class ViewHolder {
		EditText captionOrder;
		EditText captionAction;
		}

}
