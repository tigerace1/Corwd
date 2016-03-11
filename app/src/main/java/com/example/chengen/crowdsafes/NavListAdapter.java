package com.example.chengen.crowdsafes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NavListAdapter extends ArrayAdapter<NavItem> {
    Context context;
    int resLayout;
    List<NavItem>listNavItems;
    public NavListAdapter(Context context, int resource, List<NavItem> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resLayout=resource;
        this.listNavItems=objects;
    }
    @SuppressLint("ViewHolder")@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,resLayout,null);
        TextView tvHelp=(TextView)v.findViewById(R.id.nav_title);
        ImageView imageView=(ImageView)v.findViewById(R.id.ivNavIcon);
        NavItem navItem = listNavItems.get(position);
        tvHelp.setText(navItem.getTitle());
        imageView.setImageBitmap(navItem.getIcon());
        return v;

    }
}
