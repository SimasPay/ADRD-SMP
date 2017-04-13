package com.payment.simaspay.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import simaspay.payment.com.simaspay.R;

/**
 * Created by widy on 4/12/17.
 * 12
 */

public class CustomSpinnerAdapter extends BaseAdapter {
    private ArrayList<FavoriteData> favName;
    private LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, ArrayList<FavoriteData> favName) {
        this.favName = favName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return favName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderItem viewHolder;
        if(view==null) {
            view = inflter.inflate(R.layout.custom_spinner, viewGroup, false);
            viewHolder = new ViewHolderItem();
            viewHolder.label_txt = (TextView) view.findViewById(R.id.label_fav);
            viewHolder.value_txt = (TextView) view.findViewById(R.id.value_fav);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        if(favName.get(i) != null) {
            viewHolder.label_txt.setText(favName.get(i).getFavoriteLabel());
            viewHolder.value_txt.setText(favName.get(i).getCategoryName());
        }
        return view;
    }

    private class ViewHolderItem {
        TextView label_txt;
        TextView value_txt;
    }
}
